package com.mindee.pdf;

import com.mindee.input.LocalInputSource;
import java.io.IOException;
import java.nio.file.Files;
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
   * Write the PDF to a file.
   *
   * @param outputPath the output directory (must exist).
   * @throws IOException Throws if the file can't be accessed.
   */
  public void writeToFile(String outputPath) throws IOException {
    var pdfPath = Paths.get(outputPath, this.filename);
    Files.write(pdfPath, this.fileBytes);
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
