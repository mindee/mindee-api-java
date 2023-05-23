package com.mindee.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mindee.MindeeSettings;
import com.mindee.parsing.RequestParameters;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.invoice.InvoiceV4Inference;
import com.mindee.parsing.invoicesplitter.InvoiceSplitterV1Inference;
import com.mindee.utils.MindeeException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MindeeHttpApiTest extends TestCase {

  MockWebServer mockWebServer = new MockWebServer();
  private static ObjectMapper objectMapper = new ObjectMapper();

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
    Path path = Paths.get("src/test/resources/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/invoice/invoice.pdf");
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().useSystemProperties();

    MindeeHttpApi client = MindeeHttpApi.builder().mindeeSettings(new MindeeSettings("abc", url))
        .build();
    Document<InvoiceV4Inference> document = client.predict(
        InvoiceV4Inference.class,
        RequestParameters.builder()
            .file(Files.readAllBytes(file.toPath()))
            .fileName(file.getName())
            .build()).getDocument().get();

    Assertions.assertNotNull(document);

    String[] actualLines = document.toString().split(System.lineSeparator());
    String actualSummary = String.join(String.format("%n"), actualLines);
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/invoice/response_v4/summary_full.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);

    Assertions.assertNotNull(document);
    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenParseParametersWithFile_whenParsed_shouldBuildRequestCorrectly()
      throws IOException, InterruptedException {
    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get("src/test/resources/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/invoice/invoice.pdf");
    byte[] fileBytes = Files.readAllBytes(file.toPath());
    MindeeHttpApi client = MindeeHttpApi.builder().mindeeSettings(new MindeeSettings("abc", url))
        .build();
    Document<InvoiceV4Inference> document = client.predict(
        InvoiceV4Inference.class,
        RequestParameters.builder()
            .file(fileBytes)
            .fileName(file.getName())
            .build()).getDocument().get();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    Assertions.assertEquals("abc", recordedRequest.getHeader("Authorization"));
    Assertions.assertEquals(Long.toString(recordedRequest.getBodySize()),
        recordedRequest.getHeader("Content-Length"));
    Assertions.assertTrue(recordedRequest.getBodySize() > fileBytes.length);

  }

  @Test
  void givenParseParametersWithFileUrl_whenParsed_shouldBuildRequestCorrectly()
      throws IOException, InterruptedException {
    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get("src/test/resources/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path))));

    MindeeHttpApi client = MindeeHttpApi.builder().mindeeSettings(new MindeeSettings("abc", url))
        .build();
    Document<InvoiceV4Inference> document = client.predict(
        InvoiceV4Inference.class,
        RequestParameters.builder()
            .file(null)
            .fileName(null)
            .fileUrl(new URL("https://thisfile.does.not.exist"))
            .build()).getDocument().get();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    Assertions.assertEquals("abc", recordedRequest.getHeader("Authorization"));
    Assertions.assertEquals(Long.toString(recordedRequest.getBodySize()),
        recordedRequest.getHeader("Content-Length"));
    Assertions.assertTrue(recordedRequest.getHeader("Content-Type").contains("application/json"));
    Map<String, String> requestMap = objectMapper.readValue(recordedRequest.getBody().readUtf8(),
        Map.class);
    assertThat(requestMap, IsMapContaining.hasEntry("document", "https://thisfile.does.not.exist"));

  }

  @Test
  void givenAUrlBuilderFunction_whenParsed_callsTheCorrectUrl()
      throws IOException, InterruptedException {

    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    String mockPath = "/testinvoice/v2/test";
    Path path = Paths.get("src/test/resources/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/invoice/invoice.pdf");

    MindeeHttpApi client = MindeeHttpApi.builder()
        .mindeeSettings(new MindeeSettings("abc", url))
        .urlFromEndpoint(
            endpoint -> String.format("http://localhost:%s%s", mockWebServer.getPort(), mockPath))
        .build();
    Document<InvoiceV4Inference> document = client.predict(
        InvoiceV4Inference.class,
        RequestParameters.builder()
            .file(Files.readAllBytes(file.toPath()))
            .fileName(file.getName())
            .build()).getDocument().get();

    Assertions.assertNotNull(document);

    String[] actualLines = document.toString().split(System.lineSeparator());
    String actualSummary = String.join(String.format("%n"), actualLines);
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/invoice/response_v4/summary_full.rst"));
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

    Path path = Paths.get("src/test/resources/invoice/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/invoice/invoice.pdf");

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
        RequestParameters.builder()
            .file(Files.readAllBytes(file.toPath()))
            .fileName(file.getName())
            .build()).getDocument().get();

    Assertions.assertNotNull(document);

    String[] actualLines = document.toString().split(System.lineSeparator());
    String actualSummary = String.join(String.format("%n"), actualLines);
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/invoice/response_v4/summary_full.rst"));
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
      "src/test/resources/errors/complete_with_object_response_in_detail.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(400)
        .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/invoice/invoice.pdf");
    MindeeHttpApi client = MindeeHttpApi.builder().mindeeSettings(new MindeeSettings("abc", url))
        .build();
    byte[] fileInBytes = Files.readAllBytes(file.toPath());
    RequestParameters parseParameter =
        RequestParameters.builder()
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

  @Test
  void givenAResponseFromTheJobEndpoint_whenDeserialized_mustHaveValidJobAndDocument()
      throws IOException, InterruptedException {

    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get("src/test/resources/data/async/get_completed.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path))));

    MindeeHttpApi client = MindeeHttpApi.builder().mindeeSettings(new MindeeSettings("abc", url))
        .build();
    PredictResponse<InvoiceSplitterV1Inference> invoiceSplitterV1Inference = client.checkJobStatus(
        InvoiceSplitterV1Inference.class,
        "2134e243244");

    Assertions.assertNotNull(invoiceSplitterV1Inference);
    Assertions.assertEquals("completed", invoiceSplitterV1Inference.getJob().get().getStatus());
    Assertions.assertTrue(invoiceSplitterV1Inference.getDocument().isPresent());

    Assert.assertTrue(invoiceSplitterV1Inference.getJob().isPresent());
    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    Assertions.assertEquals("abc", recordedRequest.getHeader("Authorization"));
    Assertions.assertEquals("GET", recordedRequest.getMethod());
    Assertions.assertEquals("/products/Mindee/invoice_splitter/v1/documents/queue/2134e243244",
        recordedRequest.getPath());

  }


}
