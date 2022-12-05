package com.mindee.http;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MindeeHttpClientTest extends TestCase {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  MockWebServer mockWebServer = new MockWebServer();

  public void setUp() throws Exception {
    super.setUp();
    mockWebServer.start();

  }

  public void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  public void givenAHttpClient_whenParsed_callsEndpointCorrectly()
      throws IOException, InterruptedException {

    String endpoint = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get("src/test/resources/data/invoice/response_v3/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path)))
    );

    MindeeHttpClient client = new MindeeHttpClient();
    File file = new File("src/test/resources/data/invoice/invoice.pdf");
    Map s = client.parse(new FileInputStream(file),
        file.getName(),
        "dfewsvervdeverv12345",
        endpoint, Boolean.FALSE);

    RecordedRequest request = mockWebServer.takeRequest();
    Assertions.assertEquals("dfewsvervdeverv12345", request.getHeader("Authorization"));
    Assertions.assertNotNull(request.getHeader(HttpHeaders.USER_AGENT));
    Assertions.assertEquals("POST", request.getMethod());

  }

  @Test
  public void givenAResponseFromTheEndpoint_whenDeserializedToMap_returnsCorrectMap()
      throws IOException {

    String endpoint = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get("src/test/resources/data/invoice/response_v3/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path)))
    );

    MindeeHttpClient client = new MindeeHttpClient();
    File file = new File("src/test/resources/data/invoice/invoice.pdf");
    Map s = client.parse(new FileInputStream(file),
        file.getName(),
        "fdfgergverdveve",
        endpoint, Boolean.FALSE);
    Map expected = MAPPER.readValue(
        new File("src/test/resources/data/invoice/response_v3/complete.json"), Map.class);
    Assertions.assertEquals(expected, s);


  }
}
