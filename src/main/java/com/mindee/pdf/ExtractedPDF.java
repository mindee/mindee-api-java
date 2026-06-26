package com.mindee.pdf;

import com.mindee.MindeeException;
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
  /**
   * PDF content as bytes.
   */
  private final byte[] fileBytes;
  /**
   * Name of the file when writing to disk.
   */
  private final String filename;
  /**
   * 0-based indexes of all pages taken from the original PDF.
   */
  private final int[] pageIndexes;
  /**
   * The number of pages in this PDF file.
   */
  private final int pageCount;

  /**
   * Default constructor.
   *
   * @param fileBytes PDF file as bytes.
   * @param filename Name of the extracted file.
   * @param pageIndexes Two-element array: index of the first and last extracted page.
   */
  public ExtractedPDF(byte[] fileBytes, String filename, int[] pageIndexes) {
    this.fileBytes = fileBytes;
    this.filename = filename;
    this.pageIndexes = pageIndexes;
    this.pageCount = pageIndexes.length;
  }

  /**
   * Write the extracted PDF to a file.
   *
   * @param outputPath the output path, it may be a file or a directory.
   */
  public void writeToFile(Path outputPath) throws MindeeException {
    if (!Files.isDirectory(outputPath)) {
      throw new MindeeException("Provided path is not a directory.");
    }
    try {
      Files.write(outputPath.resolve(this.filename), this.fileBytes);
    } catch (IOException e) {
      throw new MindeeException("Could not save file " + this.filename + ".", e);
    }
  }

  /**
   * Write the extracted PDF to a file.
   *
   * @param outputPath the output path, it may be a file or a directory.
   */
  public void writeToFile(String outputPath) throws MindeeException {
    writeToFile(Paths.get(outputPath));
  }

  /**
   * Return the file in a format suitable for sending to MindeeClient for parsing.
   *
   * @return an instance of {@link LocalInputSource}
   */
  public LocalInputSource asInputSource() {
    return new LocalInputSource(this.fileBytes, this.filename);
  }
}
