package com.acheron.camunda.connectors.mongodb.service;

import com.acheron.camunda.connectors.mongodb.model.MongoDBRequestData;
import com.acheron.camunda.connectors.mongodb.model.MongoDBResponse;
import com.acheron.camunda.connectors.mongodb.model.QueryResponse;
import com.mongodb.client.MongoDatabase;
import jakarta.validation.constraints.NotBlank;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a MongoDB request data object for creating a new collection in a specific
 * database. It implements the MongoDBRequestData interface, which defines the contract for MongoDB
 * request data classes. It gets registered in the GSON Supplier based on the operation from the
 * JSON.
 *
 * @author HariharanB
 */
public class CreateCollection implements MongoDBRequestData {
  private static final Logger LOGGER = LoggerFactory.getLogger(CreateCollection.class);
  @NotBlank private String databaseName;
  @NotBlank private String collectionName;

  /**
   * This method represents a MongoDB request data operation to create a new collection within the
   * specified database. The method takes a MongoDatabase object as input, representing the target
   * database in which the new collection will be created.
   */
  @Override
  public MongoDBResponse invokeCut(MongoDatabase database) throws SQLException {
    LOGGER.info("Database Name = {}, Collection Name ={}", databaseName, collectionName);

    if (databaseName == null) {
      LOGGER.info("Database name is null");
      throw new IllegalArgumentException("Database name cannot be null or empty");
    }

    if (collectionName == null) {
      LOGGER.info("Collection nme is null");
      throw new IllegalArgumentException("Collection name cannot be null or empty");
    }

    if (database.listCollectionNames() != null) {
      for (String collection : database.listCollectionNames()) {
        if (collectionName.equals(collection)) {
          LOGGER.info("Collection already exists");
          throw new IllegalStateException(
              "Collection '" + collectionName + "' already exists in the database");
        }
      }
    }
    database.createCollection(collectionName);
    QueryResponse<String> queryResponse =
        new QueryResponse<>("Collection '" + collectionName + "' created successfully");
    LOGGER.info("{}", queryResponse.getResponse());
    return queryResponse;
  }

  @Override
  public String getDatabaseName() {
    return databaseName;
  }

  public String getCollectionName() {
    return collectionName;
  }

  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  @Override
  public String toString() {
    return "CreateCollection [databaseName="
        + databaseName
        + ", collectionName="
        + collectionName
        + "]";
  }
}
