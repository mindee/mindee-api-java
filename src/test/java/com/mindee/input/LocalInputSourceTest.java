package com.mindee.input;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LocalInputSourceTest {
  @Test
  void loadDocument_withFile_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/invoice/invoice.pdf");
    LocalInputSource localInputSource = new LocalInputSource(file);
    Assertions.assertNotNull(localInputSource);
    Assertions.assertArrayEquals(localInputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withInputStream_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/invoice/invoice.pdf");
    LocalInputSource localInputSource = new LocalInputSource(
        Files.newInputStream(file.toPath()),
        "");
    Assertions.assertNotNull(localInputSource);
    Assertions.assertArrayEquals(localInputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withByteArray_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/invoice/invoice.pdf");
    LocalInputSource localInputSource = new LocalInputSource(
        Files.readAllBytes(file.toPath()),
        "");
    Assertions.assertNotNull(localInputSource);
    Assertions.assertArrayEquals(localInputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withBase64Encoded_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/invoice/invoice.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
    LocalInputSource localInputSource = new LocalInputSource(
        encodedFile,
        "");
    Assertions.assertNotNull(localInputSource);
    Assertions.assertArrayEquals(localInputSource.getFile(), Files.readAllBytes(file.toPath()));
  }
}
