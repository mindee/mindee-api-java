package com.mindee.documentparser;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.documentparser.Client.DocumentClient;
import com.mindee.http.DocumentParsingHttpClient;
import com.mindee.model.documenttype.CustomDocumentResponse;
import com.mindee.model.documenttype.FinancialDocumentResponse;
import com.mindee.model.documenttype.InvoiceResponse;
import com.mindee.model.documenttype.PassportResponse;
import com.mindee.model.documenttype.PassportResponse.PassportDocument;
import com.mindee.model.documenttype.PassportResponse.PassportPage;
import com.mindee.model.documenttype.ReceiptResponse;
import com.mindee.model.fields.Field;
import com.mindee.model.mappers.FinancialDocumentResponseMapper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

  private static final String INVOICE_URL = "https://api.mindee.net/v1/products/mindee/invoices/v3/predict";
  private static final String RECEIPT_URL = "https://api.mindee.net/v1/products/mindee/expense_receipts/v3/predict";
  private static final String PASSPORT_URL = "https://api.mindee.net/v1/products/mindee/passport/v1/predict";

  @Mock
  DocumentParsingHttpClient httpClient;

  Client client;

  ObjectMapper objectMapper = new ObjectMapper();

  private static <T> T spyLambda(final Class<T> lambdaType, final T lambda) {
    return Mockito.mock(lambdaType, AdditionalAnswers.delegatesTo(lambda));
  }

  private static String getFileChecksum(MessageDigest digest, InputStream fis) throws IOException {

    //Create byte array to read data in chunks
    byte[] byteArray = new byte[1024];
    int bytesCount = 0;

    //Read file data and update in message digest
    while ((bytesCount = fis.read(byteArray)) != -1) {
      digest.update(byteArray, 0, bytesCount);
    }
    ;

    //close the stream; We don't need it now.
    fis.close();

    //Get the hash's bytes
    byte[] bytes = digest.digest();

    //This bytes[] has bytes in decimal format;
    //Convert it to hexadecimal format
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    //return complete hash
    return sb.toString();
  }

  @BeforeEach
  void setUp() {
    client = new Client(httpClient);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void givenAClientWithInvoiceConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    String invoiceApiKey = "1232CGDFD843G32";
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/invoiceResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    client.configureInvoice(invoiceApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    documentClient.parse(InvoiceResponse.class, ParseParameters.builder()
        .documentType("invoice")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture());

    Assert.assertEquals(invoiceApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(INVOICE_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithReceiptConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    String receiptApiKey = "1232CGDFD843G32";
    Map receiptMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(receiptMap);
    client.configureReceipt(receiptApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    documentClient.parse(ReceiptResponse.class, ParseParameters.builder()
        .documentType("receipt")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture());

    Assert.assertEquals(receiptApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(RECEIPT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithPassportConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    String passportApiKey = "1232CGDFD843G32";
    Map passportMap = objectMapper.readValue(new File("src/test/resources/passportResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);
    client.configurePassport(passportApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    documentClient.parse(PassportResponse.class, ParseParameters.builder()
        .documentType("passport")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture());

    Assert.assertEquals(passportApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(PASSPORT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithMultipleOffTheShelfConfigured_whenParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    String passportApiKey = "1232CGDFD843G32";
    String receiptApiKey = "feury2354";
    String invoiceApiKey = "fae9g94rfcwef";
    Map passportMap = objectMapper.readValue(new File("src/test/resources/passportResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);
    client.configurePassport(passportApiKey);
    client.configureReceipt(receiptApiKey);
    client.configureInvoice(invoiceApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    documentClient.parse(PassportResponse.class, ParseParameters.builder()
        .documentType("passport")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture());

    Assert.assertEquals(passportApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(PASSPORT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithMissingConfigured_whenParsed_thenShouldThrowException()
      throws IOException {
    String passportApiKey = "1232CGDFD843G32";
    String receiptApiKey = "feury2354";
    String invoiceApiKey = "fae9g94rfcwef";
    Map passportMap = objectMapper.readValue(new File("src/test/resources/passportResponse.json"),
        Map.class);
    client.configureReceipt(receiptApiKey);
    client.configureInvoice(invoiceApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    Exception exception = Assertions.assertThrows(RuntimeException.class,
        () -> documentClient.parse(PassportResponse.class, ParseParameters.builder()
            .documentType("passport")
            .build()));

    Assert.assertTrue(exception.getMessage().toLowerCase().contains("passport"));
  }

  @Test
  void givenAClientWithFinDocConfigured_whenPdfParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    String fdInvoiceApiKei = "1232CGDFD843G32";
    String invoiceApiKei = "dwefewf";
    String fdReceiptApiKey = "ferceajhvheverv";
    String receiptApiKey = "234rfearjdfv";
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/invoiceResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    client.configureFinancialDoc(fdInvoiceApiKei, fdReceiptApiKey);
    client.configureInvoice(invoiceApiKei);
    client.configureReceipt(receiptApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    documentClient.parse(FinancialDocumentResponse.class,
        ParseParameters.builder()
            .documentType("financial_doc")
            .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture());

    Assert.assertEquals(fdInvoiceApiKei, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(INVOICE_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  void givenAClientWithFinDocConfigured_whenJpegParsed_thenShouldCallTheHttpClientCorrectly()
      throws IOException {
    String fdInvoiceApiKei = "1232CGDFD843G32";
    String invoiceApiKei = "dwefewf";
    String fdReceiptApiKey = "ferceajhvheverv";
    String receiptApiKey = "234rfearjdfv";
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    client.configureFinancialDoc(fdInvoiceApiKei, fdReceiptApiKey);
    client.configureInvoice(invoiceApiKei);
    client.configureReceipt(receiptApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/receipttest.jpeg"));
    documentClient.parse(FinancialDocumentResponse.class, ParseParameters.builder()
        .documentType("financial_doc")
        .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture());

    Assert.assertEquals(fdReceiptApiKey, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(RECEIPT_URL, endpointCaptor.getAllValues().get(0));
  }

  @Test
  public void givenACustomDocumentConfigured_whenParsed_ThenCallsClientCorrectly()
      throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    String ladingKey1 = "dwefewf";
    String ladingKey2 = "rfbewsgfeurfewf";
    client.configureCustomDocument("bill_of_lading_line_items",
        "lading", "ladings", "testaccount1",
        ladingKey1, "1");

    CustomDocumentResponse bill = client.setDocument(new File("src/test/resources/invoicetest.pdf"))
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture());

    Assert.assertEquals(ladingKey1, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(
        "https://api.mindee.net/v1/products/testaccount1/bill_of_lading_line_items/v1/predict",
        endpointCaptor.getAllValues().get(0));

  }

  @Test
  public void givenMultipleCustomDocumentConfigured_whenParsed_ThenCallsClientCorrectly()
      throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    String ladingKey1 = "dwefewf";
    String ladingKey2 = "rfbewsgfeurfewf";
    client.configureCustomDocument("bill_of_lading_line_items",
        "lading", "ladings", "testaccount1",
        ladingKey1, "1");

    client.configureCustomDocument("bill_of_lading_line_items",
        "lading", "ladings", "testaccount2",
        ladingKey2, "1");

    CustomDocumentResponse bill = client.setDocument(new File("src/test/resources/invoicetest.pdf"))
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .accountName("testaccount2")
            .build());

    ArgumentCaptor<String> apiKeyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(Mockito.any(),
        Mockito.any(), apiKeyCaptor.capture(), endpointCaptor.capture());

    Assert.assertEquals(ladingKey2, apiKeyCaptor.getAllValues().get(0));
    Assert.assertEquals(
        "https://api.mindee.net/v1/products/testaccount2/bill_of_lading_line_items/v1/predict",
        endpointCaptor.getAllValues().get(0));


  }

  @Test
  public void givenMultipleCustomDocumentConfigured_whenParsedWithoutAccountName_thenThrowsException()
      throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);

    String ladingKey1 = "dwefewf";
    String ladingKey2 = "rfbewsgfeurfewf";
    client.configureCustomDocument("bill_of_lading_line_items",
        "lading", "ladings", "testaccount1",
        ladingKey1, "1");

    client.configureCustomDocument("bill_of_lading_line_items",
        "lading", "ladings", "testaccount2",
        ladingKey2, "1");

    Exception exception = Assertions.assertThrows(RuntimeException.class,
        () -> client.setDocument(new File("src/test/resources/invoicetest.pdf"))
            .parse(CustomDocumentResponse.class, ParseParameters.builder()
                .documentType("bill_of_lading_line_items")
                .build()));

    Assert.assertTrue(exception.getMessage().toLowerCase().contains("bill_of_lading_line_items"));
    Assert.assertTrue(exception.getMessage().toLowerCase().contains("testaccount1"));
    Assert.assertTrue(exception.getMessage().toLowerCase().contains("testaccount2"));

  }

  @Test
  public void givenNoCustomDocumentConfigured_whenParsed_thenThrowsException() throws IOException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);

    String ladingKey1 = "dwefewf";
    String ladingKey2 = "rfbewsgfeurfewf";

    Exception exception = Assertions.assertThrows(RuntimeException.class,
        () -> client.setDocument(new File("src/test/resources/invoicetest.pdf"))
            .parse(CustomDocumentResponse.class, ParseParameters.builder()
                .documentType("bill_of_lading_line_items")
                .build()));

    Assert.assertTrue(exception.getMessage().toLowerCase().contains("bill_of_lading_line_items"));


  }

  @Test
  public void givenAConfiguredClient_whenFileParsed_ThenCallsHttpClientWithCorrrectDocument()
      throws IOException, NoSuchAlgorithmException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    String ladingKey1 = "dwefewf";
    client.configureCustomDocument("bill_of_lading_line_items",
        "lading", "ladings", "testaccount1",
        ladingKey1, "1");

    CustomDocumentResponse bill = client.setDocument(new File("src/test/resources/invoicetest.pdf"))
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .build());

    ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(inputStreamCaptor.capture(),
        fileNameCaptor.capture(), Mockito.any(), Mockito.any());

    Assert.assertEquals("invoicetest.pdf", fileNameCaptor.getAllValues().get(0));
    InputStream actualStream = inputStreamCaptor.getAllValues().get(0);
    InputStream expectedStream = new FileInputStream(
        new File("src/test/resources/invoicetest.pdf"));

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    String actualHash = getFileChecksum(digest, actualStream);
    String expectedHash = getFileChecksum(digest, expectedStream);
    Assert.assertEquals(expectedHash, actualHash);

  }

  @Test
  public void givenAConfiguredClient_whenBase64Parsed_ThenCallsHttpClientWithCorrrectDocument()
      throws IOException, NoSuchAlgorithmException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    String ladingKey1 = "dwefewf";
    client.configureCustomDocument("bill_of_lading_line_items",
        "lading", "ladings", "testaccount1",
        ladingKey1, "1");

    String base64File = Base64.getEncoder()
        .encodeToString(Files.readAllBytes(Paths.get("src/test/resources/invoicetest.pdf")));

    CustomDocumentResponse bill = client.setDocument(base64File, "random.pdf")
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .build());

    ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(inputStreamCaptor.capture(),
        fileNameCaptor.capture(), Mockito.any(), Mockito.any());

    Assert.assertEquals("random.pdf", fileNameCaptor.getAllValues().get(0));
    InputStream actualStream = inputStreamCaptor.getAllValues().get(0);
    InputStream expectedStream = new FileInputStream(
        new File("src/test/resources/invoicetest.pdf"));

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    String actualHash = getFileChecksum(digest, actualStream);
    String expectedHash = getFileChecksum(digest, expectedStream);
    Assert.assertEquals(expectedHash, actualHash);

  }

  @Test
  public void givenAConfiguredClient_whenByteArrayParsed_ThenCallsHttpClientWithCorrrectDocument()
      throws IOException, NoSuchAlgorithmException {
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    String ladingKey1 = "dwefewf";

    byte[] inputBytes = "A string that doesn't really matter".getBytes();
    client.configureCustomDocument("bill_of_lading_line_items",
        "lading", "ladings", "testaccount1",
        ladingKey1, "1");

    CustomDocumentResponse bill = client.setDocument(inputBytes, "randomdoc.png")
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .build());

    ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(httpClient, Mockito.atLeast(1)).parse(inputStreamCaptor.capture(),
        fileNameCaptor.capture(), Mockito.any(), Mockito.any());

    Assert.assertEquals("randomdoc.png", fileNameCaptor.getAllValues().get(0));
    InputStream actualStream = inputStreamCaptor.getAllValues().get(0);

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    String actualHash = getFileChecksum(digest, actualStream);
    String expectedHash = getFileChecksum(digest, new ByteArrayInputStream(inputBytes));
    Assert.assertEquals(expectedHash, actualHash);

  }

  @Test
  void givenAClientParsingAnInvoice_whenAMapReturnedFromHttpClient_thenReturnsCorrectInvoice()
      throws IOException {
    String invoiceApiKey = "1232CGDFD843G32";
    Map invoiceMap = objectMapper.readValue(new File("src/test/resources/invoiceResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(invoiceMap);
    client.configureInvoice(invoiceApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    InvoiceResponse invoiceResponse = documentClient.parse(InvoiceResponse.class,
        ParseParameters.builder()
            .documentType("invoice")
            .build());

    InvoiceResponse expectedInvoiceResponse = objectMapper.convertValue(invoiceMap,
        InvoiceResponse.class);
    Assert.assertEquals(expectedInvoiceResponse, invoiceResponse);

  }

  @Test
  void givenAClientParsingAReceipt_whenAMapReturnedFromHttpClient_thenReturnsCorrectReceipt()
      throws IOException {
    String receiptApiKey = "1232CGDFD843G32";
    Map receiptMap = objectMapper.readValue(new File("src/test/resources/receiptResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(receiptMap);
    client.configureReceipt(receiptApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    ReceiptResponse receiptResponse = documentClient.parse(ReceiptResponse.class,
        ParseParameters.builder()
            .documentType("receipt")
            .build());

    ReceiptResponse expectedReceiptResponse = objectMapper.convertValue(receiptMap,
        ReceiptResponse.class);
    expectedReceiptResponse.setType("receipt");
    Assert.assertEquals(expectedReceiptResponse, receiptResponse);

  }

  @Test
  void givenAClientParsingAPassport_whenAMapReturnedFromHttpClient_thenReturnsCorrectPassport()
      throws IOException {
    String passportApiKey = "1232CGDFD843G32";
    Map passportMap = objectMapper.readValue(new File("src/test/resources/passportResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);
    client.configurePassport(passportApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    PassportResponse passportResponse = documentClient.parse(PassportResponse.class,
        ParseParameters.builder()
            .documentType("passport")
            .build());

    PassportResponse expectedPassportResponse = objectMapper.convertValue(passportMap,
        PassportResponse.class);

    PassportDocument passportDocument = expectedPassportResponse.getPassport();
    PassportDocument.PassportDocumentBuilder docBuilder = passportDocument.toBuilder();
    passportDocument = docBuilder.mrz(Field.builder()
            .confidence(
                passportDocument.getMrz1().getConfidence() * passportDocument.getMrz2().getConfidence())
            .reconstructed(Boolean.TRUE)
            .rawValue(passportDocument.getMrz1().getValue() + passportDocument.getMrz2().getValue())
            .value(passportDocument.getMrz1().getValue() + passportDocument.getMrz2().getValue())
            .build())
        .fullName(Field.builder()
            .reconstructed(Boolean.TRUE)
            .confidence(passportDocument.getGivenNames().get(0).getConfidence()
                * passportDocument.getSurname().getConfidence())
            .value(passportDocument.getGivenNames().get(0).getValue() + " "
                + passportDocument.getSurname().getValue())
            .rawValue(passportDocument.getGivenNames().get(0).getValue() + " "
                + passportDocument.getSurname().getValue())
            .build())
        .build();
    expectedPassportResponse.setPassport(passportDocument);

    PassportPage passportPage = expectedPassportResponse.getPassports().get(0);
    PassportPage.PassportPageBuilder passportPageBuilder = expectedPassportResponse.getPassports()
        .get(0).toBuilder();
    passportPage = passportPageBuilder.mrz(Field.builder()
            .confidence(passportPage.getMrz1().getConfidence() * passportPage.getMrz2().getConfidence())
            .reconstructed(Boolean.TRUE)
            .rawValue(passportPage.getMrz1().getValue() + passportPage.getMrz2().getValue())
            .value(passportPage.getMrz1().getValue() + passportPage.getMrz2().getValue())
            .build())
        .fullName(Field.builder()
            .reconstructed(Boolean.TRUE)
            .confidence(passportPage.getGivenNames().get(0).getConfidence()
                * passportPage.getSurname().getConfidence())
            .value(passportPage.getGivenNames().get(0).getValue() + " "
                + passportPage.getSurname().getValue())
            .rawValue(passportPage.getGivenNames().get(0).getValue() + " "
                + passportPage.getSurname().getValue())
            .build())
        .build();

    expectedPassportResponse.setPassports(Arrays.asList(passportPage));
    Assert.assertEquals(expectedPassportResponse, passportResponse);

  }

  @Test
  void givenAClientParsingAPdfFinDoc_whenAMapReturnedFromHttpClient_thenReturnsCorrectFinDoc()
      throws IOException {
    String receiptApiKey = "1232CGDFD843G32";
    String invoiceApiKey = "gvsrdtbgrgbrtbt";
    Map finDocMap = objectMapper.readValue(new File("src/test/resources/invoiceResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(finDocMap);
    client.configureFinancialDoc(invoiceApiKey, receiptApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
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
    UnaryOperator<PassportResponse> operator = spyLambda(UnaryOperator.class, x -> x);
    Map passportMap = objectMapper.readValue(new File("src/test/resources/passportResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);

    client.configurePassport(passportApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    PassportResponse passportResponse = documentClient.parse(PassportResponse.class,
        ParseParameters.builder()
            .documentType("passport")
            .build(), operator);

    Mockito.verify(operator, Mockito.times(1)).apply(Mockito.any(PassportResponse.class));

  }

  @Test
  void givenAConfiguredClient_whenPostProcessorReturnsAResult_thenReturnsCorrectDocument()
      throws IOException {
    String passportApiKey = "1232CGDFD843G32";
    UnaryOperator<PassportResponse> operator = x -> {
      x.setType("dweugwfw63");
      return x;
    };
    Map passportMap = objectMapper.readValue(new File("src/test/resources/passportResponse.json"),
        Map.class);
    Mockito.when(httpClient.parse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(passportMap);

    client.configurePassport(passportApiKey);
    DocumentClient documentClient = client.setDocument(
        new File("src/test/resources/invoicetest.pdf"));
    PassportResponse passportResponse = documentClient.parse(PassportResponse.class,
        ParseParameters.builder()
            .documentType("passport")
            .build(), operator);

    PassportResponse expectedPassportResponse = objectMapper.convertValue(passportMap,
        PassportResponse.class);
    expectedPassportResponse.setType("dweugwfw63");
    PassportDocument passportDocument = expectedPassportResponse.getPassport();
    PassportDocument.PassportDocumentBuilder docBuilder = passportDocument.toBuilder();
    passportDocument = docBuilder.mrz(Field.builder()
            .confidence(
                passportDocument.getMrz1().getConfidence() * passportDocument.getMrz2().getConfidence())
            .reconstructed(Boolean.TRUE)
            .rawValue(passportDocument.getMrz1().getValue() + passportDocument.getMrz2().getValue())
            .value(passportDocument.getMrz1().getValue() + passportDocument.getMrz2().getValue())
            .build())
        .fullName(Field.builder()
            .reconstructed(Boolean.TRUE)
            .confidence(passportDocument.getGivenNames().get(0).getConfidence()
                * passportDocument.getSurname().getConfidence())
            .value(passportDocument.getGivenNames().get(0).getValue() + " "
                + passportDocument.getSurname().getValue())
            .rawValue(passportDocument.getGivenNames().get(0).getValue() + " "
                + passportDocument.getSurname().getValue())
            .build())
        .build();
    expectedPassportResponse.setPassport(passportDocument);

    PassportPage passportPage = expectedPassportResponse.getPassports().get(0);
    PassportPage.PassportPageBuilder passportPageBuilder = expectedPassportResponse.getPassports()
        .get(0).toBuilder();
    passportPage = passportPageBuilder.mrz(Field.builder()
            .confidence(passportPage.getMrz1().getConfidence() * passportPage.getMrz2().getConfidence())
            .reconstructed(Boolean.TRUE)
            .rawValue(passportPage.getMrz1().getValue() + passportPage.getMrz2().getValue())
            .value(passportPage.getMrz1().getValue() + passportPage.getMrz2().getValue())
            .build())
        .fullName(Field.builder()
            .reconstructed(Boolean.TRUE)
            .confidence(passportPage.getGivenNames().get(0).getConfidence()
                * passportPage.getSurname().getConfidence())
            .value(passportPage.getGivenNames().get(0).getValue() + " "
                + passportPage.getSurname().getValue())
            .rawValue(passportPage.getGivenNames().get(0).getValue() + " "
                + passportPage.getSurname().getValue())
            .build())
        .build();

    expectedPassportResponse.setPassports(new ArrayList<>(Arrays.asList(passportPage)));
    System.out.println(expectedPassportResponse.toString());
    System.out.println(passportResponse.toString());
    Assert.assertEquals(expectedPassportResponse, passportResponse);

  }


  public void testgivenAReceiptDocumentPath_whenParsed_ThenReturnsReceipt() throws IOException {
    Client client = new Client();
    client.configureReceipt("44c0d6778265f5b8d421b41a037863f9");
    ReceiptResponse receiptResponse = client.setDocument(
            new File("src/test/resources/receipttest.jpeg"))
        .parse(ReceiptResponse.class, ParseParameters.builder()
            .documentType("receipt")
            .build());

    receiptResponse.getReceipts();
    receiptResponse.getReceipt();

    Assert.assertNotNull(receiptResponse);

    FinancialDocumentResponse response = FinancialDocumentResponseMapper.INSTANCE.receiptResponseToFinancialDocumentResponse(
        receiptResponse);

    Assert.assertNotNull(response);
  }

  public void testgivenAnInvoiceDocumentPath_whenParsed_ThenReturnsReceipt() throws IOException {
    Client client = new Client();
    client.configureInvoice("971f79664d4dd65464c31393549290aa");
    InvoiceResponse invoiceResponse = client.setDocument(
            new File("src/test/resources/invoicetest.pdf"))
        .parse(InvoiceResponse.class, ParseParameters.builder()
            .documentType("invoice")
            .build());

    Assert.assertNotNull(invoiceResponse);

    FinancialDocumentResponse response = FinancialDocumentResponseMapper.INSTANCE.invoiceResponseToFinancialDocumentResponse(
        invoiceResponse);

    Assert.assertNotNull(response);

  }

  /*

  // some int tests to help with development
  @Test
  void givenAPassport_whenParsed_ThenReturnsPassportdocument() throws IOException{
    Client client = new Client();
    client.configurePassport("ae5c3be15ca18daca4ceecdc726199f5");
    PassportResponse passportResponse = client.setDocument(new File("src/test/resources/samplepassport.jpeg"))
        .parse(PassportResponse.class, ParseParameters.builder()
            .documentType("passport")
            .build());

    PassportResponse passportResponse2 = client.setDocument(Files
        .readAllBytes(Paths.get("src/test/resources/samplepassport.jpeg")),"samplepassport.jpeg")
        .parse(PassportResponse.class, ParseParameters.builder()
            .documentType("passport")
            .build());


    PassportResponse passportResponse1 = client.setDocument(Base64
            .getEncoder()
            .encodeToString(Files.readAllBytes(Paths.get("src/test/resources/samplepassport.jpeg"))),"samplepassport.jpeg")
        .parse(PassportResponse.class, ParseParameters.builder()
            .documentType("passport")
            .build());

    Assert.assertNotNull(passportResponse1);
    Assert.assertEquals(passportResponse,passportResponse1);



  }

  public void testgivenACustomDocument_whenParsed_ThenReturnsCustomDocument() throws IOException{
    Client client = new Client();
    client.configureCustomDocument("bill_of_lading_line_items",
        "w2","w2s","vivekmindee","313f2adfb7dcd56392e7bf0fb51ea576","1");
    CustomDocumentResponse bill = client.setDocument(new File("src/test/resources/invoicetest.pdf"))
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .build());

    Assert.assertNotNull(bill);

  }
   */


}