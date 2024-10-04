package com.mindee.input;

import com.mindee.image.ImageCompressor;
import com.mindee.pdf.PdfCompressor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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

  public boolean isPdf() {
    return InputSourceUtils.isPdf(this.file);
  }

  public boolean hasSourceText() {
    return InputSourceUtils.hasSourceText(this.file);
  }

  public void compress(
      Integer quality, Integer maxWidth, Integer maxHeight,
      Boolean forceSourceText, Boolean disableSourceText
  )
      throws IOException {
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
