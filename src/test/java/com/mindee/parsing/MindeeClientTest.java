package com.mindee.parsing;

import com.mindee.DocumentToParse;
import com.mindee.MindeeClient;
import com.mindee.MindeeSettings;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.invoice.InvoiceV4Inference;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitPdf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MindeeClientTest {

  MindeeClient client;
  MindeeApi mindeeApi;
  PdfOperation pdfOperation;

  @Test
  void givenAClientForInvoice_withFile_parse_thenShouldCallMindeeApi()
    throws IOException {

    mindeeApi = Mockito.mock(MindeeApi.class);
    client = new MindeeClient(mindeeApi);
    File file = new File("src/test/resources/data/invoice/invoice.pdf");

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
      .predict(Mockito.any(),Mockito.any());
  }

  @Test
  void givenAClientForInvoice_withInputStream_parse_thenShouldCallMindeeApi()
    throws IOException {

    mindeeApi = Mockito.mock(MindeeApi.class);
    client = new MindeeClient(mindeeApi);
    File file = new File("src/test/resources/data/invoice/invoice.pdf");

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
      .predict(Mockito.any(),Mockito.any());
  }

  @Test
  void givenAClientForInvoice_withByteArray_parse_thenShouldCallMindeeApi()
    throws IOException {

    mindeeApi = Mockito.mock(MindeeApi.class);
    client = new MindeeClient(mindeeApi);
    File file = new File("src/test/resources/data/invoice/invoice.pdf");

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
      .predict(Mockito.any(),Mockito.any());
  }

  @Test
  void givenAClientForInvoiceAndPageOptions_parse_thenShouldOperateCutOnPagesAndCallTheHttpClientCorrectly()
    throws IOException {

    mindeeApi = Mockito.mock(MindeeApi.class);
    pdfOperation = Mockito.mock(PdfOperation.class);
    client = new MindeeClient(mindeeApi, pdfOperation);
    File file = new File("src/test/resources/data/invoice/invoice.pdf");
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
      .predict(Mockito.any(),Mockito.any());
    Mockito.verify(pdfOperation, Mockito.times(1))
      .split(Mockito.any());
  }
}
