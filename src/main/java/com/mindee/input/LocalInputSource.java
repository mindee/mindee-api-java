package com.mindee.input;

import com.mindee.image.ImageCompressor;
import com.mindee.pdf.PDFCompression;
import com.mindee.pdf.PDFCompressor;
import com.mindee.pdf.PDFInputOperation;
import com.mindee.pdf.PDFInputOperator;
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
public class LocalInputSource {

  @Getter
  private byte[] file;
  @Getter
  private final String filename;
  private PDFInputOperation pdfInputOperator;
  private PDFCompression pdfCompressor;
  // Store here to avoid recalculating every time.
  private Boolean isPDF;

  public LocalInputSource(InputStream file, String filename) throws IOException {
    this.file = IOUtils.toByteArray(file);
    this.filename = filename;
  }

  public LocalInputSource(String filePath) throws IOException {
    var file = new File(filePath);
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
   * Get the PDFInputOperation instance.
   * Override this method to provide custom PDF input operation handling.
   *
   * @return PDFInputOperation instance
   */
  protected PDFInputOperation getPDFInputOperator() {
    if (this.pdfInputOperator == null) {
      this.pdfInputOperator = new PDFInputOperator();
    }
    return this.pdfInputOperator;
  }

  /**
   * Get the PDFCompression instance.
   * Override this method to provide custom PDF compression handling.
   *
   * @return PDFCompression instance
   */
  protected PDFCompression getPDFCompressor() {
    if (this.pdfCompressor == null) {
      this.pdfCompressor = new PDFCompressor();
    }
    return this.pdfCompressor;
  }

  /**
   * Get the number of pages in the document.
   *
   * @return the number of pages in the current file.
   * @throws IOException If an I/O error occurs during the PDF operation.
   */
  public int getPageCount() throws IOException {
    if (!this.isPDF()) {
      return 1;
    }
    return getPDFInputOperator().getPageCount(this.file);
  }

  /**
   * Applies PDF-specific operations on the current file based on the specified {@code PageOptions}.
   *
   * @param pageOptions The options specifying which pages to modify or retain in the PDF file.
   * @throws IOException If an I/O error occurs during the PDF operation.
   */
  public void applyPageOptions(PageOptions pageOptions) throws IOException {
    if (pageOptions != null && this.isPDF()) {
      this.file = getPDFInputOperator().split(this.file, pageOptions).getFile();
    }
  }

  /**
   * Returns true if the file is a PDF.
   */
  public boolean isPDF() {
    if (this.isPDF == null) {
      this.isPDF = getPDFInputOperator().isPDF(this.file);
    }
    return this.isPDF;
  }

  public void compress(
      int quality,
      Integer maxWidth,
      Integer maxHeight,
      Boolean forceSourceText,
      Boolean disableSourceText
  ) throws IOException {
    if (isPDF()) {
      this.file = getPDFCompressor()
        .compressPDF(this.file, quality, forceSourceText, disableSourceText);
    } else {
      this.file = ImageCompressor.compressImage(this.file, quality, maxWidth, maxHeight);
    }
  }

  public void compress(
      int quality,
      Integer maxWidth,
      Integer maxHeight,
      Boolean forceSourceText
  ) throws IOException {
    this.compress(quality, maxWidth, maxHeight, forceSourceText, true);
  }

  public void compress(
      int quality,
      boolean forceSourceText,
      boolean disableSourceText
  ) throws IOException {
    this.compress(quality, null, null, forceSourceText, disableSourceText);
  }

  public void compress(int quality, Integer maxWidth, Integer maxHeight) throws IOException {
    this.compress(quality, maxWidth, maxHeight, false, true);
  }

  public void compress(int quality) throws IOException {
    this.compress(quality, null, null, false, true);
  }

  public void compress() throws IOException {
    this.compress(85, null, null, false, true);
  }
}
