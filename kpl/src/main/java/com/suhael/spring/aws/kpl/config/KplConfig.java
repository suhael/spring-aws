package com.suhael.spring.aws.kpl.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.http.Protocol;
import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
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
  public KinesisAsyncClient kinesisClient(KinesisProperties kinesisProperties) {

    SdkAsyncHttpClient nettyClient = NettyNioAsyncHttpClient
        .builder()
        .protocol(Protocol.HTTP1_1)
        .buildWithDefaults(
            AttributeMap
                .builder()
                .put(
                    SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES,
                    java.lang.Boolean.TRUE
                )
                .build()
        );

    return KinesisAsyncClient.builder()
            .endpointOverride(kinesisProperties.getEndpointURI())
            .httpClient(nettyClient)
            .region(Region.EU_WEST_1)
            .build();
  }

}
