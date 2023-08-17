package com.acheron.camunda.connectors.mongodb.service;

import com.acheron.camunda.connectors.mongodb.model.MongoDBRequestData;
import com.acheron.camunda.connectors.mongodb.model.MongoDBResponse;
import com.acheron.camunda.connectors.mongodb.model.QueryResponse;
import com.mongodb.client.MongoDatabase;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides functionality to drop a MongoDB database. It implements the
 * MongoDBRequestData interface, which defines the contract for MongoDB request data classes. It
 * gets registered in the GSON Supplier based on the operation from the JSON.
 *
 * @author HariharanB
 */
public class DeleteDatabase implements MongoDBRequestData {
  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDatabase.class);
  private String databaseName;

  /**
   * This method represents a MongoDB request data operation to delete a specified database. The
   * method takes a MongoDatabase object as input, representing the target database that can be
   * deleted.
   */
  @Override
  public MongoDBResponse invokeCut(MongoDatabase database) throws SQLException {
    LOGGER.info("Database Name = {}", databaseName);

    if (databaseName == null) {
      LOGGER.info("Database name is null");
      throw new IllegalArgumentException("Database name cannot be null or empty");
    }
    database.drop();
    QueryResponse<String> queryResponse =
        new QueryResponse<>("Database '" + databaseName + "' dropped successfully");
    LOGGER.info("{}", queryResponse.getResponse());
    return queryResponse;
  }

  @Override
  public String getDatabaseName() {
    return this.databaseName;
  }
}
