package com.mindee.extraction;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.Page;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1Document;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1Document;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PDFExtractorTest {

  protected PredictResponse<InvoiceSplitterV1> getInvoiceSplitterPrediction(String name) throws
    IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      InvoiceSplitterV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/invoice_splitter/response_v1/" + name + ".json"),
      type
    );
  }


  @Test
  public void givenAPDF_shouldExtractInvoices() throws IOException {
    LocalInputSource pdf = new LocalInputSource(
      "src/test/resources/products/invoice_splitter/invoice_5p.pdf"
    );
    PredictResponse<InvoiceSplitterV1> response = getInvoiceSplitterPrediction("complete");
    InvoiceSplitterV1 inference = response.getDocument().getInference();

    PDFExtractor extractor = new PDFExtractor(pdf);
    Assertions.assertEquals(5, extractor.getPageCount());
    List<ExtractedPDF> extractedPDFSNoStrict = extractor.extractInvoices(inference.getPrediction().getInvoicePageGroups(), false);
    Assertions.assertEquals(3, extractedPDFSNoStrict.size());
    Assertions.assertEquals("invoice_5p_000-000.pdf", extractedPDFSNoStrict.get(0).getFilename());
  }
}
