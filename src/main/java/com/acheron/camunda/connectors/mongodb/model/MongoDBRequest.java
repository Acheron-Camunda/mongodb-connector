package com.acheron.camunda.connectors.mongodb.model;

import com.acheron.camunda.connectors.mongodb.util.DatabaseClient;
import com.mongodb.client.MongoDatabase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MongoDBRequest class represents a request object for performing MongoDB operations. It is a
 * generic class that takes a type parameter T, which extends the MongoDBRequestData class. This
 * class is used to execute various MongoDB operations on a specific database.
 */
public class MongoDBRequest<T extends MongoDBRequestData> {

  @NotBlank private String operation;
  @Valid @NotNull private T data;

  private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBRequest.class);

  /**
   * This method is used to execute the MongoDB operation. It takes the name of the target database
   * as input and returns a MongoDBResponse object.
   *
   * <p>It establishes a connection to the specified database using the DatabaseClient class. Then,
   * it invokes the invokeCut method on the data object with the connected database to perform the
   * actual MongoDB operation and the result is returned as a MongoDBResponse object
   */
  public MongoDBResponse invoke(String databaseName) throws SQLException {
    MongoDatabase database = null;
    try {
      LOGGER.info("Gettting the connection object");
      database = new DatabaseClient().getConnectionObject(databaseName);
    } catch (Exception e) {
      LOGGER.info(e.getMessage());
    }
    return data.invokeCut(database);
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "MongoDBRequest [operation=" + operation + ", data=" + data + "]";
  }
}
