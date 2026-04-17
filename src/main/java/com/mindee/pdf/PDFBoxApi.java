package com.mindee.pdf;

import com.mindee.MindeeException;
import com.mindee.input.PageOptions;
import java.awt.image.BufferedImage;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * Allows performing various operations on PDFs.
 */
public final class PDFBoxApi implements PDFOperation {

  @Override
  public SplitPDF split(byte[] fileBytes, PageOptions pageOptions) throws IOException {

    if (!checkPdfOpen(fileBytes)) {
      throw new MindeeException("This document cannot be open and cannot be split.");
    }

    try (var originalDocument = Loader.loadPDF(fileBytes)) {
      try (var splitDocument = new PDDocument()) {
        int totalOriginalPages = getNumberOfPages(fileBytes);

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
          return new SplitPDF(splitPdf, getNumberOfPages(splitPdf));
        }
      }
    }
  }

  @Override
  public int getNumberOfPages(byte[] fileBytes) throws IOException {
    var document = Loader.loadPDF(fileBytes);
    int pageCount = document.getNumberOfPages();
    document.close();
    return pageCount;
  }

  @Override
  public PdfPageImage pdfPageToImage(
      byte[] fileBytes,
      String filename,
      int pageNumber
  ) throws IOException {
    int index = pageNumber - 1;
    PDDocument document = Loader.loadPDF(fileBytes);
    var pdfRenderer = new PDFRenderer(document);
    BufferedImage imageBuffer = pdfPageToImageBuffer(index, document, pdfRenderer);
    document.close();
    return new PdfPageImage(imageBuffer, index, filename, "jpg");
  }

  @Override
  public List<PdfPageImage> pdfToImages(byte[] fileBytes, String filename) throws IOException {
    PDDocument document = Loader.loadPDF(fileBytes);
    var pdfRenderer = new PDFRenderer(document);
    List<PdfPageImage> pdfPageImages = new ArrayList<>();
    for (int i = 0; i < document.getNumberOfPages(); i++) {
      var imageBuffer = pdfPageToImageBuffer(i, document, pdfRenderer);
      pdfPageImages.add(new PdfPageImage(imageBuffer, i, filename, "jpg"));
    }
    document.close();
    return pdfPageImages;
  }

  private BufferedImage pdfPageToImageBuffer(
      int index,
      PDDocument document,
      PDFRenderer pdfRenderer
  ) throws IOException {
    PDRectangle bbox = document.getPage(index).getBBox();
    float dimension = bbox.getWidth() * bbox.getHeight();
    int dpi;
    if (dimension < 200000) {
      dpi = 300;
    } else if (dimension < 300000) {
      dpi = 250;
    } else {
      dpi = 200;
    }
    return pdfRenderer.renderImageWithDPI(index, dpi, ImageType.RGB);
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
}
