package com.suhael.spring.aws.sdk.consumer.api;

import com.suhael.spring.aws.sdk.consumer.service.Consumer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class HelloController {

  private Consumer consumer;

  public HelloController(Consumer consumer) {
    this.consumer = consumer;
  }

  @GetMapping(consumes = "application/json", produces = "application/json")
  public String hello() {
    consumer.consume();
    return "Hello";
  }

}
