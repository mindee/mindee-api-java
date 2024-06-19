package com.mindee.extraction;

import static com.mindee.pdf.PDFUtils.mergePdfPages;

import com.mindee.MindeeException;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1Document;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDFExtractor {
  private final PDDocument sourcePdf;
  private final String filename;

  /**
   * Init from a path.
   *
   * @param filePath Path to the file.
   */
  public PDFExtractor(String filePath) throws IOException {
    this(new LocalInputSource(filePath));
  }

  /**
   * @return The number of pages in the file.
   */
  public int getPageCount() {
    return this.sourcePdf.getNumberOfPages();
  }

  /**
   * Init from a {@link LocalInputSource}.
   *
   * @param source The local source.
   */
  public PDFExtractor(LocalInputSource source) throws IOException {
    this.filename = source.getFilename();
    if (source.isPdf()) {
      this.sourcePdf = PDDocument.load(source.getFile());
    } else {
      try (PDDocument document = new PDDocument()) {
        PDPage page = new PDPage();
        document.addPage(page);
        BufferedImage bufferedImage = byteArrayToBufferedImage(source.getFile());
        PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
          // Draw the image at coordinates (x, y) with width and height
          contentStream.drawImage(pdImage, 100, 600, (float) pdImage.getWidth() / 2,
            (float) pdImage.getHeight() / 2);
        }
        this.sourcePdf = document;
      }

    }
  }

  public static BufferedImage byteArrayToBufferedImage(byte[] byteArray) throws IOException {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(byteArray)) {
      return ImageIO.read(stream);
    }
  }

  public List<ExtractedPDF> extractSubDocuments(List<List<Integer>> pageIndexes)
    throws IOException {
    List<ExtractedPDF> extractedPDFs = new ArrayList<>();

    for (List<Integer> pageIndexElement : pageIndexes) {
      if (pageIndexElement.isEmpty()) {
        throw new MindeeException("Empty indexes not allowed for extraction.");
      }
      String[] splitName = InputSourceUtils.splitNameStrict(filename);
      String fieldFilename = splitName[0]
        + "_"
        + String.format("_%3s", pageIndexes.get(0)).replace(" ", "0")
        + "-"
        + String.format("_%3s", pageIndexes.get(pageIndexes.size() - 1)).replace(" ", "0")
        + splitName[1];
      extractedPDFs.add(
        new ExtractedPDF(PDDocument.load(mergePdfPages(this.sourcePdf, pageIndexElement)),
          fieldFilename));
    }
    return extractedPDFs;
  }


  public List<ExtractedPDF> extractInvoices(
    List<InvoiceSplitterV1Document.PageIndexes> pageIndexes) throws IOException {


    List<List<Integer>> indexes = pageIndexes.stream()
      .map(InvoiceSplitterV1Document.PageIndexes::getPageIndexes)
      .collect(Collectors.toList());


    return extractSubDocuments(indexes);
  }


  public List<ExtractedPDF> extractInvoices(
    List<InvoiceSplitterV1Document.PageIndexes> pageIndexes, boolean strict)
    throws IOException {
    List<List<Integer>> correctPageIndexes = new ArrayList<>();
    if (!strict) {
      return extractInvoices(pageIndexes);
    }
    Double currentConfidence = pageIndexes.get(0).getConfidence();
    Iterator<InvoiceSplitterV1Document.PageIndexes> iterator = pageIndexes.iterator();
    List<Integer> currentList = new ArrayList<>();
    while (iterator.hasNext()) {
      InvoiceSplitterV1Document.PageIndexes pageIndex = iterator.next();
      Double confidence = pageIndex.getConfidence();
      List<Integer> pageList = pageIndex.getPageIndexes();

      if (confidence == 1.0) {
        currentList.addAll(pageList);
        currentConfidence = 1.0;
      } else if (confidence == 0.0 && currentConfidence == 0.0) {
        currentList.addAll(pageList);
      } else if (confidence == 0.0 && currentConfidence == 1.0) {
        correctPageIndexes.add(currentList);
        currentList = new ArrayList<>(pageList);
        currentConfidence = 0.0;
      }
    }
    if (!currentList.isEmpty()) {
      correctPageIndexes.add(currentList);
    }
    return extractSubDocuments(correctPageIndexes);
  }

}
