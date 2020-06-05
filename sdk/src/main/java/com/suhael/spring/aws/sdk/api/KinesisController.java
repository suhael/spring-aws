package com.suhael.spring.aws.sdk.api;

import com.suhael.spring.aws.sdk.service.KinesisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class KinesisController {

  private static final Logger LOG = LoggerFactory.getLogger(KinesisController.class);

  private KinesisService kinesisService;

  public KinesisController(KinesisService kinesisService) {
    this.kinesisService = kinesisService;
  }

  @GetMapping(consumes = "application/json", produces = "application/json")
  public String hello() {
    LOG.info("hello");
    kinesisService.createStream();
    return "Hello";
  }

}
