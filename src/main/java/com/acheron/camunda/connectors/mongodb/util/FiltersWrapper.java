package com.acheron.camunda.connectors.mongodb.util;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a utility class that provides functionality to manage a list of FilterEntry objects
 * and a logical operator for building complex MongoDB query filters.
 *
 * @author HariharanB
 */
public class FiltersWrapper {
  List<FilterEntry> filterList;
  String logicalOperator;

  private static final Logger LOGGER = LoggerFactory.getLogger(FiltersWrapper.class);

  public FiltersWrapper() {
    this.filterList = new ArrayList<>();
  }

  public void addFilterEntry(FilterEntry filterEntry) {
    LOGGER.info("Adding FilterEntry to the list");
    filterList.add(filterEntry);
  }

  public List<FilterEntry> getFilterList() {
    return filterList;
  }

  public void setFilterList(List<FilterEntry> filterList) {
    this.filterList = filterList;
  }

  public String getLogicalOperator() {
    return logicalOperator;
  }

  public void setLogicalOperator(String logicalOperator) {
    this.logicalOperator = logicalOperator;
  }

  @Override
  public String toString() {
    return "FiltersWrapper [filterList="
        + filterList
        + ", logicalOperator="
        + logicalOperator
        + "]";
  }
}
