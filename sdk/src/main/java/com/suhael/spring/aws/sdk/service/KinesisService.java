package com.suhael.spring.aws.sdk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest;

@Service
public class KinesisService {

  private static final Logger LOG = LoggerFactory.getLogger(KinesisService.class);

  private final KinesisClient kinesisClient;

  public KinesisService(KinesisClient kinesisClient) {
    this.kinesisClient = kinesisClient;
  }

  public void createStream() {

    LOG.info("creating stream");

    CreateStreamRequest createStreamRequest = CreateStreamRequest.builder()
        .streamName("suhael-stream")
        .shardCount(1)
        .build();
    kinesisClient.createStream(createStreamRequest);

    kinesisClient.listStreams().streamNames().forEach(System.out::println);
  }
}
