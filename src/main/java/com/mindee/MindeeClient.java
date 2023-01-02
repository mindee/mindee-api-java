package com.mindee;

import com.mindee.parsing.MindeeApi;
import com.mindee.parsing.PageOptions;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.pdf.PdfBoxApi;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitQuery;
import com.mindee.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MindeeClient {

  private final MindeeApi mindeeApi;
  private final PdfOperation pdfOperation;

  public MindeeClient(
    MindeeApi mindeeApi) {
    this.mindeeApi = mindeeApi;
    this.pdfOperation = new PdfBoxApi();
  }
  public MindeeClient(
    MindeeApi mindeeApi,
    PdfOperation pdfOperation) {
    this.mindeeApi = mindeeApi;
    this.pdfOperation = pdfOperation;
  }

  public DocumentToParse loadDocument(InputStream fileStream,
                                      String fileName) throws IOException {
    return new DocumentToParse(fileStream, fileName);
  }

  public DocumentToParse loadDocument(byte[] fileAsByteArray,
                                      String filename) {
    return new DocumentToParse(fileAsByteArray, filename);
  }

  public DocumentToParse loadDocument(File file) throws IOException {
    return new DocumentToParse(file);
  }

  public DocumentToParse loadDocument(String fileInBase64Code,
                                      String filename) {
    return new DocumentToParse(fileInBase64Code, filename);
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    DocumentToParse documentToParse) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .file(documentToParse.getFile())
        .fileName(documentToParse.getFilename())
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    DocumentToParse documentToParse,
    boolean includeWords) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .file(documentToParse.getFile())
        .fileName(documentToParse.getFilename())
        .includeWords(includeWords)
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    DocumentToParse documentToParse,
    PageOptions pageOptions) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .file(getSplitFile(documentToParse, pageOptions))
        .fileName(documentToParse.getFilename())
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    DocumentToParse documentToParse,
    boolean includeWords,
    PageOptions pageOptions) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .file(getSplitFile(documentToParse, pageOptions))
        .fileName(documentToParse.getFilename())
        .includeWords(includeWords)
        .build());
  }

  private byte[] getSplitFile(DocumentToParse documentToParse, PageOptions pageOptions) throws IOException {
    byte[] splitFile;

    if (FileUtils.getFileExtension(documentToParse.getFilename())
      .equalsIgnoreCase("pdf")) {
      splitFile = pdfOperation.split(
        new SplitQuery(documentToParse.getFile(), pageOptions)).getFile();
    } else {
      splitFile = documentToParse.getFile();
    }

    return splitFile;
  }
}
