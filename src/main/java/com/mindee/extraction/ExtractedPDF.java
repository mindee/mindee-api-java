package com.mindee.extraction;

import com.mindee.MindeeException;
import com.mindee.input.LocalInputSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * An extracted sub-PDF.
 */
@Getter
public class ExtractedPDF {
  private final PDDocument pdf;
  private final String filename;

  public ExtractedPDF(PDDocument pdf, String filename) {
    this.pdf = pdf;
    this.filename = filename;
  }

  /**
   * Write the PDF to a file.
   *
   * @param outputPath the output directory (must exist).
   */
  public void writeToFile(String outputPath) throws IOException, MindeeException {
    Path pdfPath = Paths.get(outputPath, this.filename);
    File outputfile = new File(pdfPath.toString());
    this.pdf.save(outputfile);
  }

  /**
   * Return the file in a format suitable for sending to MindeeClient for parsing.
   *
   * @return an instance of {@link LocalInputSource}
   */
  public LocalInputSource asInputSource() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    this.pdf.save(output);
    return new LocalInputSource(output.toByteArray(), this.filename);
  }
}
