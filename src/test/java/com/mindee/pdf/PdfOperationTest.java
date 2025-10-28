package com.mindee.pdf;

import com.mindee.input.PageOptions;
import com.mindee.input.PageOptionsOperation;
import com.mindee.MindeeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.mindee.TestingUtilities.getResourcePath;


public class PdfOperationTest {

  private final PdfOperation pdfOperation = new PdfBoxApi();

  @Test
  public void givenADocumentAndPageToKeep_whenSplit_thenReturnsOnlyKeptPage()
    throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
        .pageIndexes(new Integer[]{ 2 })
        .operation(PageOptionsOperation.KEEP_ONLY)
        .build();

    byte[] fileBytes = Files.readAllBytes(
        getResourcePath("file_types/pdf/multipage.pdf")
    );
    SplitQuery splitQuery = new SplitQuery(fileBytes, pageOptions);
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(1, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndListOfPagesToKeep_whenSplit_thenReturnsOnlyKeptPages()
    throws IOException {

    List<Integer> pageNumbersToKeep = new ArrayList<>();
    pageNumbersToKeep.add(0);
    pageNumbersToKeep.add(1);

    PageOptions pageOptions = new PageOptions.Builder()
        .pageIndexes(pageNumbersToKeep)
        .operation(PageOptionsOperation.KEEP_ONLY)
        .build();

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(getResourcePath("file_types/pdf/multipage.pdf")),
        pageOptions
    );
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(2, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndListOfPagesToRemove_whenSplit_thenReturnsOnlyNotRemovedPages()
    throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
        .pageIndexes(new Integer[]{ 0,1,2 })
        .operation(PageOptionsOperation.REMOVE)
        .build();

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(getResourcePath("file_types/pdf/multipage.pdf")),
        pageOptions
    );
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(9, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentOtherThantAPdf_whenSplit_mustFail()
    throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
        .pageIndexes(new Integer[]{ 1,2,3 })
        .operation(PageOptionsOperation.REMOVE)
        .build();

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(getResourcePath("file_types/receipt.jpg")),
        pageOptions
    );

    Assertions.assertThrows(
      MindeeException.class,
      () -> pdfOperation.split(splitQuery));
  }

  @Test
  public void givenADocumentAndListPagesToRemoveAndMinPagesCondition_whenSplit_mustNotRemovePages()
    throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
        .pageIndexes(new Integer[]{ 0 })
        .operation(PageOptionsOperation.REMOVE)
        .onMinPages(5)
        .build();

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(getResourcePath("file_types/pdf/multipage_cut-2.pdf")),
        pageOptions
    );
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(2, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndNegativeListPagesToKeep_whenSplit_thenReturnsOnlyKeptPages()
    throws IOException {

    PageOptions pageOptions = new PageOptions.Builder()
        .pageIndexes(new Integer[]{ 0,-2,-1 })
        .operation(PageOptionsOperation.KEEP_ONLY)
        .build();

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(getResourcePath("file_types/pdf/multipage.pdf")),
        pageOptions
    );
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(3, splitPdf.getTotalPageNumber());
  }
}
