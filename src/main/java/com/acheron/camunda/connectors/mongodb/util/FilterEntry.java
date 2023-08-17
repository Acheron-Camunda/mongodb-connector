package com.acheron.camunda.connectors.mongodb.util;

/**
 * The FilterEntry class is a utility class that represents an individual filter criteria entry to
 * be used in MongoDB query building.
 *
 * @author HariharanB
 */
public class FilterEntry {
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
