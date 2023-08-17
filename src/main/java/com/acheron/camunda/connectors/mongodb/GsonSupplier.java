package com.acheron.camunda.connectors.mongodb;

import com.acheron.camunda.connectors.mongodb.model.MongoDBRequest;
import com.acheron.camunda.connectors.mongodb.service.CreateCollection;
import com.acheron.camunda.connectors.mongodb.service.DeleteCollection;
import com.acheron.camunda.connectors.mongodb.service.DeleteDatabase;
import com.acheron.camunda.connectors.mongodb.service.DeleteDocuments;
import com.acheron.camunda.connectors.mongodb.service.InsertDocuments;
import com.acheron.camunda.connectors.mongodb.service.RetrieveDocuments;
import com.acheron.camunda.connectors.mongodb.service.UpdateDocuments;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This is the GSON supplier method that maps the model class according to the operation chosen by
 * the user in the Camunda Modeler.
 *
 * @author HariharanB
 */
public final class GsonSupplier {

  private static final MongodbRequestDeserializer DESERIALIZER =
      new MongodbRequestDeserializer("operation")
          .registerType("mongodb.create-collection", CreateCollection.class)
          .registerType("mongodb.insert-documents", InsertDocuments.class)
          .registerType("mongodb.retrieve-documents", RetrieveDocuments.class)
          .registerType("mongodb.update-documents", UpdateDocuments.class)
          .registerType("mongodb.delete-documents", DeleteDocuments.class)
          .registerType("mongodb.delete-collection", DeleteCollection.class)
          .registerType("mongodb.delete-database", DeleteDatabase.class);

  private static final Gson GSON =
      new GsonBuilder().registerTypeAdapter(MongoDBRequest.class, DESERIALIZER).create();

  private GsonSupplier() {}

  public static Gson getGson() {
    return GSON;
  }
}
