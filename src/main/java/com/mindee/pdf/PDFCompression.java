package com.mindee.pdf;

import java.io.IOException;

/**
 * Compress a PDF.
 */
public interface PDFCompression {
  byte[] compressPDF(
      byte[] fileBytes,
      int imageQuality,
      Boolean forceSourceTextCompression,
      Boolean disableSourceText
  ) throws IOException;

  default byte[] compressPDF(
      byte[] fileBytes,
      int imageQuality,
      Boolean forceSourceTextCompression
  ) throws IOException {
    return compressPDF(fileBytes, imageQuality, forceSourceTextCompression, true);
  }

  default byte[] compressPDF(byte[] fileBytes, int imageQuality) throws IOException {
    return compressPDF(fileBytes, imageQuality, false, true);
  }

  default byte[] compressPDF(byte[] fileBytes) throws IOException {
    return compressPDF(fileBytes, 85, false, true);
  }
}
