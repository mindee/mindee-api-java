package com.mindee.pdf;

import com.mindee.input.PageOptions;
import com.mindee.input.PageOptionsOperation;
import com.mindee.MindeeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class PdfOperationTest {

  private final PdfOperation pdfOperation = new PdfBoxApi();

  @Test
  public void givenADocumentAndPageToKeep_whenSplit_thenReturnsOnlyKeptPage()
    throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(2);

    SplitQuery splitQuery = new SplitQuery(
      Files.readAllBytes(new File("src/test/resources/file_types/pdf/multipage.pdf").toPath()),
      new PageOptions(pageNumberToKeep, PageOptionsOperation.KEEP_ONLY, 0));

    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(1, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndListOfPagesToKeep_whenSplit_thenReturnsOnlyKeptPages()
    throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);
    pageNumberToKeep.add(2);

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(new File("src/test/resources/file_types/pdf/multipage.pdf").toPath()),
        new PageOptions(pageNumberToKeep, PageOptionsOperation.KEEP_ONLY, 0)
    );
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(2, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndListOfPagesToRemove_whenSplit_thenReturnsOnlyNotRemovedPages()
    throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);
    pageNumberToKeep.add(2);
    pageNumberToKeep.add(3);

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(new File("src/test/resources/file_types/pdf/multipage.pdf").toPath()),
        new PageOptions(pageNumberToKeep, PageOptionsOperation.REMOVE, 0)
    );
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(9, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentOtherThantAPdf_whenSplit_mustFail()
    throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);
    pageNumberToKeep.add(2);
    pageNumberToKeep.add(3);

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(new File("src/test/resources/file_types/receipt.jpg").toPath()),
        new PageOptions(pageNumberToKeep, PageOptionsOperation.REMOVE, 0)
    );

    Assertions.assertThrows(
      MindeeException.class,
      () -> pdfOperation.split(splitQuery));
  }

  @Test
  public void givenADocumentAndListPagesToRemoveAndMinPagesCondition_whenSplit_mustNotRemovePages()
    throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(new File("src/test/resources/file_types/pdf/multipage_cut-2.pdf").toPath()),
        new PageOptions(pageNumberToKeep, PageOptionsOperation.REMOVE, 5)
    );
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(2, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndNegativeListPagesToKeep_whenSplit_thenReturnsOnlyKeptPages()
    throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);
    pageNumberToKeep.add(-2);
    pageNumberToKeep.add(-1);

    SplitQuery splitQuery = new SplitQuery(
        Files.readAllBytes(new File("src/test/resources/file_types/pdf/multipage.pdf").toPath()),
        new PageOptions(pageNumberToKeep, PageOptionsOperation.KEEP_ONLY, 0)
    );
    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assertions.assertNotNull(splitPdf);
    Assertions.assertNotNull(splitPdf.getFile());
    Assertions.assertEquals(3, splitPdf.getTotalPageNumber());
  }
}
