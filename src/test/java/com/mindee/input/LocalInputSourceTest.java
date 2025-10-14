package com.mindee.input;

import com.mindee.MindeeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalInputSourceTest {
  void assertMultipagePDF(LocalInputSource inputSource, File file) throws IOException {
    Assertions.assertNotNull(inputSource);

    String filename = inputSource.getFilename();
    boolean isPdf = inputSource.isPdf();
    boolean hasSourceText = inputSource.hasSourceText();
    int numberOfPages = inputSource.getPageCount();

    Assertions.assertTrue(isPdf);
    Assertions.assertTrue(hasSourceText);
    Assertions.assertEquals(3, numberOfPages);
    Assertions.assertEquals("multipage_cut-3.pdf", filename);
    Assertions.assertArrayEquals(inputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadPDF_withFile_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage_cut-3.pdf");
    LocalInputSource localInputSource = new LocalInputSource(file);
    assertMultipagePDF(localInputSource, file);
  }

  @Test
  void loadPDF_withInputStream_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage_cut-3.pdf");
    LocalInputSource localInputSource = new LocalInputSource(
        Files.newInputStream(file.toPath()),
        "multipage_cut-3.pdf"
    );
    assertMultipagePDF(localInputSource, file);
  }

  @Test
  void loadPDF_withByteArray_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage_cut-3.pdf");
    LocalInputSource localInputSource = new LocalInputSource(
        Files.readAllBytes(file.toPath()),
        "multipage_cut-3.pdf"
    );
    assertMultipagePDF(localInputSource, file);
  }

  @Test
  void loadPDF_withBase64Encoded_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage_cut-3.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
    LocalInputSource localInputSource = new LocalInputSource(
        encodedFile,
        "multipage_cut-3.pdf"
    );
    assertMultipagePDF(localInputSource, file);
  }

  @Test
  void loadPDF__withoutText_mustNotDetectSourceText() throws MindeeException, IOException {
    File file = new File("src/test/resources/products/invoice_splitter/default_sample.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
    LocalInputSource localInputSource = new LocalInputSource(
        encodedFile,
        "default_sample.pdf"
    );
    Assertions.assertNotNull(localInputSource);
    Assertions.assertTrue(localInputSource.isPdf());
    Assertions.assertFalse(localInputSource.hasSourceText());
  }

  void assertImage(LocalInputSource inputSource, File file) throws IOException {
    Assertions.assertNotNull(inputSource);

    String filename = inputSource.getFilename();
    boolean isPdf = inputSource.isPdf();
    boolean hasSourceText = inputSource.hasSourceText();
    int numberOfPages = inputSource.getPageCount();

    Assertions.assertFalse(isPdf);
    Assertions.assertFalse(hasSourceText);
    Assertions.assertEquals(1, numberOfPages);
    Assertions.assertEquals("receipt.jpg", filename);
    Assertions.assertArrayEquals(inputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadImage_withFile_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/receipt.jpg");
    LocalInputSource localInputSource = new LocalInputSource(file);
    assertImage(localInputSource, file);
  }

  @Test
  void loadImage_withInputStream_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/receipt.jpg");
    LocalInputSource localInputSource = new LocalInputSource(
        Files.newInputStream(file.toPath()),
        "receipt.jpg"
    );
    assertImage(localInputSource, file);
  }

  @Test
  void loadImage_withByteArray_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/receipt.jpg");
    LocalInputSource localInputSource = new LocalInputSource(
        Files.readAllBytes(file.toPath()),
        "receipt.jpg"
    );
    assertImage(localInputSource, file);
  }

  @Test
  void loadImage_withBase64Encoded_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/receipt.jpg");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
    LocalInputSource localInputSource = new LocalInputSource(
        encodedFile,
        "receipt.jpg"
    );
    assertImage(localInputSource, file);
  }

}
