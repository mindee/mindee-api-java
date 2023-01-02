package com.mindee.pdf;

import com.mindee.parsing.PageOptions;
import com.mindee.utils.MindeeException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class PdfBoxApi implements PdfOperation {

  @Override
  public SplitPdf split(SplitQuery splitQuery) throws IOException {

    if (!checkPdfOpen(splitQuery.getFile())) {
      throw new MindeeException("This document cannot be open and cannot be split.");
    }

    try (PDDocument originalDocument = PDDocument.load(splitQuery.getFile())) {
      try (PDDocument splitDocument = new PDDocument()) {
        int totalOriginalPages = countPages(splitQuery.getFile());

        if (totalOriginalPages < splitQuery.getPageOptions().getOnMinPages())
        {
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
    Set<Integer> pages = pageOptions
      .getPages().stream()
      .filter(x -> x > (numberOfPages) * (-1) && x <= (numberOfPages - 1))
      .map(x -> (numberOfPages + x) % numberOfPages)
      .collect(Collectors.toSet());
    List<Integer> allPages = IntStream.range(0, numberOfPages).boxed().collect(Collectors.toList());

    switch (pageOptions.getMode()) {
      case KEEP_ONLY_LISTED_PAGES:
        return new ArrayList<>(pages);
      case REMOVE_LISTED_PAGES:
        allPages.removeAll(pages);
        return allPages;
      default:
        return allPages;
    }
  }

  private boolean checkPdfOpen(byte[] documentFile) {
    boolean opens = false;
    try {
      PDDocument.load(documentFile).close();
      opens = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return opens;
  }

  private int countPages(byte[] documentFile) throws IOException {
      PDDocument document = PDDocument.load(documentFile);
      int pageCount = document.getNumberOfPages();
      document.close();
      return pageCount;
  }
}
