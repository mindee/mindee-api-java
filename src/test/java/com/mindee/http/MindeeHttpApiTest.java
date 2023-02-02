package com.mindee.http;

import com.mindee.ParseParameter;
import com.mindee.MindeeSettings;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.invoice.InvoiceV4Inference;
import com.mindee.utils.MindeeException;
import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MindeeHttpApiTest extends TestCase {

  MockWebServer mockWebServer = new MockWebServer();

  public void setUp() throws Exception {
    super.setUp();
    mockWebServer.start();
  }

  public void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void givenAResponseFromTheEndpoint_whenDeserialized_mustHaveValidSummary()
    throws IOException {

    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get("src/test/resources/data/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
      .setResponseCode(200)
      .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/data/invoice/invoice.pdf");
    MindeeHttpApi client = new MindeeHttpApi(new MindeeSettings("abc", url));
    Document<InvoiceV4Inference> document = client.predict(
      InvoiceV4Inference.class,
      ParseParameter.builder()
        .file(Files.readAllBytes(file.toPath()))
        .fileName(file.getName())
        .build());

    Assertions.assertNotNull(document);

    String[] actualLines = document.toString().split(System.lineSeparator());
    String actualSummary = String.join(String.format("%n"), actualLines);
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/data/invoice/response_v4/summary_full.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);

    Assertions.assertNotNull(document);
    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenError_withDetailNodeAsAnObject_mustThrowMindeeException()
    throws IOException {

    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get("src/test/resources/data/errors/with_object_response_in_detail.json");
    mockWebServer.enqueue(new MockResponse()
      .setResponseCode(400)
      .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/data/invoice/invoice.pdf");
    MindeeHttpApi client = new MindeeHttpApi(new MindeeSettings("abc", url));

    Assertions.assertThrows(
      MindeeException.class,
      () -> client.predict(
        InvoiceV4Inference.class,
        ParseParameter.builder()
          .file(Files.readAllBytes(file.toPath()))
          .fileName(file.getName())
          .build()));
  }
}
