package com.mindee.input;

import com.mindee.MindeeException;
import com.mindee.image.ImageCompressor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalInputSourceTest {
  @Test
  void loadDocument_withFile_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage.pdf");
    LocalInputSource localInputSource = new LocalInputSource(file);
    Assertions.assertNotNull(localInputSource);
    Assertions.assertArrayEquals(localInputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withInputStream_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage.pdf");
    LocalInputSource localInputSource = new LocalInputSource(
      Files.newInputStream(file.toPath()),
      "multipage.pdf"
    );
    Assertions.assertNotNull(localInputSource);
    Assertions.assertArrayEquals(localInputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withByteArray_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage.pdf");
    LocalInputSource localInputSource = new LocalInputSource(
      Files.readAllBytes(file.toPath()),
      "multipage.pdf"
    );
    Assertions.assertNotNull(localInputSource);
    Assertions.assertArrayEquals(localInputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void loadDocument_withBase64Encoded_mustReturnAValidLocalInputSource() throws IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
    LocalInputSource localInputSource = new LocalInputSource(
      encodedFile,
      "multipage.pdf"
    );
    Assertions.assertNotNull(localInputSource);
    Assertions.assertArrayEquals(localInputSource.getFile(), Files.readAllBytes(file.toPath()));
  }

  @Test
  void pdf_inputSource_withText_mustDetectSourceText() throws MindeeException, IOException {
    File file = new File("src/test/resources/file_types/pdf/multipage.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
    LocalInputSource localInputSource = new LocalInputSource(
      encodedFile,
      "multipage.pdf"
    );
    Assertions.assertNotNull(localInputSource);
    Assertions.assertTrue(localInputSource.hasSourceText());
  }

  @Test
  void pdf_inputSource_withoutText_mustNotDetectSourceText() throws MindeeException, IOException {
    File file = new File("src/test/resources/products/invoice_splitter/default_sample.pdf");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
    LocalInputSource localInputSource = new LocalInputSource(
      encodedFile,
      "default_sample.pdf"
    );
    Assertions.assertNotNull(localInputSource);
    Assertions.assertFalse(localInputSource.hasSourceText());
  }

  @Test
  void image_inputSource_mustNotDetectSourceText() throws MindeeException, IOException {
    File file = new File("src/test/resources/products/expense_receipts/default_sample.jpg");
    String encodedFile = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
    LocalInputSource localInputSource = new LocalInputSource(
      encodedFile,
      "default_sample.jpg"
    );
    Assertions.assertNotNull(localInputSource);
    Assertions.assertFalse(localInputSource.hasSourceText());
  }

  @Test
  public void fromInputSource_imageQuality_should_Compress() throws IOException {
    LocalInputSource receiptInput =
      new LocalInputSource("src/test/resources/file_types/receipt.jpg");

    receiptInput.compress(40);
    Path outputPath = Paths.get("src/test/resources/output/compresstest.jpg");

    Files.write(outputPath, receiptInput.getFile());

    Assertions.assertTrue(Files.exists(outputPath));

    long initialFileSize = Files.size(Paths.get("src/test/resources/file_types/receipt.jpg"));
    long compressedFileSize = Files.size(outputPath);

    Assertions.assertTrue(compressedFileSize < initialFileSize,
      "Compressed file size (" + compressedFileSize +
        ") should be less than initial file size (" + initialFileSize + ")");
  }

  @Test
  public void testImageQualityCompressesFromCompressor() throws IOException {
    Path outputDir = Paths.get("src/test/resources/output");
    LocalInputSource receiptInput =
      new LocalInputSource("src/test/resources/file_types/receipt.jpg");
    List<byte[]> compresses = Arrays.asList(
      ImageCompressor.compressImage(receiptInput.getFile(), 100),
      ImageCompressor.compressImage(receiptInput.getFile()),
      ImageCompressor.compressImage(receiptInput.getFile(), 50),
      ImageCompressor.compressImage(receiptInput.getFile(), 10),
      ImageCompressor.compressImage(receiptInput.getFile(), 1)
    );

    List<Path> outputPaths = Arrays.asList(
      outputDir.resolve("compress100.jpg"),
      outputDir.resolve("compress75.jpg"),
      outputDir.resolve("compress50.jpg"),
      outputDir.resolve("compress10.jpg"),
      outputDir.resolve("compress1.jpg")
    );

    for (int i = 0; i < compresses.size(); i++) {
      Files.write(outputPaths.get(i), compresses.get(i));
    }

    long initialFileSize = Files.size(Paths.get("src/test/resources/file_types/receipt.jpg"));
    List<Long> compressedFileSizes = outputPaths.stream()
      .map(path -> {
        try {
          return Files.size(path);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }).collect(Collectors.toList());

    Assertions.assertTrue(initialFileSize < compressedFileSizes.get(0));
    Assertions.assertTrue(initialFileSize < compressedFileSizes.get(1));
    Assertions.assertTrue(compressedFileSizes.get(1) > compressedFileSizes.get(2));
    Assertions.assertTrue(compressedFileSizes.get(2) > compressedFileSizes.get(3));
    Assertions.assertTrue(compressedFileSizes.get(3) > compressedFileSizes.get(4));
  }

  @Test
  public void testImageResizeFromInputSource() throws IOException {
    Path outputDir = Paths.get("src/test/resources/output");
    LocalInputSource imageResizeInput =
      new LocalInputSource("src/test/resources/file_types/receipt.jpg");
    imageResizeInput.compress(75, 250, 1000);
    Path outputPath = outputDir.resolve("resize_indirect.jpg");
    Files.write(outputPath, imageResizeInput.getFile());

    long initialFileSize = Files.size(Paths.get("src/test/resources/file_types/receipt.jpg"));
    long resizedFileSize = Files.size(outputPath);
    Assertions.assertTrue(resizedFileSize < initialFileSize);

    BufferedImage resizedImage = ImageIO.read(outputPath.toFile());
    Assertions.assertEquals(250, resizedImage.getWidth());
    Assertions.assertEquals(333, resizedImage.getHeight());
  }

  @Test
  public void testImageResizeFromCompressor() throws IOException {
    Path outputDir = Paths.get("src/test/resources/output");
    LocalInputSource imageResizeInput =
      new LocalInputSource("src/test/resources/file_types/receipt.jpg");
    List<byte[]> resizes = Arrays.asList(
      ImageCompressor.compressImage(imageResizeInput.getFile(), 75, 500, null),
      ImageCompressor.compressImage(imageResizeInput.getFile(), 75, 250, 500),
      ImageCompressor.compressImage(imageResizeInput.getFile(), 75, 500, 250),
      ImageCompressor.compressImage(imageResizeInput.getFile(), 75, null, 250)
    );

    List<Path> outputPaths = Arrays.asList(
      outputDir.resolve("resize500xnull.jpg"),
      outputDir.resolve("resize250x500.jpg"),
      outputDir.resolve("resize500x250.jpg"),
      outputDir.resolve("resizenullx250.jpg")
    );

    for (int i = 0; i < resizes.size(); i++) {
      Files.write(outputPaths.get(i), resizes.get(i));
    }

    long initialFileSize = Files.size(Paths.get("src/test/resources/file_types/receipt.jpg"));
    List<Long> resizedFileSizes = outputPaths.stream()
      .map(path -> {
        try {
          return Files.size(path);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }).collect(Collectors.toList());

    Assertions.assertTrue(initialFileSize > resizedFileSizes.get(0));
    Assertions.assertTrue(resizedFileSizes.get(0) > resizedFileSizes.get(1));
    Assertions.assertTrue(resizedFileSizes.get(1) > resizedFileSizes.get(2));
    Assertions.assertEquals(resizedFileSizes.get(2), resizedFileSizes.get(3));
  }
}
