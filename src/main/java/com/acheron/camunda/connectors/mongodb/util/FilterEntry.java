package com.acheron.camunda.connectors.mongodb.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The FilterEntry class is a utility class that represents an individual filter criteria entry to
 * be used in MongoDB query building.
 *
 * @author HariharanB
 */
@JsonTypeName("filterList")
public class FilterEntry {
  @JsonSubTypes(
      value = {
        @JsonSubTypes.Type(value = FilterEntry.class, name = "filter"),
      })
  Filter filter;

  public FilterEntry(Filter filter) {
    super();
    this.filter = filter;
  }

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  @Override
  public String toString() {
    return "FilterEntry [filter=" + filter + "]";
  }
}
