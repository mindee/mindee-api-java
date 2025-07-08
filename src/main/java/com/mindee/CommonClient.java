package com.mindee;

import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitQuery;
import java.io.IOException;

/**
 * Common client for all Mindee API clients.
 */
public abstract class CommonClient {
  protected PdfOperation pdfOperation;

  /**
   * Retrieves the file after applying page operations to it.
   * @param localInputSource Local input source to apply operations to.
   * @param pageOptions Options to apply.
   * @return A byte array of the file after applying page operations.
   * @throws IOException Throws if the file can't be accessed.
   */
  protected byte[] getSplitFile(
      LocalInputSource localInputSource,
      PageOptions pageOptions
  ) throws IOException {
    byte[] splitFile;
    if (pageOptions == null || !localInputSource.isPdf()) {
      splitFile = localInputSource.getFile();
    } else {
      splitFile = pdfOperation.split(
          new SplitQuery(localInputSource.getFile(), pageOptions)
      ).getFile();
    }
    return splitFile;
  }
}
