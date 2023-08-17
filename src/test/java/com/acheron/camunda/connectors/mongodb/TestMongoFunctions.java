package com.acheron.camunda.connectors.mongodb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acheron.camunda.connectors.mongodb.model.MongoDBResponse;
import com.acheron.camunda.connectors.mongodb.model.QueryResponse;
import com.acheron.camunda.connectors.mongodb.service.CreateCollection;
import com.acheron.camunda.connectors.mongodb.service.DeleteDocuments;
import com.acheron.camunda.connectors.mongodb.service.InsertDocuments;
import com.acheron.camunda.connectors.mongodb.service.RetrieveDocuments;
import com.acheron.camunda.connectors.mongodb.service.UpdateDocuments;
import com.acheron.camunda.connectors.mongodb.util.HelperMethods;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class TestMongoFunctions {

  private CreateCollection createCollection;

  @Mock private MongoDatabase databaseMock;
  @Mock private MongoCollection<Document> collectionMock;
  @InjectMocks private InsertDocuments insertDocuments;
  @InjectMocks private RetrieveDocuments retrieveDocuments;
  @InjectMocks private UpdateDocuments updateDocuments;
  @InjectMocks private DeleteDocuments deleteDocuments;
  private HelperMethods helperMethodsMock;
  @Mock private FindIterable<Document> findIterableMock;
  @Mock private MongoCursor<Document> mongoCursorMock;

  @SuppressWarnings("unchecked")
  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    createCollection = new CreateCollection();
    insertDocuments = new InsertDocuments();
    deleteDocuments = new DeleteDocuments();
    helperMethodsMock = mock(HelperMethods.class);
    databaseMock = mock(MongoDatabase.class);
    collectionMock = mock(MongoCollection.class);
    findIterableMock = mock(FindIterable.class);

    mongoCursorMock = mock(MongoCursor.class);
    // Initialize the RetrieveDocuments instance with the mock HelperMethods
    retrieveDocuments = new RetrieveDocuments(helperMethodsMock);

    // Set up behavior for mocks
    when(databaseMock.getCollection(Mockito.any())).thenReturn(collectionMock);
    when(collectionMock.find(Mockito.any(Bson.class))).thenReturn(findIterableMock);
    when(findIterableMock.iterator()).thenReturn(mongoCursorMock);

    // Mock the behavior of the projection and sort methods to return non-null
    // values
    when(findIterableMock.projection(Mockito.any(Bson.class))).thenReturn(findIterableMock);
    when(findIterableMock.sort(Mockito.any(Bson.class))).thenReturn(findIterableMock);
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("Create - Success")
  void testCreate() throws SQLException {
    createCollection.setDatabaseName("mydb");
    createCollection.setCollectionName("mycollection");

    MongoDBResponse response = createCollection.invokeCut(databaseMock);

    assertEquals(
        "Collection 'mycollection' created successfully",
        ((QueryResponse<String>) response).getResponse());
    verify(databaseMock).createCollection("mycollection");
  }

  @Test
  @DisplayName("Create - Null Database Name")
  void testCreateNullDatabase() {
    createCollection.setCollectionName("mycollection");

    assertThrows(IllegalArgumentException.class, () -> createCollection.invokeCut(databaseMock));
    verify(databaseMock, never()).createCollection(anyString());
  }

  @Test
  @DisplayName("Create - Null Collection Name")
  void testCreateNullCollection() {
    createCollection.setDatabaseName("mydb");

    assertThrows(IllegalArgumentException.class, () -> createCollection.invokeCut(databaseMock));
    verify(databaseMock, never()).createCollection(anyString());
  }

  @Test
  @DisplayName("Insert")
  void testInsert() throws Exception {
    when(databaseMock.getCollection(anyString())).thenReturn(collectionMock);

    insertDocuments.setDatabaseName("mydb");
    insertDocuments.setCollectionName("mycollection");

    List<Map<String, Object>> documentData = new ArrayList<>();
    Map<String, Object> document1 = new HashMap<>();
    document1.put("name", "John");
    document1.put("age", 30);
    documentData.add(document1);

    Map<String, Object> document2 = new HashMap<>();
    document2.put("name", "Jane");
    document2.put("age", 25);
    documentData.add(document2);

    insertDocuments.setDocumentData(documentData);

    QueryResponse<String> expectedResponse =
        new QueryResponse<>("Documents '" + documentData.toString() + "' inserted successfully");

    ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);

    MongoDBResponse response = insertDocuments.invokeCut(databaseMock);

    verify(databaseMock).getCollection("mycollection");
    verify(collectionMock, times(2)).insertOne(documentCaptor.capture());

    List<Document> insertedDocuments = documentCaptor.getAllValues();
    assertEquals(2, insertedDocuments.size());

    assertEquals(expectedResponse, response);
  }

  @Test
  @DisplayName("Retrieval")
  void testBasicRetrieval() {
    List<Document> sampleDocuments =
        Arrays.asList(
            new Document("name", "John").append("age", 30),
            new Document("name", "Jane").append("age", 25));

    when(helperMethodsMock.buildQueryFilter()).thenReturn(new Document());
    when(helperMethodsMock.buildProjection()).thenReturn(new Document());
    when(helperMethodsMock.buildSort()).thenReturn(new Document());

    when(mongoCursorMock.hasNext()).thenReturn(true, true, false);
    when(mongoCursorMock.next()).thenReturn(sampleDocuments.get(0), sampleDocuments.get(1));

    retrieveDocuments.setDatabaseName("mydb");
    retrieveDocuments.setCollectionName("mycollection");
    retrieveDocuments.setFieldNames(Arrays.asList("name", "age"));

    MongoDBResponse response = retrieveDocuments.invokeCut(databaseMock);

    assertEquals(QueryResponse.class, response.getClass());
    @SuppressWarnings("unchecked")
    QueryResponse<List<Map<String, Object>>> queryResponse =
        (QueryResponse<List<Map<String, Object>>>) response;
    List<Map<String, Object>> resultList = queryResponse.getResponse();
    assertEquals(2, resultList.size());

    Map<String, Object> resultDocument1 = resultList.get(0);
    assertEquals("John", resultDocument1.get("name"));
    assertEquals(30, resultDocument1.get("age"));

    Map<String, Object> resultDocument2 = resultList.get(1);
    assertEquals("Jane", resultDocument2.get("name"));
    assertEquals(25, resultDocument2.get("age"));
  }

  @Test
  @DisplayName("Delete")
  void testBasicDelete() {
    String databaseName = "testDB";
    String collectionName = "testCollection";

    List<Document> sampleDocuments =
        Arrays.asList(
            new Document("name", "John").append("age", 30),
            new Document("name", "Jane").append("age", 25));

    when(helperMethodsMock.buildQueryFilter()).thenReturn(new Document());
    when(mongoCursorMock.hasNext()).thenReturn(true, true, false);
    when(mongoCursorMock.next()).thenReturn(sampleDocuments.get(0), sampleDocuments.get(1));

    deleteDocuments.setDatabaseName(databaseName);
    deleteDocuments.setCollectionName(collectionName);

    MongoDBResponse response = deleteDocuments.invokeCut(databaseMock);

    @SuppressWarnings("unchecked")
    QueryResponse<String> queryResponse = (QueryResponse<String>) response;
    String result = queryResponse.getResponse();

    assertEquals("Deleted 2 documents successfully", result);
    verify(collectionMock, times(2)).deleteOne(any(Document.class));
  }

  @Test
  @DisplayName("Update")
  void testBasicUpdate() {
    String databaseName = "testDB";
    String collectionName = "testCollection";
    Map<String, Object> updateMap = new HashMap<>();

    List<Document> sampleDocuments =
        Arrays.asList(
            new Document("name", "John").append("age", 30),
            new Document("name", "Jane").append("age", 25));

    updateMap.put("name", "Juliet");
    Bson updateDocument = new Document("$set", new Document(updateMap));

    updateDocuments.setDatabaseName(databaseName);
    updateDocuments.setCollectionName(collectionName);
    updateDocuments.setUpdateMap(updateMap);

    when(helperMethodsMock.buildQueryFilter()).thenReturn(new Document());
    when(helperMethodsMock.buildUpdateDocument()).thenReturn(updateDocument);
    when(mongoCursorMock.hasNext()).thenReturn(true, true, false);
    when(mongoCursorMock.next()).thenReturn(sampleDocuments.get(0), sampleDocuments.get(1));

    MongoDBResponse response = updateDocuments.invokeCut(databaseMock);

    @SuppressWarnings("unchecked")
    QueryResponse<String> queryResponse = (QueryResponse<String>) response;
    String result = queryResponse.getResponse();

    assertEquals("Updated 2 documents successfully", result);
    verify(collectionMock, times(2)).updateOne(any(Document.class), eq(updateDocument));
  }
}
