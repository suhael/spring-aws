package com.suhael.spring.aws.sdk.config;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kinesis")
public class KinesisProperties {

  private URI endpointURI;

  public URI getEndpointURI() {
    return endpointURI;
  }

  public void setEndpointURI(URI endpointURI) {
    this.endpointURI = endpointURI;
  }

}
