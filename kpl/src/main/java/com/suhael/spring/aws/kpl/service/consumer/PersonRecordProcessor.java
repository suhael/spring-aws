package com.suhael.spring.aws.kpl.service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.kinesis.exceptions.InvalidStateException;
import software.amazon.kinesis.exceptions.ShutdownException;
import software.amazon.kinesis.exceptions.ThrottlingException;
import software.amazon.kinesis.lifecycle.events.InitializationInput;
import software.amazon.kinesis.lifecycle.events.LeaseLostInput;
import software.amazon.kinesis.lifecycle.events.ProcessRecordsInput;
import software.amazon.kinesis.lifecycle.events.ShardEndedInput;
import software.amazon.kinesis.lifecycle.events.ShutdownRequestedInput;
import software.amazon.kinesis.processor.RecordProcessorCheckpointer;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.retrieval.KinesisClientRecord;

/**
 * Processes records retrieved from stock trades stream.
 *
 */
public class PersonRecordProcessor implements ShardRecordProcessor {

  private static final Logger log = LoggerFactory.getLogger(PersonRecordProcessor.class);

  private String kinesisShardId;

  // Reporting interval
  private static final long REPORTING_INTERVAL_MILLIS = 60000L; // 1 minute
  private long nextReportingTimeInMillis;

  // Checkpointing interval
  private static final long CHECKPOINT_INTERVAL_MILLIS = 60000L; // 1 minute
  private long nextCheckpointTimeInMillis;

  @Override
  public void initialize(InitializationInput initializationInput) {
    kinesisShardId = initializationInput.shardId();
    log.info("Initializing record processor for shard: " + kinesisShardId);
    log.info("Initializing @ Sequence: " + initializationInput.extendedSequenceNumber().toString());

    nextReportingTimeInMillis = System.currentTimeMillis() + REPORTING_INTERVAL_MILLIS;
    nextCheckpointTimeInMillis = System.currentTimeMillis() + CHECKPOINT_INTERVAL_MILLIS;
  }

  @Override
  public void processRecords(ProcessRecordsInput processRecordsInput) {
    try {
      log.info("Processing " + processRecordsInput.records().size() + " record(s)");
      processRecordsInput.records().forEach(r -> processRecord(r));
      // If it is time to report stats as per the reporting interval, report stats
      if (System.currentTimeMillis() > nextReportingTimeInMillis) {
        reportStats();
        resetStats();
        nextReportingTimeInMillis = System.currentTimeMillis() + REPORTING_INTERVAL_MILLIS;
      }

      // Checkpoint once every checkpoint interval
      if (System.currentTimeMillis() > nextCheckpointTimeInMillis) {
        checkpoint(processRecordsInput.checkpointer());
        nextCheckpointTimeInMillis = System.currentTimeMillis() + CHECKPOINT_INTERVAL_MILLIS;
      }
    } catch (Throwable t) {
      log.error("Caught throwable while processing records. Aborting.");
      Runtime.getRuntime().halt(1);
    }

  }

  private void reportStats() {
    // TODO: Implement method
  }

  private void resetStats() {
    // TODO: Implement method
  }

  private void processRecord(KinesisClientRecord record) {
    // TODO: Implement method
  }

  @Override
  public void leaseLost(LeaseLostInput leaseLostInput) {
    log.info("Lost lease, so terminating.");
  }

  @Override
  public void shardEnded(ShardEndedInput shardEndedInput) {
    try {
      // Important to checkpoint after reaching end of shard, so we can start processing data from child shards.
      log.info("Reached shard end checkpointing.");
      shardEndedInput.checkpointer().checkpoint();
    } catch (ShutdownException | InvalidStateException e) {
      log.error("Exception while checkpointing at shard end. Giving up.", e);
    }
  }

  @Override
  public void shutdownRequested(ShutdownRequestedInput shutdownRequestedInput) {
    log.info("Scheduler is shutting down, checkpointing.");
    checkpoint(shutdownRequestedInput.checkpointer());

  }

  private void checkpoint(RecordProcessorCheckpointer checkpointer) {
    log.info("Checkpointing shard " + kinesisShardId);
    try {
      checkpointer.checkpoint();
    } catch (ShutdownException se) {
      // Ignore checkpoint if the processor instance has been shutdown (fail over).
      log.info("Caught shutdown exception, skipping checkpoint.", se);
    } catch (ThrottlingException e) {
      // Skip checkpoint when throttled. In practice, consider a backoff and retry policy.
      log.error("Caught throttling exception, skipping checkpoint.", e);
    } catch (InvalidStateException e) {
      // This indicates an issue with the DynamoDB table (check for table, provisioned IOPS).
      log.error("Cannot save checkpoint to the DynamoDB table used by the Amazon Kinesis Client Library.", e);
    }
  }

}
