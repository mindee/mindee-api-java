package com.mindee.pdf;

import com.mindee.parsing.PageOptions;
import com.mindee.parsing.PageOptionsOperation;
import org.junit.Assert;
import org.junit.Test;

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
      Files.readAllBytes(new File("src/test/resources/data/pdf/multipage.pdf").toPath()),
      new PageOptions(pageNumberToKeep, PageOptionsOperation.KEEP_ONLY_LISTED_PAGES, 0));

    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assert.assertNotNull(splitPdf);
    Assert.assertNotNull(splitPdf.getFile());
    Assert.assertEquals(1, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndListOfPagesToKeep_whenSplit_thenReturnsOnlyKeptPages()
    throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);
    pageNumberToKeep.add(2);

    SplitQuery splitQuery = new SplitQuery(
      Files.readAllBytes(new File("src/test/resources/data/pdf/multipage.pdf").toPath()),
      new PageOptions(pageNumberToKeep, PageOptionsOperation.KEEP_ONLY_LISTED_PAGES, 0));

    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assert.assertNotNull(splitPdf);
    Assert.assertNotNull(splitPdf.getFile());
    Assert.assertEquals(2, splitPdf.getTotalPageNumber());
  }

  @Test
  public void givenADocumentAndListOfPagesToRemove_whenSplit_thenReturnsOnlyNotRemovedPages()
    throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);
    pageNumberToKeep.add(2);
    pageNumberToKeep.add(3);

    SplitQuery splitQuery = new SplitQuery(
      Files.readAllBytes(new File("src/test/resources/data/pdf/multipage.pdf").toPath()),
      new PageOptions(pageNumberToKeep, PageOptionsOperation.REMOVE_LISTED_PAGES, 0));

    SplitPdf splitPdf = pdfOperation.split(splitQuery);

    Assert.assertNotNull(splitPdf);
    Assert.assertNotNull(splitPdf.getFile());
    Assert.assertEquals(9, splitPdf.getTotalPageNumber());
  }
}