package com.mindee.pdf;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ExtractedPDFs extends ArrayList<ExtractedPDF> {
  public void saveAllToDisk(String outputPath) throws IOException {
    saveAllToDisk(Path.of(outputPath));
  }

  public void saveAllToDisk(Path outputPath) throws IOException {
    for (ExtractedPDF extractedPDF : this) {
      extractedPDF.writeToFile(outputPath);
    }
  }
}
