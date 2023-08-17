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
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * This class provides functionality to update documents from a MongoDB collection based on
 * specified filters. It implements the MongoDBRequestData interface, which defines the contract for
 * MongoDB request data classes. It gets registered in the GSON Supplier based on the operation from
 * the JSON.
 *
 * @author HariharanB
 */
public class UpdateDocuments implements MongoDBRequestData {
  private String databaseName;
  private String collectionName;
  private Map<String, Object> updateMap;

  @SerializedName("filters")
  private FiltersWrapper filtersWrapper;

  private Integer limit;

  /**
   * This method represents a MongoDB request data operation to update documents in a collection
   * within the specified database. The method takes a MongoDatabase object as input, representing
   * the target database from which the documents can be updated in the specified collection.
   */
  @Override
  public MongoDBResponse invokeCut(MongoDatabase database) {
    HelperMethods helpers = new HelperMethods();
    helpers.setFiltersWrapper(filtersWrapper);
    helpers.setUpdateMap(updateMap);

    Bson queryFilter = helpers.buildQueryFilter();
    Bson updateDocument = helpers.buildUpdateDocument();

    MongoCollection<Document> collection = database.getCollection(collectionName);
    FindIterable<Document> documents = collection.find(queryFilter);

    if (limit != null && limit > 0) {
      documents = documents.limit(limit);
    }

    int updatedCount = 0;
    for (Document document : documents) {
      collection.updateOne(document, updateDocument);
      updatedCount++;
    }

    return new QueryResponse<>("Updated " + updatedCount + " documents successfully");
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

  public Map<String, Object> getUpdateMap() {
    return updateMap;
  }

  public void setUpdateMap(Map<String, Object> updateMap) {
    this.updateMap = updateMap;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public FiltersWrapper getFiltersWrapper() {
    return filtersWrapper;
  }

  public void setFiltersWrapper(FiltersWrapper filtersWrapper) {
    this.filtersWrapper = filtersWrapper;
  }

  @Override
  public String toString() {
    return "UpdateDocuments [databaseName="
        + databaseName
        + ", collectionName="
        + collectionName
        + ", updateMap="
        + updateMap
        + ", filtersWrapper="
        + filtersWrapper
        + ", limit="
        + limit
        + "]";
  }
}
