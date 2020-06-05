package com.suhael.spring.aws.sdk.config;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dynamo")
public class DynamoProperties {

  private URI endpointURI;

  public URI getEndpointURI() {
    return endpointURI;
  }

  public void setEndpointURI(URI endpointURI) {
    this.endpointURI = endpointURI;
  }

}
