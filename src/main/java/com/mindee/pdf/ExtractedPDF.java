package com.mindee.pdf;

import com.mindee.input.LocalInputSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;

/**
 * An extracted sub-PDF.
 */
@Getter
public class ExtractedPDF {
  private final byte[] fileBytes;
  private final String filename;

  /**
   * Default constructor.
   *
   * @param fileBytes PDF file as bytes.
   * @param filename Name of the extracted file.
   */
  public ExtractedPDF(byte[] fileBytes, String filename) {
    this.fileBytes = fileBytes;
    this.filename = filename;
  }

  /**
   * Write the extracted PDF to a file.
   *
   * @param outputPath the output path, it may be a file or a directory.
   * @throws IOException Throws if the file can't be accessed.
   */
  public void writeToFile(Path outputPath) throws IOException {
    if (Files.isDirectory(outputPath)) {
      outputPath = outputPath.resolve(this.filename);
    }
    Files.write(outputPath, this.fileBytes);
  }

  /**
   * Write the extracted PDF to a file.
   *
   * @param outputPath the output path, it may be a file or a directory.
   * @throws IOException Throws if the file can't be accessed.
   */
  public void writeToFile(String outputPath) throws IOException {
    writeToFile(Paths.get(outputPath));
  }

  /**
   * Return the file in a format suitable for sending to MindeeClient for parsing.
   *
   * @return an instance of {@link LocalInputSource}
   * @throws IOException Throws if the file can't be accessed.
   */
  public LocalInputSource asInputSource() throws IOException {
    return new LocalInputSource(this.fileBytes, this.filename);
  }
}
