package com.suhael.spring.aws.sdk.consumer.service;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import com.amazonaws.services.kinesis.model.ShardIteratorType;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

  private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);

  private final AmazonKinesis amazonKinesis;
  private GetShardIteratorResult shardIterator;

  public Consumer(AmazonKinesis amazonKinesis) {
    this.amazonKinesis = amazonKinesis;
  }

  public void consume() {

    GetRecordsRequest recordsRequest = new GetRecordsRequest();
    recordsRequest.setShardIterator(shardIterator.getShardIterator());
    recordsRequest.setLimit(25);

    GetRecordsResult recordsResult = amazonKinesis.getRecords(recordsRequest);
    LOG.info("getting records");
    while (!recordsResult.getRecords().isEmpty()) {
      LOG.info("records is not empty");
      recordsResult.getRecords().stream()
          .map(record -> new String(record.getData().array()))
          .forEach(System.out::println);

      recordsRequest.setShardIterator(recordsResult.getNextShardIterator());
      recordsResult = amazonKinesis.getRecords(recordsRequest);
    }
  }

  @PostConstruct
  private void buildShardIterator() {
    GetShardIteratorRequest readShardsRequest = new GetShardIteratorRequest();
    readShardsRequest.setStreamName("my-first-stream");
    readShardsRequest.setShardIteratorType(ShardIteratorType.LATEST);
    readShardsRequest.setShardId("shardId-000000000000");

    this.shardIterator = amazonKinesis.getShardIterator(readShardsRequest);
  }

}
