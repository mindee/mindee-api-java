package com.mindee.pdf;

import com.mindee.utils.PDFUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Assert;
import org.junit.Test;


public class PDFUtilsTest {

  @Test
  public void givenADocument_whenPageCounted_thenReturnsCorrectPageCount() throws IOException {
    PDDocument document = new PDDocument();
    int random = new Random().nextInt(30);
    for (int i = 0; i < random; i++) {
      PDPage page = new PDPage();
      document.addPage(page);
    }
    document.save("src/test/resources/test.pdf");
    document.close();
    File file = new File("src/test/resources/test.pdf");
    Assert.assertEquals(random, PDFUtils.countPdfPages(new FileInputStream(file)));
    file.delete();
  }

  @Test
  public void givenADocumentAndListOfPages_whenMerged_thenReturnsCorrectDocument()
      throws IOException {
    Path original = Paths.get("src/test/resources/data/invoice/invoice_10p.pdf");
    Path copied = Paths.get("src/test/resources/fileToTest.pdf");
    Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
    File file = new File("src/test/resources/fileToTest.pdf");
    List<Integer> pageList = Arrays.asList(0, 2, 3, 1, 10, 2, 1);
    byte[] newPdf = PDFUtils.mergePdfPages(file, pageList);
    PDDocument document = PDDocument.load(newPdf);

    Assert.assertEquals(6, document.getNumberOfPages());
    document.close();
    file.delete();
  }

  @Test
  public void givenANonEmptyDocument_whenEmptyChecked_shouldReturnFalse() throws IOException {
    Path original = Paths.get("src/test/resources/data/invoice/invoice.pdf");

    Assert.assertFalse(PDFUtils.isPdfEmpty(new FileInputStream(original.toFile())));

  }

  @Test
  public void givenAnEmptyDocument_whenEmptyChecked_shouldReturnTrue() throws IOException {
    PDDocument document = new PDDocument();
    int random = new Random().nextInt(30);
    for (int i = 0; i < random; i++) {
      PDPage page = new PDPage();
      document.addPage(page);
    }
    document.save("src/test/resources/test.pdf");
    document.close();
    File file = new File("src/test/resources/test.pdf");
    Assert.assertTrue(PDFUtils.isPdfEmpty(file));
    file.delete();

  }

}
