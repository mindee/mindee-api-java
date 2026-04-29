package com.mindee.pdf;

import com.mindee.MindeeException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
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
public class PDFCompressor implements PDFCompression {
  private final PDFInputOperator pdfInputOperator;

  public PDFCompressor() {
    this.pdfInputOperator = new PDFInputOperator();
  }

  @Override
  public byte[] compressPDF(
      byte[] fileBytes,
      Integer imageQuality,
      Boolean forceSourceTextCompression,
      Boolean disableSourceText
  ) throws IOException {
    if (!pdfInputOperator.isPDF(fileBytes)) {
      return fileBytes;
    }

    if (forceSourceTextCompression == null) {
      forceSourceTextCompression = false;
    }
    if (disableSourceText == null) {
      disableSourceText = true;
    }
    if (!forceSourceTextCompression && hasSourceText(fileBytes)) {
      System.out
        .println(
          "MINDEE WARNING: Found text inside of the provided PDF file. Compression operation aborted."
        );
      return fileBytes;
    }
    try (var inputDoc = Loader.loadPDF(fileBytes); PDDocument outputDoc = new PDDocument()) {

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

  /**
   * Returns true if the source PDF has source text inside. Returns false for images.
   *
   * @param fileBytes A byte array representing a PDF.
   * @return True if at least one character exists in one page.
   * @throws MindeeException if the file could not be read.
   */
  private boolean hasSourceText(byte[] fileBytes) {
    try {
      PDDocument document = Loader
        .loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(fileBytes)));
      var stripper = new PDFTextStripper();

      for (int i = 0; i < document.getNumberOfPages(); i++) {
        stripper.setStartPage(i + 1);
        stripper.setEndPage(i + 1);
        String pageText = stripper.getText(document);
        if (!pageText.trim().isEmpty()) {
          document.close();
          return true;
        }
      }
      document.close();
    } catch (IOException e) {
      return false;
    }
    return false;
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

        var firstPosition = textPositions.get(0);
        float fontSize = firstPosition.getFontSizeInPt();
        var color = getGraphicsState().getNonStrokingColor();
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
