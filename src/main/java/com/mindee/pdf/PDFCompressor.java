package com.mindee.pdf;

import static com.mindee.input.InputSourceUtils.hasSourceText;
import static com.mindee.input.InputSourceUtils.isPdf;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 * PDF compression class.
 */
public class PDFCompressor {
  public static byte[] compressPdf(
      byte[] pdfData,
      Integer imageQuality,
      Boolean forceSourceTextCompression,
      Boolean disableSourceText
  ) throws IOException {
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
      System.out
        .println(
          "MINDEE WARNING: Found text inside of the provided PDF file. Compression operation aborted."
        );
      return pdfData;
    }
    try (PDDocument inputDoc = Loader.loadPDF(pdfData); PDDocument outputDoc = new PDDocument()) {

      var pdfRenderer = new PDFRenderer(inputDoc);

      for (int pageIndex = 0; pageIndex < inputDoc.getNumberOfPages(); pageIndex++) {
        var originalPage = inputDoc.getPage(pageIndex);
        PDRectangle originalPageSize = originalPage.getMediaBox();

        processPage(
          inputDoc,
          pageIndex,
          outputDoc,
          pdfRenderer.renderImageWithDPI(pageIndex, 72 * (imageQuality / 100f), ImageType.ARGB),
          (imageQuality / 100f),
          originalPageSize,
          disableSourceText
        );
      }

      byte[] docAsBytes = documentToBytes(outputDoc);
      outputDoc.close();
      return docAsBytes;
    }
  }

  public static byte[] compressPdf(
      byte[] pdfData,
      Integer imageQuality,
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

  private static byte[] documentToBytes(PDDocument document) throws IOException {
    var outputStream = new ByteArrayOutputStream();
    document.save(outputStream);
    return outputStream.toByteArray();
  }

  private static void processPage(
      PDDocument originalDocument,
      Integer pageIndex,
      PDDocument outputDoc,
      BufferedImage image,
      Float imageQuality,
      PDRectangle originalPageSize,
      Boolean disableSourceText
  ) throws IOException {
    var newPage = new PDPage(originalPageSize);
    outputDoc.addPage(newPage);

    var pdImage = JPEGFactory.createFromImage(outputDoc, image, imageQuality);

    try (var contentStream = new PDPageContentStream(outputDoc, newPage)) {
      addImageToPage(contentStream, pdImage, originalPageSize);
      extractAndAddText(originalDocument, contentStream, pageIndex, disableSourceText);
    }
  }

  private static void extractAndAddText(
      PDDocument inputDoc,
      PDPageContentStream contentStream,
      int pageIndex,
      boolean disableSourceText
  ) throws IOException {
    if (disableSourceText) {
      return;
    }

    PDFTextStripper stripper = new PDFTextStripper() {
      @Override
      protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        if (textPositions.isEmpty()) {
          return;
        }

        TextPosition firstPosition = textPositions.get(0);
        float fontSize = firstPosition.getFontSizeInPt();
        PDColor color = getGraphicsState().getNonStrokingColor();
        contentStream.beginText();
        contentStream.setFont(firstPosition.getFont(), fontSize);
        contentStream.setNonStrokingColor(convertToAwtColor(color));

        float x = firstPosition.getXDirAdj();
        float y = firstPosition.getPageHeight() - firstPosition.getYDirAdj();

        contentStream.newLineAtOffset(x, y);
        try {
          contentStream.showText(text);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
          contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
          contentStream.showText(text);
        }
        contentStream.endText();
      }
    };

    stripper.setStartPage(pageIndex + 1);
    stripper.setEndPage(pageIndex + 1);
    stripper.getText(inputDoc);
  }

  private static Color convertToAwtColor(PDColor pdColor) {
    float[] components = pdColor.getComponents();
    if (components.length == 1) {
      // Grayscale
      return new Color(components[0], components[0], components[0]);
    } else if (components.length == 3) {
      // RGB
      return new Color(components[0], components[1], components[2]);
    } else if (components.length == 4) {
      // CMYK (simplified conversion)
      float c = components[0];
      float m = components[1];
      float y = components[2];
      float k = components[3];
      float r = 1 - Math.min(1, c + k);
      float g = 1 - Math.min(1, m + k);
      float b = 1 - Math.min(1, y + k);
      return new Color(r, g, b);
    }
    return Color.BLACK;
  }

  private static void addImageToPage(
      PDPageContentStream contentStream,
      PDImageXObject pdImage,
      PDRectangle pageSize
  ) throws IOException {
    contentStream.drawImage(pdImage, 0, 0, pageSize.getWidth(), pageSize.getHeight());
  }
}
