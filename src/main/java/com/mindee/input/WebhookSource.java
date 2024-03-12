package com.mindee.input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import lombok.Getter;
import org.apache.pdfbox.io.IOUtils;

/**
 * A source JSON coming from a Mindee webhook callback.
 */
@Getter
public class WebhookSource {
  private final byte[] file;

  /**
   * Load from an {@link InputStream}.
   */
  public WebhookSource(InputStream input) throws IOException {
    this.file = IOUtils.toByteArray(input);
  }

  /**
   * Load from a UTF-8 {@link String}.
   */
  public WebhookSource(String input) {
    this.file = input.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Load from a {@link File}.
   */
  public WebhookSource(File input) throws IOException {
    this.file = Files.readAllBytes(input.toPath());
  }
}
