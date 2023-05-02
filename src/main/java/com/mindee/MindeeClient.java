package com.mindee;

import com.mindee.parsing.CustomEndpoint;
import com.mindee.parsing.MindeeApi;
import com.mindee.parsing.PageOptions;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.custom.CustomV1Inference;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitQuery;
import com.mindee.utils.FileUtils;
import com.mindee.utils.MindeeException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Main entrypoint for Mindee operations.
 */
public class MindeeClient {

  private final MindeeApi mindeeApi;
  private final PdfOperation pdfOperation;

  public MindeeClient(PdfOperation pdfOperation, MindeeApi mindeeApi) {
    this.pdfOperation = pdfOperation;
    this.mindeeApi = mindeeApi;
  }

  public DocumentToParse loadDocument(
      InputStream fileStream,
      String fileName
  ) throws IOException {
    return new DocumentToParse(fileStream, fileName);
  }

  public DocumentToParse loadDocument(
      byte[] fileAsByteArray,
      String filename
  ) {
    return new DocumentToParse(fileAsByteArray, filename);
  }

  public DocumentToParse loadDocument(File file) throws IOException {
    return new DocumentToParse(file);
  }

  public DocumentToParse loadDocument(
      String fileInBase64Code,
      String filename
  ) {
    return new DocumentToParse(fileInBase64Code, filename);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      DocumentToParse documentToParse
  ) throws IOException {
    return this.parse(type, documentToParse.getFile(), documentToParse.getFilename(), false, null);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      DocumentToParse documentToParse,
      boolean includeWords
  ) throws IOException {
    return this.parse(type, documentToParse.getFile(), documentToParse.getFilename(), includeWords,
        null);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      DocumentToParse documentToParse,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(type, getSplitFile(documentToParse, pageOptions),
        documentToParse.getFilename(), false, null);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      DocumentToParse documentToParse,
      boolean includeWords,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(type, getSplitFile(documentToParse, pageOptions),
        documentToParse.getFilename(), includeWords, null);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      URL documentUrl
  ) throws IOException {
    validateUrl(documentUrl);
    return this.parse(type, null, null, false, documentUrl);
  }

  private <T extends Inference> Document<T> parse(
      Class<T> type,
      byte[] file,
      String filename,
      boolean includeWords,
      URL fileUrl
  ) throws IOException {
    return this.mindeeApi.predict(
        type,
        ParseParameter.builder()
            .file(file)
            .fileName(filename)
            .includeWords(includeWords)
            .fileUrl(fileUrl)
            .build());
  }

  public Document<CustomV1Inference> parse(
      DocumentToParse documentToParse,
      CustomEndpoint customEndpoint
  ) throws IOException {
    return this.parse(documentToParse.getFile(), documentToParse.getFilename(), customEndpoint,
        null);
  }

  public Document<CustomV1Inference> parse(
      URL documentUrl,
      CustomEndpoint customEndpoint
  ) throws IOException {
    validateUrl(documentUrl);
    return this.parse(null, null, customEndpoint, documentUrl);
  }

  public Document<CustomV1Inference> parse(
      DocumentToParse documentToParse,
      CustomEndpoint customEndpoint,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(getSplitFile(documentToParse, pageOptions), documentToParse.getFilename(),
        customEndpoint, null);
  }

  private Document<CustomV1Inference> parse(
      byte[] file,
      String filename,
      CustomEndpoint customEndpoint,
      URL fileUrl
  ) throws IOException {
    return this.mindeeApi.predict(
        CustomV1Inference.class,
        customEndpoint,
        ParseParameter.builder()
            .file(file)
            .fileName(filename)
            .fileUrl(fileUrl)
            .build());
  }

  private boolean validateUrl(URL documentUrl) {
    if (!"https".equalsIgnoreCase(documentUrl.getProtocol())) {
      throw new MindeeException("Only HTTPS document urls are allowed");
    }
    return true;
  }

  private byte[] getSplitFile(
      DocumentToParse documentToParse,
      PageOptions pageOptions
  ) throws IOException {
    byte[] splitFile;
    boolean isPDF = FileUtils.getFileExtension(documentToParse.getFilename())
        .equalsIgnoreCase("pdf");
    if (pageOptions == null || !isPDF) {
      splitFile = documentToParse.getFile();
    } else {
      splitFile = pdfOperation.split(
          new SplitQuery(documentToParse.getFile(), pageOptions)).getFile();
    }
    return splitFile;
  }
}
