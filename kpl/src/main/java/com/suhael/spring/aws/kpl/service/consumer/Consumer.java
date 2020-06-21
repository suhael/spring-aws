package com.suhael.spring.aws.kpl.service.consumer;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.coordinator.Scheduler;

@Service
public class Consumer {

  private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);

  public Consumer(KinesisAsyncClient kinesisClient,
      DynamoDbAsyncClient dynamoClient,
      CloudWatchAsyncClient cloudWatchClient,
      PersonRecordProcessorFactory personRecordProcessorFactory,
      AsyncTaskExecutor taskExecutor) {

    ConfigsBuilder configsBuilder = new ConfigsBuilder("my-first-stream",
        "kcl-demo", kinesisClient, dynamoClient, cloudWatchClient, UUID
        .randomUUID().toString(), personRecordProcessorFactory);

    Scheduler scheduler = new Scheduler(
        configsBuilder.checkpointConfig(),
        configsBuilder.coordinatorConfig(),
        configsBuilder.leaseManagementConfig(),
        configsBuilder.lifecycleConfig(),
        configsBuilder.metricsConfig(),
        configsBuilder.processorConfig(),
        configsBuilder.retrievalConfig()
    );

    LOG.info("submitting consumer");

    taskExecutor.submit(scheduler);
    LOG.info("finished submitting consumer");
    Runtime.getRuntime().addShutdownHook(new Thread(scheduler::startGracefulShutdown));
  }


}
