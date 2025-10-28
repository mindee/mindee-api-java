package com.mindee.input;

import com.mindee.image.ImageCompressor;
import com.mindee.pdf.PDFUtils;
import com.mindee.pdf.PdfBoxApi;
import com.mindee.pdf.PdfCompressor;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitQuery;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import lombok.Getter;
import org.apache.pdfbox.io.IOUtils;

/**
 * A source document for Mindee API operations.
 */
@Getter
public final class LocalInputSource {

  private byte[] file;
  private final String filename;

  public LocalInputSource(InputStream file, String filename) throws IOException {
    this.file = IOUtils.toByteArray(file);
    this.filename = filename;
  }

  public LocalInputSource(String filePath) throws IOException {
    File file = new File(filePath);
    this.file = Files.readAllBytes(file.toPath());
    this.filename = file.getName();
  }

  public LocalInputSource(Path filePath) throws IOException {
    this.file = Files.readAllBytes(filePath);
    this.filename = filePath.getFileName().toString();
  }

  public LocalInputSource(File file) throws IOException {
    this.file = Files.readAllBytes(file.toPath());
    this.filename = file.getName();
  }

  public LocalInputSource(byte[] fileAsByteArray, String filename) {
    this.file = fileAsByteArray;
    this.filename = filename;
  }

  public LocalInputSource(String fileAsBase64, String filename) {
    this.file = Base64.getDecoder().decode(fileAsBase64.getBytes());
    this.filename = filename;
  }

  /**
   * Get the number of pages in the document.
   * @return the number of pages in the current file.
   * @throws IOException If an I/O error occurs during the PDF operation.
   */
  public int getPageCount() throws IOException {
    if (!this.isPdf()) {
      return 1;
    }
    return PDFUtils.getNumberOfPages(this.file);
  }

  /**
   * Applies PDF-specific operations on the current file based on the specified {@code PageOptions}.
   *
   * @param pageOptions The options specifying which pages to modify or retain in the PDF file.
   * @throws IOException If an I/O error occurs during the PDF operation.
   */
  public void applyPageOptions(PageOptions pageOptions) throws IOException {
    if (pageOptions != null && this.isPdf()) {
      PdfOperation pdfOperation = new PdfBoxApi();
      this.file = pdfOperation.split(
          new SplitQuery(this.file, pageOptions)
      ).getFile();
    }
  }

  public boolean isPdf() {
    return InputSourceUtils.isPdf(this.file);
  }

  public boolean hasSourceText() {
    return InputSourceUtils.hasSourceText(this.file);
  }

  public void compress(
      Integer quality, Integer maxWidth, Integer maxHeight,
      Boolean forceSourceText, Boolean disableSourceText
  ) throws IOException {
    if (isPdf()) {
      this.file = PdfCompressor.compressPdf(this.file, quality, forceSourceText, disableSourceText);
    } else {
      this.file = ImageCompressor.compressImage(this.file, quality, maxWidth, maxHeight);
    }
  }

  public void compress(
      Integer quality, Integer maxWidth, Integer maxHeight,
      Boolean forceSourceText
  ) throws IOException {
    this.compress(quality, maxWidth, maxHeight, forceSourceText, true);
  }

  public void compress(Integer quality, Integer maxWidth, Integer maxHeight) throws IOException {
    this.compress(quality, maxWidth, maxHeight, false, true);
  }

  public void compress(Integer quality, Integer maxWidth) throws IOException {
    this.compress(quality, maxWidth, null, false, true);
  }

  public void compress(Integer quality) throws IOException {
    this.compress(quality, null, null, false, true);
  }

  public void compress() throws IOException {
    this.compress(85, null, null, false, true);
  }
}
