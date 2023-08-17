package com.acheron.camunda.connectors.mongodb;

import com.acheron.camunda.connectors.mongodb.model.MongoDBRequest;
import com.google.gson.Gson;
import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as an outbound connector to interact with a MongoDB database. It is designed
 * to be integrated into Camunda workflow as a service task internally, enabling the execution of
 * MongoDB operations based on incoming JSON data.
 *
 * @author HariharanB
 */
@OutboundConnector(
    name = "MongoDB",
    inputVariables = {"operation", "data"},
    type = "com.acheron.camunda.connectors.mongodb:1")
public class MongodbConnectorFunction implements OutboundConnectorFunction {
  private static final Logger LOGGER = LoggerFactory.getLogger(MongodbConnectorFunction.class);
  private final Gson gson;

  public MongodbConnectorFunction() {
    this(GsonSupplier.getGson());
  }

  public MongodbConnectorFunction(Gson gson) {
    this.gson = gson;
  }

  /**
   * This is the method that is executed when the connector service task is encountered in the
   * process flow. This method gets the JSON that comes from the process and converts it to a Java
   * Object of the mapped type. Further, this method invokes the request class to establish a
   * database connection.
   *
   * @author HariharanB
   */
  @Override
  public Object execute(OutboundConnectorContext outboundConnectorContext) throws Exception {
    String variables = outboundConnectorContext.getVariables();
    final var mongoDBRequest = gson.fromJson(variables, MongoDBRequest.class);
    LOGGER.info("{}", mongoDBRequest);

    return mongoDBRequest.invoke(mongoDBRequest.getData().getDatabaseName());
  }
}
