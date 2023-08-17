FROM camunda/connectors:0.21.3
COPY target/mongodb-connector-0.0.1-SNAPSHOT-jar-with-dependencies.jar /opt/app/
ENTRYPOINT ["/start.sh"]