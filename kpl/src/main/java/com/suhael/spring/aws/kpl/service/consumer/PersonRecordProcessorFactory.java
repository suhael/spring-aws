package com.suhael.spring.aws.kpl.service.consumer;

import org.springframework.stereotype.Component;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

@Component
public class PersonRecordProcessorFactory implements ShardRecordProcessorFactory {
  @Override
  public ShardRecordProcessor shardRecordProcessor() {
    return new PersonRecordProcessor();
  }

}
