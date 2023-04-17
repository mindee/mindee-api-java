package com.mindee.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mindee.MindeeSettings;
import com.mindee.ParseParameter;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.invoice.InvoiceV4Inference;
import com.mindee.utils.MindeeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().useSystemProperties();

    MindeeHttpApi client = MindeeHttpApi.builder().mindeeSettings(new MindeeSettings("abc", url))
      .build();
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
  void givenAUrlBuilderFunction_whenParsed_callsTheCorrectUrl()
    throws IOException, InterruptedException {

    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    String mockPath = "/testinvoice/v2/test";
    Path path = Paths.get("src/test/resources/data/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
      .setResponseCode(200)
      .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/data/invoice/invoice.pdf");

    MindeeHttpApi client = MindeeHttpApi.builder()
      .mindeeSettings(new MindeeSettings("abc", url))
      .urlFromEndpoint(
        endpoint -> String.format("http://localhost:%s%s", mockWebServer.getPort(), mockPath))
      .build();
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
    Assertions.assertEquals(mockPath, mockWebServer.takeRequest().getPath());
  }

  @Test
  void givenAHttpClientBuilder_whenParseCalled_usesClientBuilderToMakeHttpClient()
    throws IOException {

    WireMockRule proxyMock = new WireMockRule();
    proxyMock.start();
    int proxyPort = proxyMock.port();

    String url = String.format("http://localhost:%s", mockWebServer.getPort());

    proxyMock.stubFor(post(urlMatching(".*"))
      .willReturn(aResponse().proxiedFrom(url)));

    Path path = Paths.get("src/test/resources/data/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
      .setResponseCode(200)
      .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/data/invoice/invoice.pdf");

    HttpHost proxy = new HttpHost("localhost", proxyPort);
    DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
    HttpClientBuilder httpclientBuilder = HttpClients.custom()
      .setRoutePlanner(routePlanner);

    MindeeHttpApi client = MindeeHttpApi.builder()
      .mindeeSettings(new MindeeSettings("abc", url))
      .httpClientBuilder(httpclientBuilder)
      .build();
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

    proxyMock.verify(postRequestedFor(urlEqualTo("/products/Mindee/invoices/v4/predict"))
      .withHeader("Authorization", containing("abc")));
    Assertions.assertNotNull(document);
    Assertions.assertEquals(expectedSummary, actualSummary);
    proxyMock.shutdown();

  }

  @Test
  void givenError_withDetailNodeAsAnObject_mustThrowMindeeException()
    throws IOException {

    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get(
      "src/test/resources/data/errors/complete_with_object_response_in_detail.json");
    mockWebServer.enqueue(new MockResponse()
      .setResponseCode(400)
      .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/data/invoice/invoice.pdf");
    MindeeHttpApi client = MindeeHttpApi.builder().mindeeSettings(new MindeeSettings("abc", url))
      .build();
    byte[] fileInBytes = Files.readAllBytes(file.toPath());
    ParseParameter parseParameter =
      ParseParameter.builder()
        .file(fileInBytes)
        .fileName(file.getName())
        .build();

    Assertions.assertThrows(
      MindeeException.class,
      () -> client.predict(
        InvoiceV4Inference.class,
        parseParameter)
    );
  }
}
