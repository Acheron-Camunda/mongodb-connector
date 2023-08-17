package com.acheron.camunda.connectors.mongodb.util;

/**
 * The Filter class is a utility class that represents an individual filter criterion used in
 * MongoDB query building. It defines the column name, operator, and value to apply the filtering
 * condition.
 *
 * @author HariharanB
 */
public class Filter {
  String colName;
  String operator;
  Object value;

  public Filter(String colName, String operator, Object value) {
    super();
    this.colName = colName;
    this.operator = operator;
    this.value = value;
  }

  public String getColName() {
    return colName;
  }

  public void setColName(String colName) {
    this.colName = colName;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "Filter [colName=" + colName + ", operator=" + operator + ", value=" + value + "]";
  }
}
