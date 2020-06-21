package com.suhael.spring.aws.kpl.config;


import java.net.URI;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import software.amazon.awssdk.http.Protocol;
import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.utils.AttributeMap;

@Configuration
@EnableConfigurationProperties(KinesisProperties.class)
public class KplConfig {

  public KplConfig() {
    System.setProperty("AWS_CBOR_DISABLE", "true");
    System.setProperty("aws.cborEnabled", "false");
  }

  @Bean
  public AsyncTaskExecutor taskExecutor() {
    return new SimpleAsyncTaskExecutor("kpl-demo-executor-");
  }

  @Bean
  public DynamoDbAsyncClient dynamoClient(KinesisProperties kinesisProperties, SdkAsyncHttpClient nettyClient) {
    return DynamoDbAsyncClient.builder()
        .endpointOverride(kinesisProperties.getEndpointURI())
        .httpClient(nettyClient)
        .region(Region.EU_WEST_1)
        .build();
  }

  @Bean
  public CloudWatchAsyncClient cloudWatchClient(KinesisProperties kinesisProperties, SdkAsyncHttpClient nettyClient) {

    return CloudWatchAsyncClient.builder()
        .endpointOverride(URI.create("http://localhost:4582"))
        .httpClient(nettyClient)
        .region(Region.EU_WEST_1)
        .build();

  }

  @Bean
  public SdkAsyncHttpClient nettyClient() {
    return NettyNioAsyncHttpClient
        .builder()
        .protocol(Protocol.HTTP1_1)
        .connectionMaxIdleTime(Duration.ofSeconds(5))
        .buildWithDefaults(
            AttributeMap
                .builder()
                .put(
                    SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES,
                    java.lang.Boolean.TRUE
                )
                .build()
        );
  }

  @Bean
  public KinesisAsyncClient kinesisClient(KinesisProperties kinesisProperties, SdkAsyncHttpClient nettyClient) {



    return KinesisAsyncClient.builder()
            .endpointOverride(kinesisProperties.getEndpointURI())
            .httpClient(nettyClient)
            .region(Region.EU_WEST_1)
            .build();
  }

}
