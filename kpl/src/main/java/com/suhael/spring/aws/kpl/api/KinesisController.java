package com.suhael.spring.aws.kpl.api;

import com.suhael.spring.aws.kpl.service.Producer;
import com.suhael.spring.aws.kpl.service.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class KinesisController {

  private static final Logger LOG = LoggerFactory.getLogger(KinesisController.class);

  private final Producer produce;

  public KinesisController(Producer produce) {
    this.produce = produce;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public void hello(@RequestBody Person person) {
    LOG.info("Sending person {}", person);
    produce.produce(person);
  }
}
