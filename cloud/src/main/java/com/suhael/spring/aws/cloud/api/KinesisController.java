package com.suhael.spring.aws.cloud.api;

import com.suhael.spring.aws.cloud.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class KinesisController {

  private static final Logger LOG = LoggerFactory.getLogger(KinesisController.class);

  private final ProducerService producerService;

  public KinesisController(ProducerService producerService) {
    this.producerService = producerService;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public void hello(@RequestBody String message) {
    producerService.produce(message);
  }
}
