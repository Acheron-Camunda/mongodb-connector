package com.acheron.camunda.connectors.mongodb.model;

import java.util.Objects;

/**
 * The QueryResponse class is a implementation of the MongoDBResponse interface. It represents a
 * response from a MongoDB query operation. The class is generic, allowing it to hold a response of
 * any data type.
 *
 * @author HariharanB
 */
public class QueryResponse<T> implements MongoDBResponse {
  private final T response;

  public QueryResponse(T response) {
    super();
    this.response = response;
  }

  @Override
  public int hashCode() {
    return Objects.hash(response);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    QueryResponse<?> other = (QueryResponse<?>) obj;
    return Objects.equals(response, other.response);
  }

  public T getResponse() {
    return response;
  }

  @Override
  public String toString() {
    return "QueryResponse [response=" + response + "]";
  }
}
