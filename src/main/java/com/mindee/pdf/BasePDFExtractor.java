package com.mindee.pdf;

import com.mindee.MindeeException;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
  public BasePDFExtractor(LocalInputSource source) throws IOException {
    this.filename = source.getFilename();
    if (source.isPDF()) {
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

  public ExtractedPDF extractSinglePage(
      List<Integer> pageNumbers,
      boolean closeOriginal
  ) throws IOException {
    if (pageNumbers.isEmpty()) {
      throw new MindeeException("Empty indexes not allowed for extraction.");
    }
    var pdfBytes = createPdfFromExistingPdf(this.sourcePdf, pageNumbers, closeOriginal);
    return new ExtractedPDF(pdfBytes, makeFilename(pageNumbers));
  }

  /**
   * Given a list of page indexes, extracts the corresponding documents.
   *
   * @param pageIndexes List of page indexes.
   * @return A list of extracted files.
   * @throws IOException Throws if the file can't be accessed.
   */
  public ExtractedPDFs extractSubDocuments(List<List<Integer>> pageIndexes) throws IOException {
    var extractedPDFs = new ExtractedPDFs();

    for (List<Integer> pageIndexElement : pageIndexes) {
      extractedPDFs.add(extractSinglePage(pageIndexElement, false));
    }
    return extractedPDFs;
  }

  /**
   * Converts an array to a buffered image.
   *
   * @param byteArray Raw byte array.
   * @return a valid ImageIO buffer.
   * @throws IOException Throws if the file can't be accessed.
   */
  private static BufferedImage byteArrayToBufferedImage(byte[] byteArray) throws IOException {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(byteArray)) {
      return ImageIO.read(stream);
    }
  }

  /**
   * Make a nice filename for the split.
   */
  private String makeFilename(List<Integer> pageNumbers) {
    String[] splitName = InputSourceUtils.splitNameStrict(filename);
    return splitName[0]
      + String.format("_%3s", pageNumbers.get(0)).replace(" ", "0")
      + "-"
      + String.format("%3s", pageNumbers.get(pageNumbers.size() - 1)).replace(" ", "0")
      + "."
      + splitName[1];
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
}
