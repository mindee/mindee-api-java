package com.mindee.pdf;

import com.mindee.MindeeException;
import com.mindee.input.PageOptions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * Allows performing various operations on PDFs.
 */
public final class PDFInputOperator implements PDFInputOperation {

  @Override
  public SplitPDF split(byte[] fileBytes, PageOptions pageOptions) throws IOException {

    if (!isPDFOpen(fileBytes)) {
      throw new MindeeException("This document cannot be open and cannot be split.");
    }

    try (var originalDocument = Loader.loadPDF(fileBytes)) {
      try (var splitDocument = new PDDocument()) {
        int totalOriginalPages = getPageCount(fileBytes);

        if (totalOriginalPages < pageOptions.getOnMinPages()) {
          return new SplitPDF(fileBytes, totalOriginalPages);
        }

        var pageRange = getPageRanges(pageOptions, totalOriginalPages);
        pageRange
          .stream()
          .filter(i -> i < totalOriginalPages)
          .forEach(i -> splitDocument.addPage(originalDocument.getPage(i)));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
          splitDocument.save(outputStream);
          byte[] splitPdf = outputStream.toByteArray();
          return new SplitPDF(splitPdf, getPageCount(splitPdf));
        }
      }
    }
  }

  @Override
  public int getPageCount(byte[] fileBytes) throws IOException {
    var document = Loader.loadPDF(fileBytes);
    int pageCount = document.getNumberOfPages();
    document.close();
    return pageCount;
  }

  /**
   * Returns true if the file is a PDF.
   */
  @Override
  public boolean isPDF(byte[] fileBytes) {
    try {
      Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(fileBytes)));
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  private List<Integer> getPageRanges(PageOptions pageOptions, Integer numberOfPages) {

    Set<Integer> pages = Optional
      .ofNullable(pageOptions.getPageIndexes())
      .stream()
      .flatMap(Collection::stream)
      .filter(x -> x > (numberOfPages) * (-1) && x <= (numberOfPages - 1))
      .map(x -> (numberOfPages + x) % numberOfPages)
      .collect(Collectors.toSet());
    var allPages = IntStream.range(0, numberOfPages).boxed().collect(Collectors.toList());

    switch (pageOptions.getOperation()) {
      case KEEP_ONLY:
        return new ArrayList<>(pages);
      case REMOVE:
        allPages.removeAll(pages);
        return allPages;
      default:
        return allPages;
    }
  }

  private boolean isPDFOpen(byte[] fileBytes) {
    boolean opens = false;
    try {
      Loader.loadPDF(fileBytes).close();
      opens = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return opens;
  }
}
