package com.suhael.spring.aws.kpl.service;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

@EnableScheduling
@Service
public class Producer {

  private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

  private final KinesisAsyncClient kinesisClient;

  public Producer(KinesisAsyncClient kinesisClient) {
    this.kinesisClient = kinesisClient;
  }


  @Scheduled(fixedDelay = 3000L)
  private void produce() {

    LOG.info("write message");
    IntStream.range(1, 200).mapToObj(ipSuffix -> ByteBuffer.wrap(("192.168.0." + ipSuffix).getBytes()))
        .forEach(entry -> {
          byte[] bytes = entry.array();
          // The bytes could be null if there is an issue with the JSON serialization by the Jackson JSON library.
          if (bytes == null) {
            LOG.warn("Could not get JSON bytes for stock trade");
            return;
          }

          LOG.info("Putting trade: " + entry);
          PutRecordRequest request = PutRecordRequest.builder()
              .partitionKey("1") // We use the ticker symbol as the partition key, explained in the Supplemental Information section below.
              .streamName("my-first-stream")
              .data(SdkBytes.fromByteArray(bytes))
              .build();
          try {
            kinesisClient.putRecord(request).get();
          } catch (InterruptedException e) {
            LOG.info("Interrupted, assuming shutdown.");
          } catch (ExecutionException e) {
            LOG.error("Exception while sending data to Kinesis. Will try again next cycle.", e);
          }
        });
  }

}
