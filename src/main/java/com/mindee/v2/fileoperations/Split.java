package com.mindee.v2.fileoperations;

import com.mindee.input.LocalInputSource;
import com.mindee.pdf.BasePDFExtractor;
import com.mindee.pdf.ExtractedPDF;
import com.mindee.pdf.ExtractedPDFs;
import com.mindee.v2.product.split.SplitRange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Split {
  BasePDFExtractor pdfSplitter;

  public Split(LocalInputSource inputSource) throws IOException {
    this.pdfSplitter = new BasePDFExtractor(inputSource);
  }

  public ExtractedPDF extractSingleSplit(SplitRange splitRange) throws IOException {
    return this.pdfSplitter.extractSingleDocument(splitRange.getPageRangeDistinct(), true);
  }

  public ExtractedPDFs extractMultipleSplits(ArrayList<SplitRange> splitRanges) throws IOException {
    return this.pdfSplitter
      .extractMultipleDocuments(
        splitRanges.stream().map(SplitRange::getPageRangeDistinct).collect(Collectors.toList())
      );
  }
}
