package com.acheron.camunda.connectors.mongodb.service;

import com.acheron.camunda.connectors.mongodb.model.MongoDBRequestData;
import com.acheron.camunda.connectors.mongodb.model.MongoDBResponse;
import com.acheron.camunda.connectors.mongodb.model.QueryResponse;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a MongoDB request data object for inserting new record in a specific
 * collection and database. It implements the MongoDBRequestData interface, which defines the
 * contract for MongoDB request data classes. It gets registered in the GSON Supplier based on the
 * operation from the JSON.
 *
 * @author HariharanB
 */
public class InsertDocuments implements MongoDBRequestData {

  private static final Logger LOGGER = LoggerFactory.getLogger(InsertDocuments.class);

  @NotBlank private String databaseName;
  @NotBlank private String collectionName;
  @NotEmpty private List<Map<String, Object>> documentData;

  /**
   * This method represents a MongoDB request data operation to insert documents into a collection
   * within the specified database. The method takes a MongoDatabase object as input, representing
   * the target database in which the insertion will take place.
   */
  @Override
  public MongoDBResponse invokeCut(MongoDatabase database) throws SQLException {
    LOGGER.info(
        "Database Name = {}, Collection Name ={}, DocumentData={}",
        databaseName,
        collectionName,
        documentData);

    MongoCollection<Document> collection = database.getCollection(collectionName);

    // Insert each record into the collection
    for (Map<String, Object> document : documentData) {
      Document insert = new Document(document);
      LOGGER.info("Inserting document :{}", insert);
      collection.insertOne(insert);
    }

    QueryResponse<String> queryResponse =
        new QueryResponse<>("Documents '" + documentData.toString() + "' inserted successfully");

    LOGGER.info("{}", queryResponse.getResponse());
    return queryResponse;
  }

  public String getCollectionName() {
    return collectionName;
  }

  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }

  public List<Map<String, Object>> getDocumentData() {
    return documentData;
  }

  public void setDocumentData(List<Map<String, Object>> documentData) {
    this.documentData = documentData;
  }

  @Override
  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  @Override
  public String toString() {
    return "InsertDocuments [databaseName="
        + databaseName
        + ", collectionName="
        + collectionName
        + ", documentData="
        + documentData
        + "]";
  }
}
