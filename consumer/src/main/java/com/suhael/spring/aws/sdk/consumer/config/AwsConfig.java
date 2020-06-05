package com.suhael.spring.aws.sdk.consumer.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

  @Value("${cloud.aws.region.static}")
  String awsRegion;

  @Value("${localstack.kinesis.url}")
  String localStackKinesisUrl;

  public AwsConfig() {
    System.setProperty(SDKGlobalConfiguration.AWS_CBOR_DISABLE_SYSTEM_PROPERTY, "true");
    System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
  }


  @Bean
  public AmazonKinesis amazonKinesis() {

    System.out.println("CREATING BEAN WITH URL " + localStackKinesisUrl);

    BasicAWSCredentials awsCredentials = new BasicAWSCredentials("foo", "bar");

    return AmazonKinesisClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(localStackKinesisUrl, awsRegion))
        .withClientConfiguration(new ClientConfiguration())
        .build();
  }
}
