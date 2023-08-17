package com.acheron.camunda.connectors.mongodb.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DatabaseClient class is responsible for establishing connections to a MongoDB database using
 * the MongoDB Java driver. It provides a method to obtain a MongoDatabase object representing the
 * connection to the specified database.
 *
 * @author HariharanB
 */
public class DatabaseClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseClient.class);
  private static final String USERNAME = System.getenv("MONGODB_USERNAME").strip();
  private static final String PASSWORD = System.getenv("MONGODB_PASSWORD").strip();
  private static final String HOST = System.getenv("MONGODB_HOST").strip();
  private static final String PORT = System.getenv("MONGODB_PORT").strip();

  /**
   * This method is used to establish a connection to the MongoDB database and obtain a
   * MongoDatabase object representing the connection to the specified database.
   */
  public MongoDatabase getConnectionObject(String databaseName) {
    String dbURL =
        "mongodb://" + USERNAME + ":" + PASSWORD + "@" + HOST + ":" + PORT + "/?authSource=admin";
    LOGGER.info("{} Connecting...", dbURL);
    MongoClient mongoClient = connectToDatabase(dbURL);
    return mongoClient.getDatabase(databaseName);
  }

  /**
   * This private method is used to create a MongoClient based on the provided MongoDB connection
   * URL
   */
  private MongoClient connectToDatabase(String dbURL) {
    LOGGER.info("Returning MongoClient");
    return MongoClients.create(dbURL);
  }
}
