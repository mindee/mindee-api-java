package com.mindee.pdf;

import com.mindee.input.LocalInputSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class PDFUtilsTest {

  @Test
  public void givenADocument_whenPageCounted_thenReturnsCorrectPageCount() throws IOException {
    PDDocument document = new PDDocument();
    int random = new Random().nextInt(30);
    for (int i = 0; i < random; i++) {
      PDPage page = new PDPage();
      document.addPage(page);
    }
    document.save("src/test/resources/output/test.pdf");
    document.close();
    File file = new File("src/test/resources/output/test.pdf");
    LocalInputSource source = new LocalInputSource(file);
    Assertions.assertEquals(random, PDFUtils.getNumberOfPages(source));
    file.delete();
  }

  @Test
  public void givenADocumentAndListOfPages_whenMerged_thenReturnsCorrectDocument()
      throws IOException {
    Path original = Paths.get("src/test/resources/file_types/pdf/multipage.pdf");
    Path copied = Paths.get("src/test/resources/output/fileToTest.pdf");
    Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
    File file = new File("src/test/resources/output/fileToTest.pdf");
    List<Integer> pageList = Arrays.asList(0, 2, 3, 1, 10, 2, 1);
    byte[] newPdf = PDFUtils.mergePdfPages(file, pageList);
    PDDocument document = Loader.loadPDF(newPdf);

    Assertions.assertEquals(7, document.getNumberOfPages());
    document.close();
    file.delete();
  }

  @Test
  public void givenANonEmptyDocument_whenEmptyChecked_shouldReturnFalse() throws IOException {
    File pdfFile = new File("src/test/resources/file_types/pdf/multipage.pdf");
    Assertions.assertFalse(PDFUtils.isPdfEmpty(pdfFile));
  }

  @Test
  public void givenAnEmptyDocument_whenEmptyChecked_shouldReturnTrue() throws IOException {
    PDDocument document = new PDDocument();
    int random = new Random().nextInt(30);
    for (int i = 0; i < random; i++) {
      PDPage page = new PDPage();
      document.addPage(page);
    }
    document.save("src/test/resources/output/test.pdf");
    document.close();
    File file = new File("src/test/resources/output/test.pdf");
    Assertions.assertTrue(PDFUtils.isPdfEmpty(file));
    file.delete();
  }

  @Test
  public void shouldConvertAllPagesToJpg() throws IOException {
    List<PdfPageImage> pdfPageImages = PDFUtils.pdfToImages(
      "src/test/resources/file_types/pdf/multipage_cut-2.pdf"
    );
    for (PdfPageImage pdfPageImage : pdfPageImages) {
      Assertions.assertNotNull(pdfPageImage.getImage());
      Assertions.assertEquals(pdfPageImage.asInputSource().getFilename(), pdfPageImage.getFilename());
      pdfPageImage.writeToFile("src/test/resources/output/");
      Assertions.assertTrue(Files.exists(Paths.get("src/test/resources/output/" + pdfPageImage.getFilename())));
    }
  }

  @Test
  public void shouldConvertSinglePageToJpg() throws IOException {
    LocalInputSource source = new LocalInputSource("src/test/resources/file_types/pdf/multipage.pdf");
    PdfPageImage pdfPageImage = PDFUtils.pdfPageToImage(source, 3);
    Assertions.assertNotNull(pdfPageImage.getImage());
    Assertions.assertEquals(pdfPageImage.asInputSource().getFilename(), pdfPageImage.getFilename());
    pdfPageImage.writeToFile("src/test/resources/output/");
    Assertions.assertTrue(Files.exists(Paths.get("src/test/resources/output/" + pdfPageImage.getFilename())));
  }
}
