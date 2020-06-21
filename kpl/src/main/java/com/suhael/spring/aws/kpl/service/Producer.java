package com.suhael.spring.aws.kpl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhael.spring.aws.kpl.service.model.Person;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

@Service
public class Producer {

  private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

  private final KinesisAsyncClient kinesisClient;
  private final ObjectMapper objectMapper;

  public Producer(KinesisAsyncClient kinesisClient,
      ObjectMapper objectMapper) {
    this.kinesisClient = kinesisClient;
    this.objectMapper = objectMapper;
  }

  public void produce(Person person) {

    byte[] bytes = toJsonAsBytes(person);
    // The bytes could be null if there is an issue with the JSON serialization by the Jackson JSON library.
    if (bytes == null) {
      LOG.warn("Could not get JSON bytes for stock trade");
      return;
    }

    LOG.info("Putting person: " + person);
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
  }

  private byte[] toJsonAsBytes(Object object) {
    try {
      return objectMapper.writeValueAsBytes(object);
    } catch (IOException e) {
      return null;
    }
  }

}
