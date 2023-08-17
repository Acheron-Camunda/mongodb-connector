package com.acheron.camunda.connectors.mongodb.model;

import com.mongodb.client.MongoDatabase;
import java.sql.SQLException;

/**
 * The interface defines the contract for classes that represent data associated with MongoDB
 * operations coming from MongoDBRequest.
 *
 * @author HariharanB
 */
public interface MongoDBRequestData {
  MongoDBResponse invokeCut(final MongoDatabase database) throws SQLException;

  String getDatabaseName();
}
