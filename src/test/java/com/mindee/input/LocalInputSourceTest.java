package com.mindee.input;

import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV1ResourcePath;

import com.mindee.MindeeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class LocalInputSourceTest {
  void assertMultipagePDF(LocalInputSource inputSource, Path filePath) throws IOException {
    Assertions.assertNotNull(inputSource);

    Assertions.assertTrue(inputSource.isPDF());
    Assertions.assertEquals(3, inputSource.getPageCount());
    Assertions.assertEquals("multipage_cut-3.pdf", inputSource.getFilename());
    Assertions.assertArrayEquals(inputSource.getFile(), Files.readAllBytes(filePath));
  }

  @Test
  void loadPDF_withFile_mustReturnAValidLocalInputSource() throws IOException {
    File file = getResourcePath("file_types/pdf/multipage_cut-3.pdf").toFile();
    var localInputSource = new LocalInputSource(file);
    assertMultipagePDF(localInputSource, file.toPath());
  }

  @Test
  void loadPDF_withInputStream_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/pdf/multipage_cut-3.pdf");
    var localInputSource = new LocalInputSource(
      Files.newInputStream(filePath),
      "multipage_cut-3.pdf"
    );
    assertMultipagePDF(localInputSource, filePath);
  }

  @Test
  void loadPDF_withByteArray_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/pdf/multipage_cut-3.pdf");
    var localInputSource = new LocalInputSource(
      Files.readAllBytes(filePath),
      "multipage_cut-3.pdf"
    );
    assertMultipagePDF(localInputSource, filePath);
  }

  @Test
  void loadPDF_withBase64Encoded_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/pdf/multipage_cut-3.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(filePath));
    var localInputSource = new LocalInputSource(encodedFile, "multipage_cut-3.pdf");
    assertMultipagePDF(localInputSource, filePath);
  }

  @Test
  void loadPDF__withoutText_mustNotDetectSourceText() throws MindeeException, IOException {
    Path filePath = getV1ResourcePath("products/invoice_splitter/default_sample.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(filePath));
    var localInputSource = new LocalInputSource(encodedFile, "default_sample.pdf");
    Assertions.assertNotNull(localInputSource);
    Assertions.assertTrue(localInputSource.isPDF());
  }

  void assertImage(
      LocalInputSource inputSource,
      Path filePath,
      String filename
  ) throws IOException {
    Assertions.assertNotNull(inputSource);

    Assertions.assertFalse(inputSource.isPDF());
    Assertions.assertEquals(1, inputSource.getPageCount());
    Assertions.assertEquals(filename, inputSource.getFilename());
    Assertions.assertArrayEquals(inputSource.getFile(), Files.readAllBytes(filePath));
  }

  private static Stream<String> imageExtensions() {
    return Stream.of("heic", "heif", "jpg", "jpga", "png", "tif", "tiff", "webp");
  }

  @ParameterizedTest
  @MethodSource("imageExtensions")
  void loadImage_withFile_mustReturnAValidLocalInputSource(String extension) throws IOException {
    File file = getResourcePath("file_types/receipt." + extension).toFile();
    var inputSource = new LocalInputSource(file);
    assertImage(inputSource, file.toPath(), "receipt." + extension);
  }

  @ParameterizedTest
  @MethodSource("imageExtensions")
  void loadImage_withInputStream_mustReturnAValidLocalInputSource(
      String extension
  ) throws IOException {
    Path filePath = getResourcePath("file_types/receipt." + extension);
    var inputSource = new LocalInputSource(Files.newInputStream(filePath), "receipt." + extension);
    assertImage(inputSource, filePath, "receipt." + extension);
  }

  @ParameterizedTest
  @MethodSource("imageExtensions")
  void loadImage_withByteArray_mustReturnAValidLocalInputSource(
      String extension
  ) throws IOException {
    Path filePath = getResourcePath("file_types/receipt." + extension);
    var inputSource = new LocalInputSource(Files.readAllBytes(filePath), "receipt." + extension);
    assertImage(inputSource, filePath, "receipt." + extension);
  }

  @Test
  void loadImage_withBase64Encoded_mustReturnAValidLocalInputSource() throws IOException {
    Path filePath = getResourcePath("file_types/receipt.txt");
    String encodedFile = Files.readString(filePath, java.nio.charset.StandardCharsets.UTF_8);
    var inputSource = new LocalInputSource(encodedFile, "receipt.jpg");
    Assertions.assertFalse(inputSource.isPDF());
    Assertions.assertEquals(1, inputSource.getPageCount());
    Assertions.assertEquals("receipt.jpg", inputSource.getFilename());
  }

}
