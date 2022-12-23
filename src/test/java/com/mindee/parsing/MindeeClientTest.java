package com.mindee.parsing;

import com.mindee.MindeeClient;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.invoice.InvoiceV4Inference;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MindeeClientTest {

  MindeeClient client;
  MockWebServer mockWebServer = new MockWebServer();

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer.start();
    client = new MindeeClient(
      new MindeeSettings(
        "abc",
        String.format("http://localhost:%s", mockWebServer.getPort())));
  }

  @AfterEach
  public void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void givenAClientWithMissingConfigured_whenParsed_thenShouldThrowException() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> new MindeeSettings("", ""));
  }

  @Test
  void givenAClientWithInvoiceConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
    throws IOException {

    Path path = Paths.get("src/test/resources/data/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
      .setResponseCode(200)
      .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/data/invoice/invoice.pdf");
    Document<InvoiceV4Inference> document = client.parse(
      InvoiceV4Inference.class,
      file);

    Assertions.assertNotNull(document);

    String[] actualLines = document.toString().split(System.lineSeparator());
    String actualSummary = String.join(String.format("%n"), actualLines);
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/data/invoice/response_v4/summary.txt"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);

    Assertions.assertNotNull(document);
    Assertions.assertEquals(expectedSummary, actualSummary);
  }
}
