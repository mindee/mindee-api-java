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
import com.mindee.PredictOptions;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Document;
import com.mindee.product.invoice.InvoiceV4;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;

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

  private MindeeHttpApi getClientForResponse(String filePath, int statusCode) throws IOException {
    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    Path path = Paths.get("src/test/resources/" + filePath);
    mockWebServer.enqueue(new MockResponse()
      .setResponseCode(statusCode)
      .setBody(new String(Files.readAllBytes(path)))
    );
    return MindeeHttpApi.builder().mindeeSettings(new MindeeSettings("abc", url)).build();
  }

  @Test
  void givenAResponseFromTheEndpoint_whenDeserialized_mustHaveValidSummary()
      throws IOException
  {
    MindeeHttpApi client = getClientForResponse("products/invoices/response_v4/complete.json", 200);

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().useSystemProperties();

    Document<InvoiceV4> document = client.predictPost(
        InvoiceV4.class,
        new Endpoint("", ""),
        RequestParameters.builder()
            .file(Files.readAllBytes(file.toPath()))
            .fileName(file.getName())
            .build())
      .getDocument();

    Assertions.assertNotNull(document);

    String[] actualLines = document.toString().split(System.lineSeparator());
    String actualSummary = String.join(String.format("%n"), actualLines);
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/products/invoices/response_v4/summary_full.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);

    Assertions.assertNotNull(document);
    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenParseParametersWithFile_whenParsed_shouldBuildRequestCorrectly()
      throws IOException, InterruptedException
  {
    MindeeHttpApi client = getClientForResponse("products/invoices/response_v4/complete.json", 200);

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    byte[] fileBytes = Files.readAllBytes(file.toPath());
    Document<InvoiceV4> document = client.predictPost(
        InvoiceV4.class,
        new Endpoint("", ""),
        RequestParameters.builder()
            .file(fileBytes)
            .fileName(file.getName())
            .build())
      .getDocument();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    Assertions.assertEquals("abc", recordedRequest.getHeader("Authorization"));
    Assertions.assertEquals(Long.toString(recordedRequest.getBodySize()),
        recordedRequest.getHeader("Content-Length"));
    Assertions.assertTrue(recordedRequest.getBodySize() > fileBytes.length);
  }

  @Test
  void givenPredictOptions_whenParsed_shouldBuildRequestCorrectly()
      throws IOException, InterruptedException
  {
    MindeeHttpApi client = getClientForResponse("products/invoices/response_v4/complete.json", 200);

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    byte[] fileBytes = Files.readAllBytes(file.toPath());
    PredictOptions predictOptions = PredictOptions.builder()
      .cropper(true)
      .allWords(true)
      .build();
    Document<InvoiceV4> document = client.predictPost(
      InvoiceV4.class,
      new Endpoint("", ""),
      RequestParameters.builder()
        .file(fileBytes)
        .fileName(file.getName())
        .predictOptions(predictOptions)
        .build())
      .getDocument();

    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    Assertions.assertEquals("abc", recordedRequest.getHeader("Authorization"));
    Assertions.assertEquals(
      Long.toString(recordedRequest.getBodySize()),
      recordedRequest.getHeader("Content-Length")
    );
    Assertions.assertTrue(recordedRequest.getRequestUrl().toString().contains("cropper=true"));
    Assertions.assertTrue(recordedRequest.getBody().readUtf8().contains("include_mvision"));
    Assertions.assertTrue(recordedRequest.getBodySize() > fileBytes.length);
  }

  @Test
  void givenParseParametersWithFileUrl_whenParsed_shouldBuildRequestCorrectly()
      throws IOException, InterruptedException
  {
    MindeeHttpApi client = getClientForResponse("products/invoices/response_v4/complete.json", 200);

    Document<InvoiceV4> document = client.predictPost(
        InvoiceV4.class,
        new Endpoint("", ""),
        RequestParameters.builder()
            .file(null)
            .fileName(null)
            .urlInputSource(new URL("https://thisfile.does.not.exist"))
            .build())
      .getDocument();

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
  void givenAnUrlBuilderFunction_whenParsed_callsTheCorrectUrl()
      throws IOException, InterruptedException
  {
    String url = String.format("http://localhost:%s", mockWebServer.getPort());
    String mockPath = "/testinvoice/v2/test";
    Path path = Paths.get("src/test/resources/products/invoices/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path)))
    );

    MindeeHttpApi client = MindeeHttpApi.builder()
        .mindeeSettings(new MindeeSettings("abc", url))
        .urlFromEndpoint(
            endpoint -> String.format("http://localhost:%s%s", mockWebServer.getPort(), mockPath))
        .build();

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    Document<InvoiceV4> document = client.predictPost(
        InvoiceV4.class,
        new Endpoint("", ""),
        RequestParameters.builder()
            .file(Files.readAllBytes(file.toPath()))
            .fileName(file.getName())
            .build())
      .getDocument();

    Assertions.assertNotNull(document);

    String[] actualLines = document.toString().split(System.lineSeparator());
    String actualSummary = String.join(String.format("%n"), actualLines);
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/products/invoices/response_v4/summary_full.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);

    Assertions.assertNotNull(document);
    Assertions.assertEquals(expectedSummary, actualSummary);
    Assertions.assertEquals(mockPath, mockWebServer.takeRequest().getPath());
  }

  @Test
  void givenAHttpClientBuilder_whenParseCalled_usesClientBuilderToMakeHttpClient()
      throws IOException
  {
    WireMockRule proxyMock = new WireMockRule();
    proxyMock.start();
    int proxyPort = proxyMock.port();

    String url = String.format("http://localhost:%s", mockWebServer.getPort());

    proxyMock.stubFor(post(urlMatching(".*"))
        .willReturn(aResponse().proxiedFrom(url)));

    Path path = Paths.get("src/test/resources/products/invoices/response_v4/complete.json");
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(new String(Files.readAllBytes(path))));

    File file = new File("src/test/resources/products/invoices/invoice.pdf");

    HttpHost proxy = new HttpHost("localhost", proxyPort);
    DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
    HttpClientBuilder httpclientBuilder = HttpClients.custom()
        .setRoutePlanner(routePlanner);

    MindeeHttpApi client = MindeeHttpApi.builder()
        .mindeeSettings(new MindeeSettings("abc", url))
        .httpClientBuilder(httpclientBuilder)
        .build();

    Document<InvoiceV4> document = client.predictPost(
        InvoiceV4.class,
        new Endpoint(InvoiceV4.class),
        RequestParameters.builder()
            .file(Files.readAllBytes(file.toPath()))
            .fileName(file.getName())
            .build())
      .getDocument();

    Assertions.assertNotNull(document);

    String[] actualLines = document.toString().split(System.lineSeparator());
    String actualSummary = String.join(String.format("%n"), actualLines);
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/products/invoices/response_v4/summary_full.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);

    proxyMock.verify(postRequestedFor(urlEqualTo("/products/mindee/invoices/v4/predict"))
        .withHeader("Authorization", containing("abc")));
    Assertions.assertNotNull(document);
    Assertions.assertEquals(expectedSummary, actualSummary);
    proxyMock.shutdown();
  }

  @Test
  void givenAnAsncResponse_whenDeserialized_mustHaveValidJob()
    throws IOException, InterruptedException
  {
    MindeeHttpApi client = getClientForResponse("async/post_success.json", 200);

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    AsyncPredictResponse<InvoiceV4> response = client.predictAsyncPost(
      InvoiceV4.class,
      new Endpoint(InvoiceV4.class),
      RequestParameters.builder()
        .file(Files.readAllBytes(file.toPath()))
        .fileName(file.getName())
        .build());

    Assertions.assertNotNull(response);
    Assertions.assertEquals("waiting", response.getJob().getStatus());
    Assertions.assertFalse(response.getDocument().isPresent());

    Assert.assertNotNull(response.getJob());
    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    Assertions.assertEquals("abc", recordedRequest.getHeader("Authorization"));
    Assertions.assertEquals("POST", recordedRequest.getMethod());
    Assertions.assertEquals("/products/mindee/invoices/v4/predict_async",
      recordedRequest.getPath());
  }

  @Test
  void givenAResponseFromTheJobEndpoint_whenDeserialized_mustHaveValidJobAndDocument()
      throws IOException, InterruptedException
  {
    MindeeHttpApi client = getClientForResponse("async/get_completed.json", 200);

    AsyncPredictResponse<InvoiceSplitterV1> invoiceSplitterV1Inference = client.documentQueueGet(
        InvoiceSplitterV1.class,
        new Endpoint(InvoiceSplitterV1.class),
        "2134e243244");

    Assertions.assertNotNull(invoiceSplitterV1Inference);
    Assertions.assertEquals("completed", invoiceSplitterV1Inference.getJob().getStatus());
    Assertions.assertTrue(invoiceSplitterV1Inference.getDocument().isPresent());

    Assert.assertNotNull(invoiceSplitterV1Inference.getJob());
    RecordedRequest recordedRequest = mockWebServer.takeRequest();

    Assertions.assertEquals("abc", recordedRequest.getHeader("Authorization"));
    Assertions.assertEquals("GET", recordedRequest.getMethod());
    Assertions.assertEquals("/products/mindee/invoice_splitter/v1/documents/queue/2134e243244",
        recordedRequest.getPath());
  }

  @Test
  void givenError401_noToken_mustThrowMindeeHttpException()
    throws IOException
  {
    MindeeHttpApi client = getClientForResponse("errors/error_401_no_token.json", 401);

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    byte[] fileInBytes = Files.readAllBytes(file.toPath());
    RequestParameters parseParameter =
      RequestParameters.builder()
        .file(fileInBytes)
        .fileName(file.getName())
        .build();

    MindeeHttpException httpError = Assertions.assertThrows(
      MindeeHttpException.class,
      () -> client.predictPost(
        InvoiceV4.class,
        new Endpoint(InvoiceV4.class),
        parseParameter)
    );
    assertEquals(401, httpError.getStatusCode());
    assertEquals("Authorization required", httpError.getMessage());
    assertEquals("No token provided", httpError.getDetails());
  }

  @Test
  void givenError429_mustThrowMindeeHttpException()
    throws IOException
  {
    MindeeHttpApi client = getClientForResponse("errors/error_429_too_many_requests.json", 429);

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    byte[] fileInBytes = Files.readAllBytes(file.toPath());
    RequestParameters parseParameter =
      RequestParameters.builder()
        .file(fileInBytes)
        .fileName(file.getName())
        .build();

    MindeeHttpException httpError = Assertions.assertThrows(
      MindeeHttpException.class,
      () -> client.predictPost(
        InvoiceV4.class,
        new Endpoint(InvoiceV4.class),
        parseParameter)
    );
    assertEquals(429, httpError.getStatusCode());
    assertEquals("Too many requests", httpError.getMessage());
    assertEquals("Too Many Requests.", httpError.getDetails());
  }

  @Test
  void givenError_inHtml_mustThrowMindeeHttpException()
    throws IOException
  {
    MindeeHttpApi client = getClientForResponse("errors/error_50x.html", 413);

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    byte[] fileInBytes = Files.readAllBytes(file.toPath());
    RequestParameters parseParameter =
      RequestParameters.builder()
        .file(fileInBytes)
        .fileName(file.getName())
        .build();

    MindeeHttpException httpError = Assertions.assertThrows(
      MindeeHttpException.class,
      () -> client.predictPost(
        InvoiceV4.class,
        new Endpoint(InvoiceV4.class),
        parseParameter)
    );
    assertEquals(413, httpError.getStatusCode());
    assertEquals("HTTP Status 413 - Unhandled server response, check details.", httpError.getMessage());
    assertTrue(httpError.getDetails().contains("<h1>An error occurred.</h1>"));
  }

  @Test
  void givenError400_noDetails_mustThrowMindeeHttpException()
    throws IOException
  {
    MindeeHttpApi client = getClientForResponse("errors/error_400_no_details.json", 400);

    File file = new File("src/test/resources/products/invoices/invoice.pdf");
    byte[] fileInBytes = Files.readAllBytes(file.toPath());
    RequestParameters parseParameter =
      RequestParameters.builder()
        .file(fileInBytes)
        .fileName(file.getName())
        .build();

    MindeeHttpException httpError = Assertions.assertThrows(
      MindeeHttpException.class,
      () -> client.predictPost(
        InvoiceV4.class,
        new Endpoint(InvoiceV4.class),
        parseParameter)
    );
    assertEquals(400, httpError.getStatusCode());
    assertEquals("Some scary message here", httpError.getMessage());
    assertEquals("", httpError.getDetails());
  }
}
