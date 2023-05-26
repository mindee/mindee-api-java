package com.mindee.parsing;

import com.mindee.DocumentToParse;
import com.mindee.MindeeClient;
import com.mindee.ParseParameter;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.custom.CustomV1Inference;
import com.mindee.parsing.invoice.InvoiceV4Inference;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitPdf;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MindeeClientTest {

  MindeeClient client;
  MindeeApi mindeeApi;
  PdfOperation pdfOperation;

  @BeforeEach
  public void setUp() {

    mindeeApi = Mockito.mock(MindeeApi.class);
    pdfOperation = Mockito.mock(PdfOperation.class);
    client = new MindeeClient(pdfOperation, mindeeApi);
  }

  @Test
  void givenAClientForCustom_withFile_parse_thenShouldCallMindeeApi()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
        .thenReturn(new Document<>());

    Document<CustomV1Inference> document = client.parse(
        new DocumentToParse(file),
        new CustomEndpoint("", "", ""));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  void givenAClientForCustomAndPageOptions_parse_thenShouldOperateCutOnPagesAndCallTheHttpClientCorrectly()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");
    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);

    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
        .thenReturn(new Document<>());
    Mockito.when(
            pdfOperation.split(
                Mockito.any()))
        .thenReturn(new SplitPdf(new byte[0], 0));

    Document<CustomV1Inference> document = client.parse(
        new DocumentToParse(file),
        new CustomEndpoint("", "", ""),
        new PageOptions(
            pageNumberToKeep, PageOptionsOperation.KEEP_ONLY_LISTED_PAGES, 0));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any(), Mockito.any());
    Mockito.verify(pdfOperation, Mockito.times(1))
        .split(Mockito.any());
  }

  @Test
  void givenAClientForInvoice_withFile_parse_thenShouldCallMindeeApi()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(new Document<>());

    Document<InvoiceV4Inference> document = client.parse(
        InvoiceV4Inference.class,
        new DocumentToParse(file));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any());
  }

  @Test
  void givenAClientForInvoice_withInputStream_parse_thenShouldCallMindeeApi()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(new Document<>());

    Document<InvoiceV4Inference> document = client.parse(
        InvoiceV4Inference.class,
        new DocumentToParse(
            Files.newInputStream(file.toPath()),
            ""));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any());
  }

  @Test
  void givenAClientForInvoice_withByteArray_parse_thenShouldCallMindeeApi()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(new Document<>());

    Document<InvoiceV4Inference> document = client.parse(
        InvoiceV4Inference.class,
        new DocumentToParse(
            Files.readAllBytes(file.toPath()),
            ""));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any());
  }

  @Test
  void givenAClientForInvoiceAndPageOptions_parse_thenShouldOperateCutOnPagesAndCallTheHttpClientCorrectly()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");
    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);

    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(new Document<>());
    Mockito.when(
            pdfOperation.split(
                Mockito.any()))
        .thenReturn(new SplitPdf(new byte[0], 0));

    Document<InvoiceV4Inference> document = client.parse(
        InvoiceV4Inference.class,
        new DocumentToParse(file),
        new PageOptions(
            pageNumberToKeep, PageOptionsOperation.KEEP_ONLY_LISTED_PAGES, 0));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any());
    Mockito.verify(pdfOperation, Mockito.times(1))
        .split(Mockito.any());
  }

  @Test
  void loadDocument_withFile_mustReturnAValidDocumentToParse() throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    DocumentToParse documentToParse = client.loadDocument(file);

    Assertions.assertNotNull(documentToParse);
    Assertions.assertArrayEquals(documentToParse.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withInputStream_mustReturnAValidDocumentToParse() throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    DocumentToParse documentToParse = client.loadDocument(
        Files.newInputStream(file.toPath()),
        "");

    Assertions.assertNotNull(documentToParse);
    Assertions.assertArrayEquals(documentToParse.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withByteArray_mustReturnAValidDocumentToParse() throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    DocumentToParse documentToParse = client.loadDocument(
        Files.readAllBytes(file.toPath()),
        "");

    Assertions.assertNotNull(documentToParse);
    Assertions.assertArrayEquals(documentToParse.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withBase64Encoded_mustReturnAValidDocumentToParse() throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));

    DocumentToParse documentToParse = client.loadDocument(
        encodedFile,
        "");

    Assertions.assertNotNull(documentToParse);
    Assertions.assertArrayEquals(documentToParse.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void givenADocumentUrl_whenParsed_shouldCallApiWithCorrectParams() throws IOException {

    ArgumentCaptor<Class> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<ParseParameter> parseParameterArgumentCaptor = ArgumentCaptor.forClass(
        ParseParameter.class);

    URL docUrl = new URL("https://this.document.does.not.exist");
    Document<InvoiceV4Inference> document = client.parse(
        InvoiceV4Inference.class, docUrl);

    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(classArgumentCaptor.capture(), parseParameterArgumentCaptor.capture());
    Assertions.assertEquals(InvoiceV4Inference.class, classArgumentCaptor.getValue());
    Assertions.assertEquals(docUrl, parseParameterArgumentCaptor.getValue().getFileUrl());
    Assertions.assertNull(parseParameterArgumentCaptor.getValue().getFile());
    Assertions.assertNull(parseParameterArgumentCaptor.getValue().getFileName());
  }

  @Test
  void givenACustomDocumentUrl_whenParsed_shouldCallApiWithCorrectParams() throws IOException {

    ArgumentCaptor<Class> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<CustomEndpoint> customEndpointArgumentCaptor = ArgumentCaptor.forClass(
        CustomEndpoint.class);
    ArgumentCaptor<ParseParameter> parseParameterArgumentCaptor = ArgumentCaptor.forClass(
        ParseParameter.class);

    URL docUrl = new URL("https://this.document.does.not.exist");
    CustomEndpoint endpoint = new CustomEndpoint("dsddw", "dcsdcd", "dsfdd");
    Document<CustomV1Inference> document = client.parse(
        docUrl, endpoint);

    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(classArgumentCaptor.capture(), customEndpointArgumentCaptor.capture(),
            parseParameterArgumentCaptor.capture());
    Assertions.assertEquals(CustomV1Inference.class, classArgumentCaptor.getValue());
    Assertions.assertEquals(docUrl, parseParameterArgumentCaptor.getValue().getFileUrl());
    Assertions.assertEquals(endpoint, customEndpointArgumentCaptor.getValue());
    Assertions.assertNull(parseParameterArgumentCaptor.getValue().getFile());
    Assertions.assertNull(parseParameterArgumentCaptor.getValue().getFileName());
  }
}
