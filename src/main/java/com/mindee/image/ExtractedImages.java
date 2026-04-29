package com.mindee.image;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ExtractedImages extends ArrayList<ExtractedImage> {
  public void saveAllToDisk(String outputPath) throws IOException {
    saveAllToDisk(Path.of(outputPath));
  }

  public void saveAllToDisk(Path outputPath) throws IOException {
    for (ExtractedImage image : this) {
      image.writeToFile(outputPath);
    }
  }
}
