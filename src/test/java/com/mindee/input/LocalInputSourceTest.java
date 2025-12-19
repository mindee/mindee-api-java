package com.mindee.input;

import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV1ResourcePath;

import com.mindee.MindeeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalInputSourceTest {
  void assertMultipagePDF(LocalInputSource inputSource, Path filePath) throws IOException {
    Assertions.assertNotNull(inputSource);

    String filename = inputSource.getFilename();
    boolean isPdf = inputSource.isPdf();
    boolean hasSourceText = inputSource.hasSourceText();
    int numberOfPages = inputSource.getPageCount();

    Assertions.assertTrue(isPdf);
    Assertions.assertTrue(hasSourceText);
    Assertions.assertEquals(3, numberOfPages);
    Assertions.assertEquals("multipage_cut-3.pdf", filename);
    Assertions.assertArrayEquals(inputSource.getFile(), Files.readAllBytes(filePath));
  }

  @Test
  void loadPDF_withFile_mustReturnAValidLocalInputSource() throws IOException {
    File file = getResourcePath("file_types/pdf/multipage_cut-3.pdf").toFile();
    LocalInputSource localInputSource = new LocalInputSource(file);
    assertMultipagePDF(localInputSource, file.toPath());
  }

  @Test
  void loadPDF_withInputStream_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/pdf/multipage_cut-3.pdf");
    LocalInputSource localInputSource = new LocalInputSource(
      Files.newInputStream(filePath),
      "multipage_cut-3.pdf"
    );
    assertMultipagePDF(localInputSource, filePath);
  }

  @Test
  void loadPDF_withByteArray_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/pdf/multipage_cut-3.pdf");
    LocalInputSource localInputSource = new LocalInputSource(
      Files.readAllBytes(filePath),
      "multipage_cut-3.pdf"
    );
    assertMultipagePDF(localInputSource, filePath);
  }

  @Test
  void loadPDF_withBase64Encoded_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/pdf/multipage_cut-3.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(filePath));
    LocalInputSource localInputSource = new LocalInputSource(encodedFile, "multipage_cut-3.pdf");
    assertMultipagePDF(localInputSource, filePath);
  }

  @Test
  void loadPDF__withoutText_mustNotDetectSourceText() throws MindeeException, IOException {
    Path filePath = getV1ResourcePath("products/invoice_splitter/default_sample.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(filePath));
    LocalInputSource localInputSource = new LocalInputSource(encodedFile, "default_sample.pdf");
    Assertions.assertNotNull(localInputSource);
    Assertions.assertTrue(localInputSource.isPdf());
    Assertions.assertFalse(localInputSource.hasSourceText());
  }

  void assertImage(LocalInputSource inputSource, Path filePath) throws IOException {
    Assertions.assertNotNull(inputSource);

    String filename = inputSource.getFilename();
    boolean isPdf = inputSource.isPdf();
    boolean hasSourceText = inputSource.hasSourceText();
    int numberOfPages = inputSource.getPageCount();

    Assertions.assertFalse(isPdf);
    Assertions.assertFalse(hasSourceText);
    Assertions.assertEquals(1, numberOfPages);
    Assertions.assertEquals("receipt.jpg", filename);
    Assertions.assertArrayEquals(inputSource.getFile(), Files.readAllBytes(filePath));
  }

  @Test
  void loadImage_withFile_mustReturnAValidLocalInputSource() throws IOException {
    File file = getResourcePath("file_types/receipt.jpg").toFile();
    LocalInputSource localInputSource = new LocalInputSource(file);
    assertImage(localInputSource, file.toPath());
  }

  @Test
  void loadImage_withInputStream_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/receipt.jpg");
    LocalInputSource localInputSource = new LocalInputSource(
      Files.newInputStream(filePath),
      "receipt.jpg"
    );
    assertImage(localInputSource, filePath);
  }

  @Test
  void loadImage_withByteArray_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/receipt.jpg");
    LocalInputSource localInputSource = new LocalInputSource(
      Files.readAllBytes(filePath),
      "receipt.jpg"
    );
    assertImage(localInputSource, filePath);
  }

  @Test
  void loadImage_withBase64Encoded_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/receipt.jpg");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(filePath));
    LocalInputSource localInputSource = new LocalInputSource(encodedFile, "receipt.jpg");
    assertImage(localInputSource, filePath);
  }

}
