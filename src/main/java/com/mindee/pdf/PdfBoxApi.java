package com.mindee.pdf;

import com.mindee.MindeeException;
import com.mindee.input.PageOptions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * Allows performing various operations on PDFs.
 */
public final class PdfBoxApi implements PdfOperation {

  @Override
  public SplitPdf split(SplitQuery splitQuery) throws IOException {

    if (!checkPdfOpen(splitQuery.getFile())) {
      throw new MindeeException("This document cannot be open and cannot be split.");
    }

    try (PDDocument originalDocument = Loader.loadPDF(splitQuery.getFile())) {
      try (PDDocument splitDocument = new PDDocument()) {
        int totalOriginalPages = countPages(splitQuery.getFile());

        if (totalOriginalPages < splitQuery.getPageOptions().getOnMinPages()) {
          return new SplitPdf(splitQuery.getFile(), totalOriginalPages);
        }

        List<Integer> pageRange = getPageRanges(splitQuery.getPageOptions(), totalOriginalPages);

        pageRange.stream()
            .filter(i -> i < totalOriginalPages)
            .forEach(i -> splitDocument.addPage(originalDocument.getPage(i)));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
          splitDocument.save(outputStream);
          byte[] splitPdf = outputStream.toByteArray();
          return new SplitPdf(splitPdf, countPages(splitPdf));
        }
      }
    }
  }

  private List<Integer> getPageRanges(PageOptions pageOptions, Integer numberOfPages) {

    Set<Integer> pages = Optional.ofNullable(pageOptions.getPageIndexes())
        .map(Collection::stream)
        .orElseGet(Stream::empty)
        .filter(x -> x > (numberOfPages) * (-1) && x <= (numberOfPages - 1))
        .map(x -> (numberOfPages + x) % numberOfPages)
        .collect(Collectors.toSet());
    List<Integer> allPages = IntStream.range(0, numberOfPages).boxed().collect(Collectors.toList());

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

  private boolean checkPdfOpen(byte[] documentFile) {
    boolean opens = false;
    try {
      Loader.loadPDF(documentFile).close();
      opens = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return opens;
  }

  private int countPages(byte[] documentFile) throws IOException {
    PDDocument document = Loader.loadPDF(documentFile);
    int pageCount = document.getNumberOfPages();
    document.close();
    return pageCount;
  }
}
