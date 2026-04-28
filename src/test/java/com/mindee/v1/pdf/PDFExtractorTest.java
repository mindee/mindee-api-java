package com.mindee.v1.pdf;

import static com.mindee.TestingUtilities.getV1ResourcePath;

import com.mindee.input.LocalInputSource;
import com.mindee.v1.parsing.LocalResponse;
import com.mindee.v1.parsing.common.PredictResponse;
import com.mindee.v1.product.invoicesplitter.InvoiceSplitterV1;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PDFExtractorTest {

  protected PredictResponse<InvoiceSplitterV1> getInvoiceSplitterPrediction() throws IOException {
    var localResponse = new LocalResponse(
      getV1ResourcePath("products/invoice_splitter/response_v1/complete.json")
    );
    return localResponse.deserializeSyncResponse(InvoiceSplitterV1.class);
  }

  @Test
  public void givenAPDF_shouldExtractInvoicesNoStrict() throws IOException {
    LocalInputSource pdf = new LocalInputSource(
      getV1ResourcePath("products/invoice_splitter/invoice_5p.pdf")
    );
    PredictResponse<InvoiceSplitterV1> response = getInvoiceSplitterPrediction();
    InvoiceSplitterV1 inference = response.getDocument().getInference();

    PDFExtractor extractor = new PDFExtractor(pdf);
    var extractedPDFSNoStrict = extractor
      .extractInvoices(inference.getPrediction().getInvoicePageGroups(), false);
    Assertions.assertEquals(3, extractedPDFSNoStrict.size());
    Assertions.assertEquals("invoice_5p_001-001.pdf", extractedPDFSNoStrict.get(0).getFilename());
    Assertions.assertEquals("invoice_5p_002-004.pdf", extractedPDFSNoStrict.get(1).getFilename());
    Assertions.assertEquals("invoice_5p_005-005.pdf", extractedPDFSNoStrict.get(2).getFilename());
  }

  @Test
  public void givenAPDF_shouldExtractInvoicesStrict() throws IOException {
    var inputSource = new LocalInputSource(
      getV1ResourcePath("products/invoice_splitter/invoice_5p.pdf")
    );
    PredictResponse<InvoiceSplitterV1> response = getInvoiceSplitterPrediction();
    InvoiceSplitterV1 inference = response.getDocument().getInference();

    PDFExtractor extractor = new PDFExtractor(inputSource);
    var extractedPDFStrict = extractor
      .extractInvoices(inference.getPrediction().getInvoicePageGroups(), true);
    Assertions.assertEquals(2, extractedPDFStrict.size());
    Assertions.assertEquals("invoice_5p_001-001.pdf", extractedPDFStrict.get(0).getFilename());
    Assertions.assertEquals("invoice_5p_002-005.pdf", extractedPDFStrict.get(1).getFilename());
  }
}
