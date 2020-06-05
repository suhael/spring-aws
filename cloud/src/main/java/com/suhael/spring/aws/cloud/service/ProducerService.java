package com.suhael.spring.aws.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Processor.class)
public class ProducerService {

  @Autowired
  private Processor processor;

  public void produce(String message) {

    processor.output().send(MessageBuilder.withPayload(message).build());
  }

}
