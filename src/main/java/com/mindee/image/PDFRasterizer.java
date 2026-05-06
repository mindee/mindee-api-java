package com.mindee.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * Default PDF rasterization implementation.
 */
public class PDFRasterizer implements PDFRasterization {
  public List<PDFPageImage> PDFToImages(byte[] fileBytes, String filename) throws IOException {
    PDDocument document = Loader.loadPDF(fileBytes);
    var pdfRenderer = new PDFRenderer(document);
    List<PDFPageImage> pdfPageImages = new ArrayList<>();
    for (int i = 0; i < document.getNumberOfPages(); i++) {
      var imageBuffer = pdfPageToImageBuffer(i, document, pdfRenderer);
      pdfPageImages.add(new PDFPageImage(imageBuffer, i, filename, "jpg"));
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
}
