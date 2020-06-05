package com.suhael.spring.aws.sdk.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@EnableConfigurationProperties(DynamoProperties.class)
public class DynamoConfig {

  @Bean
  public DynamoDbClient dynamoDbClient(DynamoProperties dynamoProperties) {
    return apply(dynamoProperties, DynamoDbClient.builder()).build();
  }

  public <BuilderT extends AwsClientBuilder<BuilderT, ClientT>, ClientT> AwsClientBuilder<BuilderT, ClientT> apply(DynamoProperties dynamoProperties, AwsClientBuilder<BuilderT, ClientT> builder) {

    if(dynamoProperties.getEndpointURI() != null) {
      builder.endpointOverride(dynamoProperties.getEndpointURI());
    }

    return builder;
  }

}
