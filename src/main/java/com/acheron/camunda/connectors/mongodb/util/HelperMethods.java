package com.acheron.camunda.connectors.mongodb.util;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HelperMethods class is a utility class that provides various methods to assist in building
 * MongoDB query filters, projections, sorting criteria, and update documents.
 *
 * @author HariharanB
 */
public class HelperMethods {

  private List<String> fieldNames;
  private FiltersWrapper filtersWrapper;
  private List<Map<String, String>> orderBy;
  private Map<String, Object> updateMap;
  private static final Logger LOGGER = LoggerFactory.getLogger(HelperMethods.class);

  /**
   * Builds the MongoDB projection to specify which fields to include or exclude in the query
   * result.
   */
  public Bson buildProjection() {
    Bson projection = new Document("_id", 0);

    LOGGER.info("Checking projections for null");
    if (fieldNames != null && !fieldNames.isEmpty()) {
      LOGGER.info("Fields are not null");
      List<Bson> includeList = new ArrayList<>();
      for (String fieldName : fieldNames) {
        LOGGER.info("Adding {} to the projection", fieldName);
        includeList.add(Projections.include(fieldName));
      }
      projection = Projections.fields(includeList);
    }
    LOGGER.info("Returning Projection values");
    return projection;
  }

  /**
   * Constructs the MongoDB query filter based on the provided filter entries and logical operators,
   * such as "AND," "OR," or "NOT."
   */
  public Bson buildQueryFilter() {
    List<Bson> filters = buildFilters();

    // Check if filters list is empty
    if (filters.isEmpty()) {
      LOGGER.info("Returning new Document since Filters are empty");
      return new Document();
    } else if (filters.size() == 1 && filtersWrapper.logicalOperator == null) {
      LOGGER.info("Only one filter is present without operators");
      return Filters.and(filters.get(0));
    } else if (filtersWrapper != null && filtersWrapper.logicalOperator != null) {
      if (filtersWrapper.logicalOperator.equalsIgnoreCase("AND")) {
        LOGGER.info("Returning filters with AND");
        return Filters.and(filters);
      } else if (filtersWrapper.logicalOperator.equalsIgnoreCase("OR")) {
        LOGGER.info("Returning filters with OR");
        return Filters.or(filters);
      } else if (filtersWrapper.logicalOperator.equalsIgnoreCase("NOT")) {
        LOGGER.info("Returning filters with NOT");
        return Filters.nor(filters);
      }
    }
    LOGGER.info(
        "Returning filters in default mode since logicalOperator is invalid or not provided");
    return Filters.and(filters);
  }

  /**
   * Generates a list of MongoDB filters from the FiltersWrapper containing individual filter
   * criteria.
   */
  public List<Bson> buildFilters() {
    List<Bson> filters = new ArrayList<>();
    LOGGER.info("Checking the filters for null");
    if (filtersWrapper != null
        && filtersWrapper.filterList != null
        && !filtersWrapper.filterList.isEmpty()) {
      LOGGER.info("Filters are present");
      for (FilterEntry filterEntry : filtersWrapper.filterList) {
        LOGGER.info("Adding {} to the filters", filterEntry);
        Bson filter = buildFilter(filterEntry.filter);
        if (filter != null) {
          filters.add(filter);
        }
      }
    }

    return filters;
  }

  /**
   * Builds a single MongoDB filter from a Filter entry, consisting of column name, operator, and
   * value.
   *
   * @param filterEntry
   */
  public Bson buildFilter(Filter filterEntry) {
    String colName = filterEntry.colName;
    String operator = filterEntry.operator;
    Object value = filterEntry.value;

    Bson filter = null;
    if (operator != null && value != null) {
      filter = buildBsonFilter(colName, operator, value);
    }
    LOGGER.info("Returning filter");
    return filter;
  }

  /**
   * Constructs a MongoDB Bson filter based on the provided column name, operator, and value.
   *
   * @param colName
   * @param operator
   * @param value
   */
  public Bson buildBsonFilter(String colName, String operator, Object value) {
    LOGGER.info("Building the filters");
    if (value instanceof Number number) {
      LOGGER.info("value is a number");
      switch (operator) {
        case ">":
          return Filters.gt(colName, number);
        case ">=":
          return Filters.gte(colName, number);
        case "<":
          return Filters.lt(colName, number);
        case "<=":
          return Filters.lte(colName, number);
        case "!=":
          return Filters.ne(colName, number);
        default:
          return Filters.eq(colName, number);
      }
    } else if (value instanceof String string) {
      LOGGER.info("value is a string");
      if (operator.equals("like")) return Filters.regex(colName, string);
      else return Filters.eq(colName, string);
    } else if (value instanceof List) {
      LOGGER.info("value is a list");
      switch (operator) {
        case "in":
          return Filters.in(colName, (List<?>) value);
        case "nin":
          return Filters.nin(colName, (List<?>) value);
        default:
          return Filters.eq(colName, value);
      }
    } else {
      return Filters.eq(colName, value);
    }
  }

  /**
   * Builds the MongoDB sorting criteria based on the specified fields and sorting order (ascending
   * or descending).
   */
  public Bson buildSort() {
    Bson sort = new Document();
    LOGGER.info("Checking order by for null");
    if (orderBy != null && !orderBy.isEmpty()) {
      for (Map<String, String> orderByEntry : orderBy) {
        String sortOn = orderByEntry.get("sortOn");
        String order = orderByEntry.get("order");
        if (sortOn != null && order != null) {
          if (order.equalsIgnoreCase("asc")) {
            LOGGER.info("Sort in ASC");
            sort = Sorts.ascending(sortOn);
          } else if (order.equalsIgnoreCase("desc")) {
            LOGGER.info("Sort in DESC");
            sort = Sorts.descending(sortOn);
          }
        }
      }
    }

    return sort;
  }

  /**
   * Creates a MongoDB update document using the provided map of field-value pairs for updating
   * documents in the collection.
   */
  public Bson buildUpdateDocument() {
    return new Document("$set", new Document(updateMap));
  }

  public void setFieldNames(List<String> fieldNames) {
    this.fieldNames = fieldNames;
  }

  public void setFiltersWrapper(FiltersWrapper filtersWrapper) {
    this.filtersWrapper = filtersWrapper;
  }

  public void setOrderBy(List<Map<String, String>> orderBy) {
    this.orderBy = orderBy;
  }

  public void setUpdateMap(Map<String, Object> updateMap) {
    this.updateMap = updateMap;
  }
}
