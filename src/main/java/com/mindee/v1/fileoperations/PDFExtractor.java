package com.mindee.v1.fileoperations;

import com.mindee.input.LocalInputSource;
import com.mindee.pdf.BasePDFExtractor;
import com.mindee.pdf.ExtractedPDF;
import com.mindee.v1.product.invoicesplitter.InvoiceSplitterV1InvoicePageGroup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PDF extraction class.
 */
public class PDFExtractor extends BasePDFExtractor {

  /**
   * Init from a {@link LocalInputSource}.
   *
   * @param source The local source.
   * @throws IOException Throws if the file can't be accessed.
   */
  public PDFExtractor(LocalInputSource source) throws IOException {
    super(source);
  }

  /**
   * Extract invoices from the given page indexes (from an invoice-splitter prediction).
   *
   * @param pageIndexes List of page indexes.
   * @return a list of extracted files.
   * @throws IOException Throws if the file can't be accessed.
   */
  public List<ExtractedPDF> extractInvoices(
      List<InvoiceSplitterV1InvoicePageGroup> pageIndexes
  ) throws IOException {

    List<List<Integer>> indexes = pageIndexes
      .stream()
      .map(InvoiceSplitterV1InvoicePageGroup::getPageIndexes)
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
  public List<ExtractedPDF> extractInvoices(
      List<InvoiceSplitterV1InvoicePageGroup> pageIndexes,
      boolean strict
  ) throws IOException {
    var correctPageIndexes = new ArrayList<List<Integer>>();
    if (!strict) {
      return extractInvoices(pageIndexes);
    }
    var iterator = pageIndexes.iterator();
    var currentList = new ArrayList<Integer>();
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
