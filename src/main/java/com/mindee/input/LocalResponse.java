package com.mindee.input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import lombok.Getter;
import org.apache.pdfbox.io.IOUtils;

/**
 * A Mindee response saved locally.
 */
@Getter
public class LocalResponse {
  private final byte[] file;

  /**
   * Load from an {@link InputStream}.
   */
  public LocalResponse(InputStream input) throws IOException {
    this.file = IOUtils.toByteArray(input);
  }

  /**
   * Load from a UTF-8 {@link String}.
   */
  public LocalResponse(String input) {
    this.file = input.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Load from a {@link File}.
   */
  public LocalResponse(File input) throws IOException {
    this.file = Files.readAllBytes(input.toPath());
  }
}
