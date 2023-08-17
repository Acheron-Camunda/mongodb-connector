package com.acheron.camunda.connectors.mongodb;

import com.acheron.camunda.connectors.mongodb.model.MongoDBRequest;
import com.acheron.camunda.connectors.mongodb.model.MongoDBRequestData;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This Java class is a custom deserializer for JSON objects representing MongoDB requests. It
 * implements the JsonDeserializer interface from the Gson library, allowing it to be used in the
 * process of deserializing JSON data into Java objects
 *
 * @author HariharanB
 */
public class MongodbRequestDeserializer
    implements JsonDeserializer<MongoDBRequest<? extends MongoDBRequestData>> {

  private String typeElementName;
  private Gson gson;
  private Map<String, Class<? extends MongoDBRequestData>> typeRegistry;

  public MongodbRequestDeserializer(String typeElementName) {
    this.typeElementName = typeElementName;
    this.gson = new Gson();
    this.typeRegistry = new HashMap<>();
  }

  /**
   * This static helper method constructs a TypeToken for a MongoDBRequest object with a specific
   * subclass of MongoDBRequestData
   */
  @SuppressWarnings("unchecked")
  private static TypeToken<MongoDBRequest<? extends MongoDBRequestData>> getTypeToken(
      Class<? extends MongoDBRequestData> requestDataClass) {
    return (TypeToken<MongoDBRequest<? extends MongoDBRequestData>>)
        TypeToken.getParameterized(MongoDBRequest.class, requestDataClass);
  }

  /**
   * This method allows adding type mappings to the typeRegistry. It takes a typeName (the name of
   * the type as it appears in the JSON data) and the corresponding requestType (the class
   * representing the MongoDB request).
   */
  public MongodbRequestDeserializer registerType(
      String typeName, Class<? extends MongoDBRequestData> requestType) {
    typeRegistry.put(typeName, requestType);
    return this;
  }

  /**
   * This method is responsible for deserializing JSON data into a MongoDBRequest object. It first
   * extracts the value of the type element from the JSON data. Using the type value, it looks up
   * the appropriate MongoDBRequestData subclass from the typeRegistry. It then invokes the
   * getMongoDBRequest method to create the appropriate MongoDBRequest object with the correct
   * subclass of MongoDBRequestData
   */
  @Override
  public MongoDBRequest<? extends MongoDBRequestData> deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return getTypeElementValue(jsonElement)
        .map(typeRegistry::get)
        .map(MongodbRequestDeserializer::getTypeToken)
        .map(typeToken -> getMongoDBRequest(jsonElement, typeToken))
        .orElse(null);
  }

  /**
   * This helper method extracts the value of the type element from the JSON data and returns it as
   * an optional string.
   */
  private Optional<String> getTypeElementValue(JsonElement jsonElement) {
    JsonObject asJsonObject = jsonElement.getAsJsonObject();
    JsonElement element = asJsonObject.get(typeElementName);
    return Optional.ofNullable(element).map(JsonElement::getAsString);
  }

  /**
   * This method takes a JsonElement and a TypeToken and uses Gson to deserialize the JSON data into
   * a MongoDBRequest object with the specified MongoDBRequestData subclass.
   */
  private MongoDBRequest<? extends MongoDBRequestData> getMongoDBRequest(
      JsonElement jsonElement, TypeToken<MongoDBRequest<? extends MongoDBRequestData>> typeToken) {
    return gson.fromJson(jsonElement, typeToken.getType());
  }
}
