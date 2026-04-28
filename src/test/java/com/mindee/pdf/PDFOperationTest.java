package com.mindee.pdf;

import static com.mindee.TestingUtilities.getResourcePath;

import com.mindee.MindeeException;
import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import com.mindee.input.PageOptionsOperation;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PDFOperationTest {

  private final InputSourcePDFOperation pdfOperation = new PDFBoxApi();

//  @Test
//  public void shouldConvertSinglePageToJpg() throws IOException {
//    LocalInputSource source = new LocalInputSource(
//      "src/test/resources/file_types/pdf/multipage.pdf"
//    );
//    PdfPageImage pdfPageImage = pdfOperation.pdfPageToImage(source, 3);
//    Assertions.assertNotNull(pdfPageImage.getImage());
//    Assertions.assertEquals(pdfPageImage.asInputSource().getFilename(), pdfPageImage.getFilename());
//    pdfPageImage.writeToFile("src/test/resources/output/");
//    Assertions
//      .assertTrue(
//        Files.exists(Paths.get("src/test/resources/output/" + pdfPageImage.getFilename()))
//      );
//  }

//  @Test
//  public void shouldConvertAllPagesToJpg() throws IOException {
//    LocalInputSource source = new LocalInputSource(
//      "src/test/resources/file_types/pdf/multipage.pdf"
//    );
//    List<PdfPageImage> pdfPageImages = pdfOperation.pdfToImages(source);
//    for (PdfPageImage pdfPageImage : pdfPageImages) {
//      Assertions.assertNotNull(pdfPageImage.getImage());
//      Assertions
//        .assertEquals(pdfPageImage.asInputSource().getFilename(), pdfPageImage.getFilename());
//      pdfPageImage.writeToFile("src/test/resources/output/");
//      Assertions
//        .assertTrue(
//          Files.exists(Paths.get("src/test/resources/output/" + pdfPageImage.getFilename()))
//        );
//    }
//  }

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
    File file = getResourcePath("output/test.pdf").toFile();
    LocalInputSource source = new LocalInputSource(file);
    Assertions.assertEquals(random, pdfOperation.getNumberOfPages(source));
    file.delete();
  }

  @Test
  public void givenADocumentAndPageToKeep_whenSplit_thenReturnsOnlyKeptPage() throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
      .pageIndexes(new Integer[] { 2 })
      .operation(PageOptionsOperation.KEEP_ONLY)
      .build();

    byte[] fileBytes = Files.readAllBytes(getResourcePath("file_types/pdf/multipage.pdf"));
    SplitPDF splitPdf = pdfOperation.split(fileBytes, pageOptions);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(1, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndListOfPagesToKeep_whenSplit_thenReturnsOnlyKeptPages() throws IOException {

    List<Integer> pageNumbersToKeep = new ArrayList<>();
    pageNumbersToKeep.add(0);
    pageNumbersToKeep.add(1);

    PageOptions pageOptions = new PageOptions.Builder()
      .pageIndexes(pageNumbersToKeep)
      .operation(PageOptionsOperation.KEEP_ONLY)
      .build();

    SplitPDF splitPdf = pdfOperation
      .split(Files.readAllBytes(getResourcePath("file_types/pdf/multipage.pdf")), pageOptions);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(2, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndListOfPagesToRemove_whenSplit_thenReturnsOnlyNotRemovedPages() throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
      .pageIndexes(new Integer[] { 0, 1, 2 })
      .operation(PageOptionsOperation.REMOVE)
      .build();

    SplitPDF splitPdf = pdfOperation
      .split(Files.readAllBytes(getResourcePath("file_types/pdf/multipage.pdf")), pageOptions);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(9, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentOtherThantAPdf_whenSplit_mustFail() throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
      .pageIndexes(new Integer[] { 1, 2, 3 })
      .operation(PageOptionsOperation.REMOVE)
      .build();

    Assertions
      .assertThrows(
        MindeeException.class,
        () -> pdfOperation
          .split(Files.readAllBytes(getResourcePath("file_types/receipt.jpg")), pageOptions)
      );
  }

  @Test
  public void givenADocumentAndListPagesToRemoveAndMinPagesCondition_whenSplit_mustNotRemovePages() throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
      .pageIndexes(new Integer[] { 0 })
      .operation(PageOptionsOperation.REMOVE)
      .onMinPages(5)
      .build();

    SplitPDF splitPdf = pdfOperation
      .split(
        Files.readAllBytes(getResourcePath("file_types/pdf/multipage_cut-2.pdf")),
        pageOptions
      );

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(2, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndNegativeListPagesToKeep_whenSplit_thenReturnsOnlyKeptPages() throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
      .pageIndexes(new Integer[] { 0, -2, -1 })
      .operation(PageOptionsOperation.KEEP_ONLY)
      .build();

    SplitPDF splitPdf = pdfOperation
      .split(Files.readAllBytes(getResourcePath("file_types/pdf/multipage.pdf")), pageOptions);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(3, splitPdf.getTotalPageNumber());
  }
}
