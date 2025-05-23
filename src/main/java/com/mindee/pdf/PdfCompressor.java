package com.mindee.pdf;

import static com.mindee.input.InputSourceUtils.hasSourceText;
import static com.mindee.input.InputSourceUtils.isPdf;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * PDF compression class.
 */
public class PdfCompressor {
  public static byte[] compressPdf(
      byte[] pdfData, Integer imageQuality,
      Boolean forceSourceTextCompression, Boolean disableSourceText
  )
      throws IOException {
    if (!isPdf(pdfData)) {
      return pdfData;
    }

    if (forceSourceTextCompression == null) {
      forceSourceTextCompression = false;
    }
    if (disableSourceText == null) {
      disableSourceText = true;
    }
    if (!forceSourceTextCompression && hasSourceText(pdfData)) {
      System.out.println(
          "MINDEE WARNING: Found text inside of the provided PDF file. Compression operation aborted.");
      return pdfData;
    }
    try (PDDocument inputDoc = Loader.loadPDF(pdfData);
         PDDocument outputDoc = new PDDocument()) {

      PDFRenderer pdfRenderer = new PDFRenderer(inputDoc);

      for (int pageIndex = 0; pageIndex < inputDoc.getNumberOfPages(); pageIndex++) {
        PDPage originalPage = inputDoc.getPage(pageIndex);
        PDRectangle originalPageSize = originalPage.getMediaBox();

        processPage(inputDoc, pageIndex, outputDoc,
            pdfRenderer.renderImageWithDPI(pageIndex, 72 * (imageQuality / 100f), ImageType.ARGB),
            (imageQuality / 100f), originalPageSize, disableSourceText
        );
      }

      byte[] docAsBytes = PDFUtils.documentToBytes(outputDoc);
      outputDoc.close();
      return docAsBytes;
    }
  }

  public static byte[] compressPdf(
      byte[] pdfData, Integer imageQuality,
      Boolean forceSourceTextCompression
  ) throws IOException {
    return compressPdf(pdfData, imageQuality, forceSourceTextCompression, true);
  }

  public static byte[] compressPdf(byte[] pdfData, Integer imageQuality) throws IOException {
    return compressPdf(pdfData, imageQuality, false, true);
  }

  public static byte[] compressPdf(byte[] pdfData) throws IOException {
    return compressPdf(pdfData, 85, false, true);
  }

  private static void processPage(
      PDDocument originalDocument, Integer pageIndex,
      PDDocument outputDoc, BufferedImage image,
      Float imageQuality,
      PDRectangle originalPageSize, Boolean disableSourceText
  )
      throws IOException {
    PDPage newPage = new PDPage(originalPageSize);
    outputDoc.addPage(newPage);

    PDImageXObject pdImage = JPEGFactory.createFromImage(outputDoc, image, imageQuality);

    try (PDPageContentStream contentStream = new PDPageContentStream(outputDoc, newPage)) {
      PDFUtils.addImageToPage(contentStream, pdImage, originalPageSize);
      PDFUtils.extractAndAddText(originalDocument, contentStream, pageIndex, disableSourceText);
    }
  }
}
