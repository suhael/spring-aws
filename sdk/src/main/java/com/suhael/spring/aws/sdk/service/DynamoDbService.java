package com.suhael.spring.aws.sdk.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@Service
public class DynamoDbService {

  private final DynamoDbClient dynamoDbClient;

  public DynamoDbService(DynamoDbClient dynamoDbClient) {
    this.dynamoDbClient = dynamoDbClient;
  }

  public void createTable(String tablename) {
    CreateTableRequest createTableRequest = CreateTableRequest.builder()
        .tableName(tablename)
        .keySchema(KeySchemaElement.builder()
            .keyType(KeyType.HASH)
            .attributeName("id")
            .build())
        .attributeDefinitions(AttributeDefinition.builder()
            .attributeName("id")
            .attributeType(ScalarAttributeType.S)
            .build())
        .provisionedThroughput(ProvisionedThroughput.builder()
            .writeCapacityUnits(5L)
            .readCapacityUnits(5L)
            .build())
        .build();

    dynamoDbClient.createTable(createTableRequest);

    dynamoDbClient.listTables().tableNames()
        .forEach(System.out::println);
  }

}
