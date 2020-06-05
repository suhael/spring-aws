package com.suhael.spring.aws.sdk.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.services.kinesis.KinesisClient;

@Configuration
@EnableConfigurationProperties(KinesisProperties.class)
public class KinesisConfig {

  public KinesisConfig() {
    System.setProperty("AWS_CBOR_DISABLE", "true");
    System.setProperty("aws.cborEnabled", "false");
  }

  @Bean
  public KinesisClient kinesisClient(KinesisProperties kinesisProperties) {
    return apply(kinesisProperties, KinesisClient.builder()).build();
  }


  public <BuilderT extends AwsClientBuilder<BuilderT, ClientT>, ClientT> AwsClientBuilder<BuilderT, ClientT> apply(KinesisProperties kinesisProperties, AwsClientBuilder<BuilderT, ClientT> builder) {

    if(kinesisProperties.getEndpointURI() != null) {
      builder.endpointOverride(kinesisProperties.getEndpointURI());
    }

    return builder;
  }

}
