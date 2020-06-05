package com.suhael.spring.aws.sdk.service;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

@ContextConfiguration(initializers = BaseIntegrationTests.Initializer.class)
public abstract class BaseIntegrationTests {

  private static LocalStackContainer localStackContainer = new LocalStackContainer()
      .withServices(Service.DYNAMODB);

  static {
    localStackContainer.start();
  }

  static class Initializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues values = TestPropertyValues.of(
          "dynamo.endpointURI=" + localStackContainer.getEndpointConfiguration(Service.DYNAMODB)
              .getServiceEndpoint()
      );

      values.applyTo(configurableApplicationContext);

    }
  }

}
