package com.mindee.pdf;

import com.mindee.MindeeException;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * PDF extraction class.
 */
public class BasePDFExtractor {
  protected final PDDocument sourcePdf;
  protected final String filename;

  /**
   * Init from a {@link LocalInputSource}.
   *
   * @param source The local source.
   * @throws IOException Throws if the file can't be accessed.
   */
  protected BasePDFExtractor(LocalInputSource source) throws IOException {
    this.filename = source.getFilename();
    if (source.isPdf()) {
      this.sourcePdf = Loader.loadPDF(source.getFile());
    } else {
      var document = new PDDocument();
      var page = new PDPage();
      document.addPage(page);
      BufferedImage bufferedImage = byteArrayToBufferedImage(source.getFile());
      PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
      try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
        contentStream
          .drawImage(
            pdImage,
            100,
            600,
            (float) pdImage.getWidth() / 2,
            (float) pdImage.getHeight() / 2
          );
      }
      this.sourcePdf = document;
    }
  }

  /**
   * Get the number of pages in the PDF file.
   *
   * @return The number of pages in the PDF file.
   */
  public int getPageCount() {
    return sourcePdf.getNumberOfPages();
  }

  /**
   * Converts an array to a buffered image.
   *
   * @param byteArray Raw byte array.
   * @return a valid ImageIO buffer.
   * @throws IOException Throws if the file can't be accessed.
   */
  public static BufferedImage byteArrayToBufferedImage(byte[] byteArray) throws IOException {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(byteArray)) {
      return ImageIO.read(stream);
    }
  }

  /**
   * Given a list of page indexes, extracts the corresponding documents.
   *
   * @param pageIndexes List of page indexes.
   * @return A list of extracted files.
   * @throws IOException Throws if the file can't be accessed.
   */
  public List<ExtractedPDF> extractSubDocuments(
      List<List<Integer>> pageIndexes
  ) throws IOException {
    var extractedPDFs = new ArrayList<ExtractedPDF>();

    for (List<Integer> pageIndexElement : pageIndexes) {
      if (pageIndexElement.isEmpty()) {
        throw new MindeeException("Empty indexes not allowed for extraction.");
      }
      String[] splitName = InputSourceUtils.splitNameStrict(filename);
      String fieldFilename = splitName[0]
        + String.format("_%3s", pageIndexElement.get(0) + 1).replace(" ", "0")
        + "-"
        + String
          .format("%3s", pageIndexElement.get(pageIndexElement.size() - 1) + 1)
          .replace(" ", "0")
        + "."
        + splitName[1];
      extractedPDFs
        .add(
          new ExtractedPDF(
            Loader.loadPDF(mergePdfPages(this.sourcePdf, pageIndexElement, false)),
            fieldFilename
          )
        );
    }
    return extractedPDFs;
  }

  private static PDPage clonePage(PDPage page) {

    COSDictionary pageDict = page.getCOSObject();
    COSDictionary newPageDict = new COSDictionary(pageDict);

    newPageDict.removeItem(COSName.ANNOTS);

    return new PDPage(newPageDict);
  }

  private static byte[] createPdfFromExistingPdf(
      PDDocument document,
      List<Integer> pageNumbers,
      boolean closeOriginal
  ) throws IOException {
    var outputStream = new ByteArrayOutputStream();
    var newDocument = new PDDocument();
    int pageCount = document.getNumberOfPages();
    pageNumbers
      .stream()
      .filter(i -> i < pageCount)
      .forEach(i -> newDocument.addPage(clonePage(document.getPage(i))));

    newDocument.save(outputStream);
    newDocument.close();
    if (closeOriginal) {
      document.close();
    }

    byte[] output = outputStream.toByteArray();
    outputStream.close();
    return output;
  }

  /**
   * Merge specified PDF pages together.
   *
   * @param file The PDF file.
   * @param pageNumbers Lit of page numbers to merge together.
   */
  public static byte[] mergePdfPages(File file, List<Integer> pageNumbers) throws IOException {
    PDDocument document = Loader.loadPDF(file);
    return mergePdfPages(document, pageNumbers, true);
  }

  public static byte[] mergePdfPages(
      PDDocument document,
      List<Integer> pageNumbers
  ) throws IOException {
    return mergePdfPages(document, pageNumbers, true);
  }

  public static byte[] mergePdfPages(
      PDDocument document,
      List<Integer> pageNumbers,
      boolean closeOriginal
  ) throws IOException {
    return createPdfFromExistingPdf(document, pageNumbers, closeOriginal);
  }
}
