package com.mindee.input;

import com.mindee.image.ImageCompressor;
import com.mindee.pdf.InputSourcePDFOperation;
import com.mindee.pdf.PDFBoxApi;
import com.mindee.pdf.PDFCompressor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.io.IOUtils;

/**
 * A source document for Mindee API operations.
 */
public final class LocalInputSource {

  @Getter
  private byte[] file;
  @Getter
  private final String filename;
  @Setter
  private InputSourcePDFOperation pdfOperation;

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

  public InputSourcePDFOperation getPdfOperation() {
    if (this.pdfOperation == null) {
      this.pdfOperation = new PDFBoxApi();
    }
    return this.pdfOperation;
  }

  /**
   * Get the number of pages in the document.
   *
   * @return the number of pages in the current file.
   * @throws IOException If an I/O error occurs during the PDF operation.
   */
  public int getPageCount() throws IOException {
    if (!this.isPdf()) {
      return 1;
    }
    return getPdfOperation().getNumberOfPages(this.file);
  }

  /**
   * Applies PDF-specific operations on the current file based on the specified {@code PageOptions}.
   *
   * @param pageOptions The options specifying which pages to modify or retain in the PDF file.
   * @throws IOException If an I/O error occurs during the PDF operation.
   */
  public void applyPageOptions(PageOptions pageOptions) throws IOException {
    if (pageOptions != null && this.isPdf()) {
      this.file = getPdfOperation().split(this.file, pageOptions).getFile();
    }
  }

  public boolean isPdf() {
    return getPdfOperation().isPdf(this.file);
  }

  public boolean hasSourceText() {
    return getPdfOperation().hasSourceText(this.file);
  }

  public void compress(
      Integer quality,
      Integer maxWidth,
      Integer maxHeight,
      Boolean forceSourceText,
      Boolean disableSourceText
  ) throws IOException {
    if (isPdf()) {
      this.file = PDFCompressor.compressPdf(this.file, quality, forceSourceText, disableSourceText);
    } else {
      this.file = ImageCompressor.compressImage(this.file, quality, maxWidth, maxHeight);
    }
  }

  public void compress(
      Integer quality,
      Integer maxWidth,
      Integer maxHeight,
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
