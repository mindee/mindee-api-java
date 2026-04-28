package com.mindee.pdf;

import java.io.IOException;

public interface PDFCompression {
  byte[] compressPdf(
      byte[] pdfData,
      Integer imageQuality,
      Boolean forceSourceTextCompression,
      Boolean disableSourceText
  ) throws IOException;

  default byte[] compressPdf(
      byte[] pdfData,
      Integer imageQuality,
      Boolean forceSourceTextCompression
  ) throws IOException {
    return compressPdf(pdfData, imageQuality, forceSourceTextCompression, true);
  }

  default byte[] compressPdf(byte[] pdfData, Integer imageQuality) throws IOException {
    return compressPdf(pdfData, imageQuality, false, true);
  }

  default byte[] compressPdf(byte[] pdfData) throws IOException {
    return compressPdf(pdfData, 85, false, true);
  }
}
