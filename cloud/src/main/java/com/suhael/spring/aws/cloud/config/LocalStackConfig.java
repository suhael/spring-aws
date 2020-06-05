package com.suhael.spring.aws.cloud.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesisAsync;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalStackConfig {

  @Bean
  public AmazonKinesisAsync kinesisClient() {
    return AmazonKinesisAsyncClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "eu-west-1"))
        .build();
  }

  @Bean
  public AmazonDynamoDBAsync dynamoDB() {
    return AmazonDynamoDBAsyncClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "eu-west-1"))
        .build();
  }
}
