package com.mindee.pdf;

import com.mindee.MindeeException;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import lombok.Getter;

/**
 * A page in a PDF extracted as an image.
 */
@Getter
public class PdfPageImage {
  private final BufferedImage image;
  private final int originalIndex;
  private final String saveFormat;
  private final String originalFilename;

  public PdfPageImage(
      BufferedImage image,
      int originalIndex,
      String originalFilename,
      String saveFormat
  ) {
    this.image = image;
    this.originalIndex = originalIndex;
    this.saveFormat = saveFormat;
    this.originalFilename = originalFilename;
  }

  /**
   * Return the image in a format suitable for sending to MindeeClient for parsing.
   * 
   * @return an instance of {@link LocalInputSource}
   */
  public LocalInputSource asInputSource() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ImageIO.write(this.image, this.saveFormat, output);
    return new LocalInputSource(output.toByteArray(), this.getFilename());
  }

  /**
   * Write the image to a file.
   * Uses the default image format and filename.
   * 
   * @param outputPath the output directory (must exist).
   */
  public void writeToFile(String outputPath) throws IOException, MindeeException {
    Path imagePath = Paths.get(outputPath, this.getFilename());
    File outputfile = new File(imagePath.toString());
    ImageIO.write(this.image, this.saveFormat, outputfile);
  }

  /**
   * Generate a filename for the image.
   * 
   * @return An auto-generated filename String.
   */
  public String getFilename() {
    String[] splitName = InputSourceUtils.splitNameStrict(this.originalFilename);
    return splitName[0]
      + String.format("_pdf-%3s", this.originalIndex + 1).replace(" ", "0")
      + "."
      + this.saveFormat;
  }
}
