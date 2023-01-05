package com.mindee;

import lombok.Getter;
import org.apache.pdfbox.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

@Getter
public final class DocumentToParse {

  private final byte[] file;
  private final String filename;

  public DocumentToParse(InputStream file, String filename) throws IOException {
    this.file = IOUtils.toByteArray(file);
    this.filename = filename;
  }

  public DocumentToParse(File file) throws IOException {
    this.file = Files.readAllBytes(file.toPath());
    this.filename = file.getName();
  }

  public DocumentToParse(byte[] fileAsByteArray, String filename) {
    this.file = fileAsByteArray;
    this.filename = filename;
  }

  public DocumentToParse(String fileAsBase64, String filename) {
    this.file = Base64.getDecoder().decode(fileAsBase64.getBytes());
    this.filename = filename;
  }
}
