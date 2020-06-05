package com.suhael.spring.aws.cloud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Processor.class)
public class ConsumerService {

  private static final Logger LOG = LoggerFactory.getLogger(ConsumerService.class);

  @StreamListener(Processor.INPUT)
  public void consume(String message) {
    LOG.info(message);
  }

}
