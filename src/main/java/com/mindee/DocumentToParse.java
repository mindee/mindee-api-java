package com.mindee;

import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

@Getter
public final class DocumentToParse {

  private final InputStream file;
  private final String filename;

  public DocumentToParse(InputStream file, String filename) {
    this.file = file;
    this.filename = filename;
  }

  public DocumentToParse(File file) throws IOException {
    this.file = Files.newInputStream(file.toPath());
    this.filename = file.getName();
  }

  public DocumentToParse(byte[] fileAsByteArray, String filename) {
    this.file = new ByteArrayInputStream(fileAsByteArray);
    this.filename = filename;
  }

  public DocumentToParse(String fileAsBase64, String filename) {
    byte[] fileRead = Base64.getDecoder().decode(fileAsBase64.getBytes());

    this.file = new ByteArrayInputStream(fileRead);
    this.filename = filename;
  }
}
