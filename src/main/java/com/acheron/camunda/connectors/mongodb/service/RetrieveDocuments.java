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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * This class allows retrieving documents from a MongoDB collection based on specified filters,
 * field names, sorting criteria, and a limit on the number of documents returned. It implements the
 * MongoDBRequestData interface, which defines the contract for MongoDB request data classes. It
 * gets registered in the GSON Supplier based on the operation from the JSON.
 *
 * @author HariharanB
 */
public class RetrieveDocuments implements MongoDBRequestData {
  private String databaseName;
  private String collectionName;
  private List<String> fieldNames;

  @SerializedName("filters")
  private FiltersWrapper filtersWrapper;

  private List<Map<String, String>> orderBy;
  private Integer limit;
  private HelperMethods helpers;

  public RetrieveDocuments(HelperMethods helpers) {
    super();
    this.helpers = helpers;
  }

  /**
   * This method represents a MongoDB request data operation to retrieve documents from a collection
   * within the specified database. The method takes a MongoDatabase object as input, representing
   * the target database from which the documents can be retrieved in the specified collection.
   */
  @Override
  public MongoDBResponse invokeCut(MongoDatabase database) {
    helpers = new HelperMethods();
    helpers.setFieldNames(fieldNames);
    helpers.setOrderBy(orderBy);
    helpers.setFiltersWrapper(filtersWrapper);

    Bson queryFilter = helpers.buildQueryFilter();
    Bson projection = helpers.buildProjection();
    Bson sort = helpers.buildSort();

    MongoCollection<Document> collection = database.getCollection(collectionName);
    FindIterable<Document> documents =
        collection.find(queryFilter).projection(projection).sort(sort);

    if (limit != null && limit > 0) {
      documents = documents.limit(limit);
    }

    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Document document : documents) {
      resultList.add(document);
    }

    return new QueryResponse<>(resultList);
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

  public List<String> getFieldNames() {
    return fieldNames;
  }

  public void setFieldNames(List<String> fieldNames) {
    this.fieldNames = fieldNames;
  }

  public FiltersWrapper getFiltersWrapper() {
    return filtersWrapper;
  }

  public void setFiltersWrapper(FiltersWrapper filtersWrapper) {
    this.filtersWrapper = filtersWrapper;
  }

  public List<Map<String, String>> getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(List<Map<String, String>> orderBy) {
    this.orderBy = orderBy;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public void setHelpers(HelperMethods helpers) {
    this.helpers = helpers;
  }

  @Override
  public String toString() {
    return "RetrieveDocuments [databaseName="
        + databaseName
        + ", collectionName="
        + collectionName
        + ", fieldNames="
        + fieldNames
        + ", filtersWrapper="
        + filtersWrapper
        + ", orderBy="
        + orderBy
        + ", limit="
        + limit
        + "]";
  }
}
