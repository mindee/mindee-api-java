package com.mindee.documentparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.documentparser.Client.DocumentClient;
import com.mindee.documentparser.PageOptions.PageOptionsOperation;
import com.mindee.http.DocumentParsingHttpClient;
import com.mindee.model.customdocument.CustomDocumentResponse;
import com.mindee.model.documenttype.FinancialDocumentResponse;
import com.mindee.model.documenttype.InvoiceV3Response;
import com.mindee.model.documenttype.PassportV1Response;
import com.mindee.model.documenttype.ReceiptV3Response;
import com.mindee.model.mappers.FinancialDocumentResponseMapper;
import com.mindee.utils.PDFUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

  private static final String INVOICE_URL = "https://api.mindee.net/v1/products/mindee/invoices/v3/predict";
  private static final String RECEIPT_URL = "https://api.mindee.net/v1/products/mindee/expense_receipts/v3/predict";
  private static final String PASSPORT_URL = "https://api.mindee.net/v1/products/mindee/passport/v1/predict";
  private static final String testApiKey = "dsceveve2345gwdc832";

  @Mock
  DocumentParsingHttpClient httpClient;

  Client client;

  ObjectMapper objectMapper = new ObjectMapper();

  private static <T> T spyLambda(final Class<T> lambdaType, final T lambda) {
    return Mockito.mock(lambdaType, AdditionalAnswers.delegatesTo(lambda));
  }

  private static String getFileChecksum(MessageDigest digest, InputStream fis) throws IOException {

    // Create byte array to read data in chunks
    byte[] byteArray = new byte[1024];
    int bytesCount = 0;

    // Read file data and update in message digest
    while ((bytesCount = fis.read(byteArray)) != -1) {
      digest.update(byteArray, 0, bytesCount);
    }
    ;

    // close the stream; We don't need it now.
    fis.close();

    // Get the hash's bytes
    byte[] bytes = digest.digest();

    // This bytes[] has bytes in decimal format;
    // Convert it to hexadecimal format
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
  }

  @BeforeEach
  void setUp() {
    client = new Client(testApiKey, httpClient);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void givenAClientWithInvoiceConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/pdf/multipage_cut-1.pdf"));
    documentClient.parse(InvoiceV3Response.class, ParseParameters.builder()
        .documentType("invoice")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(INVOICE_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithInvoiceConfigured_whenParsedWithoutParseParam_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/invoice/invoice.pdf"));
    documentClient.parse(InvoiceV3Response.class);

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(INVOICE_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithReceiptConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {

    Map receiptMap = objectMapper.readValue(new File("src/test/resources/data/receipt/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(receiptMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/pdf/multipage_cut-3.pdf"));
    documentClient.parse(ReceiptV3Response.class, ParseParameters.builder()
        .documentType("receipt")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(RECEIPT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithReceiptConfigured_whenParsedWithoutParseParam_thenShouldCallTheHttpClientCorrectly()
      throws IOException {

    Map receiptMap = objectMapper.readValue(new File("src/test/resources/data/receipt/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(receiptMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/receipt/receipt.jpg"));
    documentClient.parse(ReceiptV3Response.class);

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(RECEIPT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithPassportConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {

    Map passportMap = objectMapper.readValue(new File("src/test/resources/data/passport/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/passport/passport.jpeg"));
    documentClient.parse(PassportV1Response.class, ParseParameters.builder()
        .documentType("passport")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(PASSPORT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithPassportConfigured_whenParsedWithoutParseParam_thenShouldCallTheHttpClientCorrectly()
      throws IOException {

    Map passportMap = objectMapper.readValue(new File("src/test/resources/data/passport/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/passport/passport.jpeg"));
    documentClient.parse(PassportV1Response.class);

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(PASSPORT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithMultipleOffTheShelfConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {

    Map passportMap = objectMapper.readValue(new File("src/test/resources/data/passport/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/passport/passport.jpeg"));
    documentClient.parse(PassportV1Response.class, ParseParameters.builder()
        .documentType("passport")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(PASSPORT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithMissingConfigured_whenParsed_thenShouldThrowException()
      throws IOException {

    try (MockedStatic<DocumentConfigFactory> utilities = Mockito.mockStatic(
        DocumentConfigFactory.class)) {
      utilities.when(() -> DocumentConfigFactory.getApiKeyFromEnvironmentVariable())
          .thenReturn(null);

      Exception exception = Assertions.assertThrows(RuntimeException.class,
          () -> new Client(httpClient));

      Assert.assertTrue(exception.getMessage().toLowerCase().contains("api key"));

    }

  }

  @Test
  void givenAClientthatUsesEnvKey_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    Client testClientWithoutApiKey = null;
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);

    DocumentConfig config = DocumentConfigFactory.getDocumentConfigForOffTheShelfDocType(
      InvoiceV3Response.class,
      "mockapikeyfromenv");
    try (MockedStatic<DocumentConfigFactory> utilities = Mockito.mockStatic(
        DocumentConfigFactory.class)) {
      utilities.when(() -> DocumentConfigFactory.getApiKeyFromEnvironmentVariable())
          .thenReturn("mockapikeyfromenv");
      utilities.when(
          () -> DocumentConfigFactory.offTheShelfResponseTypes())
        .thenReturn(Arrays.asList(InvoiceV3Response.class));
      utilities.when(
          () -> DocumentConfigFactory.getDocumentConfigForOffTheShelfDocType(InvoiceV3Response.class,
              "mockapikeyfromenv"))
          .thenReturn(config);

      testClientWithoutApiKey = new Client(httpClient);
      DocumentClient documentClient = testClientWithoutApiKey.loadDocument(
          new File("src/test/resources/data/invoice/invoice.pdf"));
      documentClient.parse(InvoiceV3Response.class, ParseParameters.builder()
          .documentType("invoice")
          .build());

      ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
      Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
          Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

      Assert.assertEquals("mockapikeyfromenv", apiKeyCaptor.getAllValues().get(0));
      Assert.assertEquals(INVOICE_URL, endpointCaptor.getAllValues().get(0));

    }

  }

  @Test
  void givenAClientWithFinDocConfigured_whenPdfParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {

    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/invoice/invoice.pdf"));
    documentClient.parse(FinancialDocumentResponse.class,
        ParseParameters.builder()
            .documentType("financial_doc")
            .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(INVOICE_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithFinDocConfigured_whenJpegParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {

    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/receipt/receipt.jpg"));
    documentClient.parse(FinancialDocumentResponse.class, ParseParameters.builder()
        .documentType("financial_doc")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(RECEIPT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  public void givenACustomDocumentConfigured_whenParsed_ThenCallsClientCorrectly()
      throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/custom/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);

    CustomDocumentResponse bill = client.loadDocument(
        new File("src/test/resources/data/invoice/invoice.pdf"))
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .accountName("testaccount")
            .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(
        "https://api.mindee.net/v1/products/testaccount/bill_of_lading_line_items/v1/predict",
        endpointCaptor.getAllValues().get(0));

  }

  @Test
  public void givenMultipleCustomDocumentConfigured_whenParsed_ThenCallsClientCorrectly()
      throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/custom/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);

    CustomDocumentResponse bill = client.loadDocument(
        new File("src/test/resources/data/pdf/multipage_cut-3.pdf"))
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .accountName("testaccount2")
            .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture(), Mockito.any());

    Assert.assertEquals(testApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(
        "https://api.mindee.net/v1/products/testaccount2/bill_of_lading_line_items/v1/predict",
        endpointCaptor.getAllValues().get(0));

  }

  @Test
  public void givenMultipleCustomDocumentConfigured_whenParsedWithoutAccountName_thenThrowsException()
      throws IOException {

    Exception exception = Assertions.assertThrows(RuntimeException.class,
        () -> client.loadDocument(new File("src/test/resources/invoicetest.pdf"))
            .parse(CustomDocumentResponse.class, ParseParameters.builder()
                .documentType("bill_of_lading_line_items")
                .build()));

    Assert.assertTrue(exception.getMessage().toLowerCase().contains("bill_of_lading_line_items"));

  }

  @Test
  public void givenNoCustomDocumentConfigured_whenParsed_thenThrowsException() throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/receipt/response_v3/complete.json"),
        Map.class);

    String ladingKey1 = "dwefewf";
    String ladingKey2 = "rfbewsgfeurfewf";

    Exception exception = Assertions.assertThrows(RuntimeException.class,
        () -> client.loadDocument(new File("src/test/resources/data/pdf/not_blank_image_only.pdf"))
            .parse(CustomDocumentResponse.class, ParseParameters.builder()
                .documentType("bill_of_lading_line_items")
                .build()));

    Assert.assertTrue(exception.getMessage().toLowerCase().contains("bill_of_lading_line_items"));

  }

  @Test
  public void givenAConfiguredClient_whenFileParsed_ThenCallsHttpClientWithCorrrectDocument()
      throws IOException, NoSuchAlgorithmException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/custom/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    String ladingKey1 = "dwefewf";

    CustomDocumentResponse bill = client.loadDocument(
        new File("src/test/resources/data/invoice/invoice.pdf"))
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .accountName("testaccount")
            .build());

    ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(inputStreamCaptor.capture(),
        fileNameCaptor.capture(), Mockito.any(), Mockito.any(), Mockito.any());

    Assert.assertEquals("invoice.pdf", fileNameCaptor.getAllValues().get(0));
    InputStream actualStream = inputStreamCaptor.getAllValues().get(0);
    InputStream expectedStream = new FileInputStream(
        new File("src/test/resources/data/invoice/invoice.pdf"));

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    String actualHash = getFileChecksum(digest, actualStream);
    String expectedHash = getFileChecksum(digest, expectedStream);
    Assert.assertEquals(expectedHash, actualHash);

  }

  @Test
  public void givenAConfiguredClient_whenBase64Parsed_ThenCallsHttpClientWithCorrrectDocument()
      throws IOException, NoSuchAlgorithmException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    String ladingKey1 = "dwefewf";

    String base64File = Base64.getEncoder()
        .encodeToString(Files.readAllBytes(Paths.get("src/test/resources/data/invoice/invoice.pdf")));

    ReceiptV3Response bill = client.loadDocument(base64File, "random.pdf")
        .parse(ReceiptV3Response.class);

    ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(inputStreamCaptor.capture(),
        fileNameCaptor.capture(), Mockito.any(), Mockito.any(), Mockito.any());

    Assert.assertEquals("random.pdf", fileNameCaptor.getAllValues().get(0));
    InputStream actualStream = inputStreamCaptor.getAllValues().get(0);
    InputStream expectedStream = new FileInputStream(
        new File("src/test/resources/data/invoice/invoice.pdf"));

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    String actualHash = getFileChecksum(digest, actualStream);
    String expectedHash = getFileChecksum(digest, expectedStream);
    Assert.assertEquals(expectedHash, actualHash);

  }

  @Test
  public void givenAConfiguredClient_whenByteArrayParsed_ThenCallsHttpClientWithCorrrectDocument()
      throws IOException, NoSuchAlgorithmException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    String ladingKey1 = "dwefewf";

    byte[] inputBytes = "A string that doesn't really matter".getBytes();

    ReceiptV3Response bill = client.loadDocument(inputBytes, "randomdoc.png")
        .parse(ReceiptV3Response.class);

    ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(inputStreamCaptor.capture(),
        fileNameCaptor.capture(), Mockito.any(), Mockito.any(), Mockito.any());

    Assert.assertEquals("randomdoc.png", fileNameCaptor.getAllValues().get(0));
    InputStream actualStream = inputStreamCaptor.getAllValues().get(0);

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    String actualHash = getFileChecksum(digest, actualStream);
    String expectedHash = getFileChecksum(digest, new ByteArrayInputStream(inputBytes));
    Assert.assertEquals(expectedHash, actualHash);

  }

  @Test
  void givenAClientParsingAReceipt_whenAMapReturnedFromHttpClient_thenReturnsCorrectReceipt()
      throws IOException {
    String receiptApiKey = "1232CGDFD843G32";
    Map receiptMap = objectMapper.readValue(new File("src/test/resources/data/receipt/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(receiptMap);

    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/pdf/multipage.pdf"));
    ReceiptV3Response receiptResponse = documentClient.parse(ReceiptV3Response.class,
        ParseParameters.builder()
            .documentType("receipt")
            .build());


    Assert.assertNotNull( receiptResponse);

  }

  @Test
  void givenAClientParsingAPassport_whenAMapReturnedFromHttpClient_thenReturnsCorrectPassport()
      throws IOException {
    String passportApiKey = "1232CGDFD843G32";
    Map passportMap = objectMapper.readValue(new File("src/test/resources/data/passport/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);

    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/pdf/multipage_cut-1.pdf"));
    PassportV1Response passportV1Response = documentClient.parse(PassportV1Response.class,
        ParseParameters.builder()
            .documentType("passport")
            .build());


    Assert.assertNotNull(passportV1Response);

  }

  @Test
  void givenAClientParsingAPdfFinDoc_whenAMapReturnedFromHttpClient_thenReturnsCorrectFinDoc()
      throws IOException {
    String receiptApiKey = "1232CGDFD843G32";
    String invoiceApiKey = "gvsrdtbgrgbrtbt";
    Map finDocMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(finDocMap);

    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/invoice/invoice.pdf"));
    FinancialDocumentResponse finDocResponse = documentClient.parse(FinancialDocumentResponse.class,
        ParseParameters.builder()
            .documentType("financial_doc")
            .build());

    FinancialDocumentResponse expectedfinDocResponse = objectMapper.convertValue(finDocMap,
        FinancialDocumentResponse.class);
    Assert.assertEquals(expectedfinDocResponse, finDocResponse);

  }

  @Test
  void givenAConfiguredClient_whenParsedWithAPostProcessor_thenCallsThePostProcessor()
      throws IOException {
    String passportApiKey = "1232CGDFD843G32";
    UnaryOperator<PassportV1Response> operator = spyLambda(UnaryOperator.class, x -> x);
    Map passportMap = objectMapper.readValue(new File("src/test/resources/data/passport/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);

    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/passport/passport.jpeg"));
    PassportV1Response passportV1Response = documentClient.parse(PassportV1Response.class,
        ParseParameters.builder()
            .documentType("passport")
            .build(),
        operator);

    Mockito.verify(operator, Mockito.times(1)).apply(Mockito.any(PassportV1Response.class));

  }

  @Test
  void givenAConfiguredClient_whenPostProcessorReturnsAResult_thenReturnsCorrectDocument()
      throws IOException {
    String passportApiKey = "1232CGDFD843G32";
    UnaryOperator<PassportV1Response> operator = x -> {
      x.setType("dweugwfw63");
      return x;
    };
    Map passportMap = objectMapper.readValue(new File("src/test/resources/data/passport/response_v1/complete.json"),
        Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);

    DocumentClient documentClient = client.loadDocument(
        new File("src/test/resources/data/pdf/multipage.pdf"));
    PassportV1Response passportV1Response = documentClient.parse(PassportV1Response.class,
        ParseParameters.builder()
            .documentType("passport")
            .build(),
        operator);


    Assert.assertEquals("dweugwfw63", passportV1Response.getType());

  }

  @Test
  public void givenAConfiguredClient_whenPdfFileParsedWithDocManipulationRemove_ThenCallsHttpClientWithCorrrectDocument()
    throws IOException, NoSuchAlgorithmException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
      Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
      .thenReturn(invoiceMap);

    List<Integer> pageList = Arrays.asList(3,4,5,6,7,8);
    File file = new File("src/test/resources/data/invoice/invoice_10p.pdf");
    byte[] bytesAfterRemoval = PDFUtils.mergePdfPages(file,pageList);

    int pageCount = PDFUtils.countPdfPages(bytesAfterRemoval);
    Assert.assertEquals(6, pageCount);

    try (MockedStatic<PDFUtils> utilities = Mockito.mockStatic(
      PDFUtils.class)) {
      utilities.when(() -> PDFUtils.countPdfPages(file))
        .thenReturn(10);
      utilities.when(
          () -> PDFUtils.mergePdfPages(file,pageList))
        .thenReturn(bytesAfterRemoval);

      InvoiceV3Response bill = client.loadDocument(
          new File("src/test/resources/data/invoice/invoice_10p.pdf"))
        .parse(InvoiceV3Response.class, ParseParameters.builder()
          .pageOptions(PageOptions.builder()
            .onMinPages(5)
            .mode(PageOptionsOperation.REMOVE_LISTED_PAGES)
            .pages(Arrays.asList(0,-1,1,2))
            .build())
          .build());

      ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
      ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
      Mockito.verify(httpClient, Mockito.atLeast(1)).parse(inputStreamCaptor.capture(),
        fileNameCaptor.capture(), Mockito.any(), Mockito.any(), Mockito.any());

      Assert.assertEquals("invoice_10p.pdf", fileNameCaptor.getAllValues().get(0));
      InputStream actualStream = inputStreamCaptor.getAllValues().get(0);
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      String actualHash = getFileChecksum(digest, actualStream);
      String expectedHash = getFileChecksum(digest, new ByteArrayInputStream(bytesAfterRemoval));
      Assert.assertNotEquals(expectedHash, actualHash);
    }




  }

  @Test
  public void givenAConfiguredClient_whenPdfFileParsedWithDocManipulationKeep_ThenCallsHttpClientWithCorrrectDocument()
    throws IOException, NoSuchAlgorithmException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
      Map.class);
    Mockito.when(
        httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
      .thenReturn(invoiceMap);


    InvoiceV3Response bill = client.loadDocument(
        new File("src/test/resources/data/pdf/multipage_cut-3.pdf"))
      .parse(InvoiceV3Response.class, ParseParameters.builder()
        .pageOptions(PageOptions.builder()
          .onMinPages(1)
          .mode(PageOptionsOperation.KEEP_ONLY_LISTED_PAGES)
          .pages(Arrays.asList(0,1,-1))
          .build())
        .build());

    ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(inputStreamCaptor.capture(),
      fileNameCaptor.capture(), Mockito.any(), Mockito.any(), Mockito.any());

    Assert.assertEquals("multipage_cut-3.pdf", fileNameCaptor.getAllValues().get(0));
    InputStream actualStream = inputStreamCaptor.getAllValues().get(0);
    InputStream expectedStream = new FileInputStream(
      new File("src/test/resources/data/pdf/multipage_cut-3.pdf"));

    int pageCount = PDFUtils.countPdfPages(actualStream);
    Assert.assertEquals(3, pageCount);

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    String actualHash = getFileChecksum(digest, actualStream);
    String expectedHash = getFileChecksum(digest, expectedStream);
     Assert.assertNotEquals(expectedHash, actualHash);


  }


  public void testgivenAReceiptDocumentPath_whenParsed_ThenReturnsReceipt() throws IOException {
    Client client = new Client("");

    ReceiptV3Response receiptResponse = client.loadDocument(
        new File("src/test/resources/receipttest.jpeg"))
        .parse(ReceiptV3Response.class, ParseParameters.builder()
            .documentType("receipt")
            .build());

    receiptResponse.getPages();
    receiptResponse.getDocument();

    Assert.assertNotNull(receiptResponse);

    InvoiceV3Response bill = client.loadDocument(
        new File("src/test/resources/data/invoice/invoice_10p.pdf"))
      .parse(InvoiceV3Response.class, ParseParameters.builder()
        .pageOptions(PageOptions.builder()
          .onMinPages(5)
          .mode(PageOptionsOperation.KEEP_ONLY_LISTED_PAGES)
          .pages(Arrays.asList(0,-1,1,2))
          .build())
        .build());


  }

  public void testgivenAnInvoiceDocumentPath_whenParsed_ThenReturnsReceipt() throws IOException {
    Client client = new Client();

    InvoiceV3Response invoiceV3Response = client.loadDocument(
        new File("src/test/resources/invoicetest.pdf"))
        .parse(InvoiceV3Response.class, ParseParameters.builder()
            .documentType("invoice")
            .build());

    Assert.assertNotNull(invoiceV3Response);

    FinancialDocumentResponse response = FinancialDocumentResponseMapper.INSTANCE
        .invoiceResponseToFinancialDocumentResponse(
          invoiceV3Response);

    Assert.assertNotNull(response);

  }

}
