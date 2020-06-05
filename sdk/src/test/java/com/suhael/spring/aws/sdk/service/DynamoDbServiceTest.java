package com.suhael.spring.aws.sdk.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamoDbServiceTest extends BaseIntegrationTests{

  @Autowired
  private DynamoDbService dynamoDbService;

  @Autowired
  private DynamoDbClient dynamoDbClient;

  @Test
  public void test() {

    dynamoDbService.createTable("suhael");

    assertThat(dynamoDbClient.listTables().tableNames()).contains("suhael");
  }

}
