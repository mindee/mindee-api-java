package com.mindee.extraction;

import static com.mindee.pdf.PDFUtils.mergePdfPages;

import com.mindee.MindeeException;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1InvoicePageGroup;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * PDF extraction class.
 */
public class PDFExtractor {
  private final PDDocument sourcePdf;
  private final String filename;

  /**
   * Init from a path.
   *
   * @param filePath Path to the file.
   * @throws IOException Throws if the file can't be accessed.
   */
  public PDFExtractor(String filePath) throws IOException {
    this(new LocalInputSource(filePath));
  }

  /**
   * Init from a {@link LocalInputSource}.
   *
   * @param source The local source.
   * @throws IOException Throws if the file can't be accessed.
   */
  public PDFExtractor(LocalInputSource source) throws IOException {
    this.filename = source.getFilename();
    if (source.isPdf()) {
      this.sourcePdf = Loader.loadPDF(source.getFile());
    } else {
      PDDocument document = new PDDocument();
      PDPage page = new PDPage();
      document.addPage(page);
      BufferedImage bufferedImage = byteArrayToBufferedImage(source.getFile());
      PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
      try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
        contentStream.drawImage(pdImage, 100, 600, (float) pdImage.getWidth() / 2,
            (float) pdImage.getHeight() / 2);
      }
      this.sourcePdf = document;

    }
  }

  /**
   * @return The number of pages in the file.
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
  public List<ExtractedPDF> extractSubDocuments(List<List<Integer>> pageIndexes)
      throws IOException {
    List<ExtractedPDF> extractedPDFs = new ArrayList<>();

    for (List<Integer> pageIndexElement : pageIndexes) {
      if (pageIndexElement.isEmpty()) {
        throw new MindeeException("Empty indexes not allowed for extraction.");
      }
      String[] splitName = InputSourceUtils.splitNameStrict(filename);
      String fieldFilename =
          splitName[0] + String.format("_%3s", pageIndexElement.get(0) + 1).replace(" ", "0")
          + "-"
          + String.format("%3s", pageIndexElement.get(pageIndexElement.size() - 1) + 1)
            .replace(" ", "0") + "." + splitName[1];
      extractedPDFs.add(
        new ExtractedPDF(Loader.loadPDF(mergePdfPages(this.sourcePdf, pageIndexElement, false)),
          fieldFilename));
    }
    return extractedPDFs;
  }


  /**
   * Extract invoices from the given page indexes (from an invoice-splitter prediction).
   *
   * @param pageIndexes List of page indexes.
   * @return a list of extracted files.
   * @throws IOException Throws if the file can't be accessed.
   */
  public List<ExtractedPDF> extractInvoices(List<InvoiceSplitterV1InvoicePageGroup> pageIndexes)
      throws IOException {

    List<List<Integer>> indexes =
        pageIndexes.stream().map(InvoiceSplitterV1InvoicePageGroup::getPageIndexes)
        .collect(Collectors.toList());


    return extractSubDocuments(indexes);
  }


  /**
   * Extract invoices from the given page indexes (from an invoice-splitter prediction).
   *
   * @param pageIndexes List of page indexes.
   * @param strict Whether the extraction should strictly follow the confidence scores or not.
   * @return a list of extracted files.
   * @throws IOException Throws if the file can't be accessed.
   */
  public List<ExtractedPDF> extractInvoices(List<InvoiceSplitterV1InvoicePageGroup> pageIndexes,
                                            boolean strict) throws IOException {
    List<List<Integer>> correctPageIndexes = new ArrayList<>();
    if (!strict) {
      return extractInvoices(pageIndexes);
    }
    Iterator<InvoiceSplitterV1InvoicePageGroup> iterator = pageIndexes.iterator();
    List<Integer> currentList = new ArrayList<>();
    Double previousConfidence = null;
    while (iterator.hasNext()) {
      InvoiceSplitterV1InvoicePageGroup pageIndex = iterator.next();
      Double confidence = pageIndex.getConfidence();
      List<Integer> pageList = pageIndex.getPageIndexes();

      if (confidence == 1.0 && previousConfidence == null) {
        currentList = new ArrayList<>(pageList);
      } else if (confidence == 1.0) {
        correctPageIndexes.add(currentList);
        currentList = new ArrayList<>(pageList);
      } else if (confidence == 0.0 && !iterator.hasNext()) {
        currentList.addAll(pageList);
        correctPageIndexes.add(currentList);
      } else {
        correctPageIndexes.add(currentList);
        correctPageIndexes.add(pageList);
      }
      previousConfidence = confidence;
    }
    return extractSubDocuments(correctPageIndexes);
  }

}
