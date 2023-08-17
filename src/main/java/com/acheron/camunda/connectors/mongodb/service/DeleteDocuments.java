package com.acheron.camunda.connectors.mongodb.service;

import com.acheron.camunda.connectors.mongodb.model.MongoDBRequestData;
import com.acheron.camunda.connectors.mongodb.model.MongoDBResponse;
import com.acheron.camunda.connectors.mongodb.model.QueryResponse;
import com.acheron.camunda.connectors.mongodb.util.FiltersWrapper;
import com.acheron.camunda.connectors.mongodb.util.HelperMethods;
import com.google.gson.annotations.SerializedName;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * This class provides functionality to delete documents from a MongoDB collection based on
 * specified filters. It implements the MongoDBRequestData interface, which defines the contract for
 * MongoDB request data classes. It gets registered in the GSON Supplier based on the operation from
 * the JSON.
 *
 * @author HariharanB
 */
public class DeleteDocuments implements MongoDBRequestData {
  private String databaseName;
  private String collectionName;

  @SerializedName("filters")
  private FiltersWrapper filtersWrapper;

  private Integer limit;

  /**
   * This method represents a MongoDB request data operation to delete documents in a collection
   * within the specified database. The method takes a MongoDatabase object as input, representing
   * the target database from which the documents can be deleted in the specified collection.
   */
  @Override
  public MongoDBResponse invokeCut(MongoDatabase database) {
    HelperMethods helpers = new HelperMethods();
    helpers.setFiltersWrapper(filtersWrapper);

    Bson queryFilter = helpers.buildQueryFilter();

    MongoCollection<Document> collection = database.getCollection(collectionName);
    FindIterable<Document> documents = collection.find(queryFilter);

    if (limit != null && limit > 0) {
      documents = documents.limit(limit);
    }

    int deletedCount = 0;
    for (Document document : documents) {
      collection.deleteOne(document);
      deletedCount++;
    }

    return new QueryResponse<>("Deleted " + deletedCount + " documents successfully");
  }

  @Override
  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  public String getCollectionName() {
    return collectionName;
  }

  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }

  public FiltersWrapper getFiltersWrapper() {
    return filtersWrapper;
  }

  public void setFiltersWrapper(FiltersWrapper filtersWrapper) {
    this.filtersWrapper = filtersWrapper;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  @Override
  public String toString() {
    return "DeleteDocuments [databaseName="
        + databaseName
        + ", collectionName="
        + collectionName
        + ", filtersWrapper="
        + filtersWrapper
        + ", limit="
        + limit
        + "]";
  }
}
