package com.mindee.extraction;

import static com.mindee.TestingUtilities.getV1ResourcePath;
import static com.mindee.TestingUtilities.levenshteinRatio;

import com.mindee.MindeeClient;
import com.mindee.MindeeException;
import com.mindee.TestingUtilities;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.invoice.InvoiceV4;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InvoiceSplitterAutoExtractionIT {

  private static MindeeClient client;
  private static LocalInputSource invoiceSplitterInputSource;

  @BeforeAll
  static void clientSetUp() throws IOException {
    client = new MindeeClient();
    invoiceSplitterInputSource = new LocalInputSource(
      getV1ResourcePath("products/invoice_splitter/default_sample.pdf")
    );
  }

  protected Document<InvoiceSplitterV1> getInvoiceSplitterPrediction() throws
    IOException, MindeeException, InterruptedException {
    AsyncPredictResponse<InvoiceSplitterV1> response =
      client.enqueueAndParse(InvoiceSplitterV1.class, invoiceSplitterInputSource);
    return response.getDocumentObj();
  }

  protected PredictResponse<InvoiceV4> getInvoicePrediction(LocalInputSource invoicePDF) throws
    IOException, MindeeException {
    return client.parse(InvoiceV4.class, invoicePDF);
  }

  protected String prepareInvoiceReturn(Path rstFilePath, Document<InvoiceV4> invoicePrediction)
    throws IOException {
    List<String> rstRefLines = Files.readAllLines(rstFilePath);
    String parsingVersion = invoicePrediction.getInference().getProduct().getVersion();
    String parsingId = invoicePrediction.getId();
    String rstRefString = String.join(String.format("%n"), rstRefLines);
    rstRefString = rstRefString
      .replace(TestingUtilities.getVersion(rstRefString), parsingVersion)
      .replace(TestingUtilities.getId(rstRefString), parsingId)
      .replace(TestingUtilities.getFileName(rstRefString), invoicePrediction.getFilename());
    return rstRefString;
  }

  @Test
  public void givenAPDF_shouldExtractInvoices() throws IOException, InterruptedException {
    Document<InvoiceSplitterV1> document = getInvoiceSplitterPrediction();
    InvoiceSplitterV1 inference = document.getInference();

    PDFExtractor extractor = new PDFExtractor(invoiceSplitterInputSource);
    Assertions.assertEquals(2, extractor.getPageCount());
    List<ExtractedPDF> extractedPDFsStrict =
      extractor.extractInvoices(inference.getPrediction().getInvoicePageGroups(), false);
    Assertions.assertEquals(2, extractedPDFsStrict.size());
    Assertions.assertEquals("default_sample_001-001.pdf", extractedPDFsStrict.get(0).getFilename());
    Assertions.assertEquals("default_sample_002-002.pdf", extractedPDFsStrict.get(1).getFilename());

    PredictResponse<InvoiceV4> invoice0 = getInvoicePrediction(
        extractedPDFsStrict.get(0).asInputSource()
    );
    String testStringRSTInvoice0 = prepareInvoiceReturn(
        getV1ResourcePath("products/invoices/response_v4/summary_full_invoice_p1.rst"),
      invoice0.getDocument()
    );
    double invoice0Ratio = levenshteinRatio(
        testStringRSTInvoice0,
        String.join(
            String.format("%n"),
            invoice0.getDocument().toString().split(System.lineSeparator())
        )
    );
    Assertions.assertTrue(invoice0Ratio > 0.90);

    PredictResponse<InvoiceV4> invoice1 = getInvoicePrediction(
        extractedPDFsStrict.get(1).asInputSource()
    );
    String testStringRSTInvoice1 = prepareInvoiceReturn(
        getV1ResourcePath("products/invoices/response_v4/summary_full_invoice_p2.rst"),
      invoice1.getDocument()
    );
    double invoice1Ratio = levenshteinRatio(
        testStringRSTInvoice1,
        String.join(
            String.format("%n"),
            invoice1.getDocument().toString().split(System.lineSeparator())
        )
    );
    Assertions.assertTrue(invoice1Ratio > 0.90);
  }
}
