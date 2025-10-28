package com.mindee.extraction;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.mindee.TestingUtilities.getV1ResourcePath;

public class PDFExtractorTest {

  protected PredictResponse<InvoiceSplitterV1> getInvoiceSplitterPrediction() throws
    IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      InvoiceSplitterV1.class
    );
    return objectMapper.readValue(
      getV1ResourcePath("products/invoice_splitter/response_v1/complete.json").toFile(),
      type
    );
  }


  @Test
  public void givenAPDF_shouldExtractInvoicesNoStrict() throws IOException {
    LocalInputSource pdf = new LocalInputSource(
      getV1ResourcePath("products/invoice_splitter/invoice_5p.pdf")
    );
    PredictResponse<InvoiceSplitterV1> response = getInvoiceSplitterPrediction();
    InvoiceSplitterV1 inference = response.getDocument().getInference();

    PDFExtractor extractor = new PDFExtractor(pdf);
    Assertions.assertEquals(5, extractor.getPageCount());
    List<ExtractedPDF> extractedPDFSNoStrict =
      extractor.extractInvoices(inference.getPrediction().getInvoicePageGroups(), false);
    Assertions.assertEquals(3, extractedPDFSNoStrict.size());
    Assertions.assertEquals("invoice_5p_001-001.pdf", extractedPDFSNoStrict.get(0).getFilename());
    Assertions.assertEquals("invoice_5p_002-004.pdf", extractedPDFSNoStrict.get(1).getFilename());
    Assertions.assertEquals("invoice_5p_005-005.pdf", extractedPDFSNoStrict.get(2).getFilename());
  }

  @Test
  public void givenAPDF_shouldExtractInvoicesStrict() throws IOException {
    LocalInputSource pdf = new LocalInputSource(
      getV1ResourcePath("products/invoice_splitter/invoice_5p.pdf")
    );
    PredictResponse<InvoiceSplitterV1> response = getInvoiceSplitterPrediction();
    InvoiceSplitterV1 inference = response.getDocument().getInference();

    PDFExtractor extractor = new PDFExtractor(pdf);
    Assertions.assertEquals(5, extractor.getPageCount());
    List<ExtractedPDF> extractedPDFStrict =
      extractor.extractInvoices(inference.getPrediction().getInvoicePageGroups(), true);
    Assertions.assertEquals(2, extractedPDFStrict.size());
    Assertions.assertEquals("invoice_5p_001-001.pdf", extractedPDFStrict.get(0).getFilename());
    Assertions.assertEquals("invoice_5p_002-005.pdf", extractedPDFStrict.get(1).getFilename());
  }
}
