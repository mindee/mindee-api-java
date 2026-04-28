package com.mindee.pdf;

import com.mindee.MindeeException;
import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import java.io.IOException;

public interface InputSourcePDFOperation {

  /**
   * Split a PDF file.
   */
  SplitPDF split(byte[] fileBytes, PageOptions pageOptions) throws IOException;

  /**
   * Get the number of pages in a PDF file.
   */
  int getNumberOfPages(byte[] fileBytes) throws IOException;

  default int getNumberOfPages(LocalInputSource inputSource) throws IOException {
    return getNumberOfPages(inputSource.getFile());
  }

  /**
   * Returns true if the file is a PDF.
   */
  boolean isPdf(byte[] fileBytes);

  /**
   * Returns true if the source PDF has source text inside. Returns false for images.
   *
   * @param fileBytes A byte array representing a PDF.
   * @return True if at least one character exists in one page.
   * @throws MindeeException if the file could not be read.
   */
  boolean hasSourceText(byte[] fileBytes);
}
