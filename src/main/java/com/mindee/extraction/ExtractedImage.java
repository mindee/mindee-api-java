package com.mindee.extraction;

import com.mindee.MindeeException;
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
 * An extracted sub-image.
 */
@Getter
public class ExtractedImage {
  private final BufferedImage image;
  private final String filename;
  private final String saveFormat;

  public ExtractedImage(BufferedImage image, String filename, String saveFormat) {
    this.image = image;
    this.filename = filename;
    this.saveFormat = saveFormat;
  }

  /**
   * Write the image to a file.
   * Uses the default image format and filename.
   * @param outputPath the output directory (must exist).
   */
  public void writeToFile(String outputPath) throws IOException, MindeeException {
    Path imagePath = Paths.get(outputPath, this.filename);
    File outputfile = new File(imagePath.toString());
    ImageIO.write(this.image, this.saveFormat, outputfile);
  }

  /**
   * Return the image in a format suitable for sending to MindeeClient for parsing.
   * @return an instance of {@link LocalInputSource}
   */
  public LocalInputSource asInputSource() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ImageIO.write(this.image, this.saveFormat, output);
    return new LocalInputSource(output.toByteArray(), this.filename);
  }
}
