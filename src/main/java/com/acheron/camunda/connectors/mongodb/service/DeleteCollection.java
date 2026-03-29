package com.acheron.camunda.connectors.mongodb.service;

import com.acheron.camunda.connectors.mongodb.model.MongoDBRequestData;
import com.acheron.camunda.connectors.mongodb.model.MongoDBResponse;
import com.acheron.camunda.connectors.mongodb.model.QueryResponse;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.SQLException;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides functionality to delete a MongoDB collection. It implements the
 * MongoDBRequestData interface, which defines the contract for MongoDB request data classes. It
 * gets registered in the GSON Supplier based on the operation from the JSON.
 *
 * @author HariharanB
 */
public class DeleteCollection implements MongoDBRequestData {
  private String databaseName;
  private String collectionName;
  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCollection.class);

  /**
   * This method represents a MongoDB request data operation to delete a collection within the
   * specified database. The method takes a MongoDatabase object as input, representing the target
   * database from which the specified collection can be deleted.
   */
  @Override
  public MongoDBResponse invokeCut(MongoDatabase database) throws SQLException {
    if (databaseName == null) {
      LOGGER.info("Database name is null");
      throw new IllegalArgumentException("Database name cannot be null or empty");
    }
    if (collectionName == null) {
      LOGGER.info("Collection name is null");
      throw new IllegalArgumentException("Collection name cannot be null or empty");
    }
    MongoCollection<Document> collection = database.getCollection(collectionName);
    collection.drop();
    QueryResponse<String> queryResponse =
        new QueryResponse<>("Collection '" + collectionName + "' dropped successfully");
    LOGGER.info("{}", queryResponse.getResponse());
    return queryResponse;
  }

  @Override
  public String getDatabaseName() {
    return this.databaseName;
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
}
