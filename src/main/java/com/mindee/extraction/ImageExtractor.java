package com.mindee.extraction;

import com.mindee.geometry.Bbox;
import com.mindee.geometry.BboxUtils;
import com.mindee.geometry.Polygon;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.standard.PositionData;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Extract sub-images from an image.
 */
public class ImageExtractor {
  private final BufferedImage bufferedImage;
  private final String filename;

  /**
   * Init from a path.
   * @param filePath Path to the file.
   */
  public ImageExtractor(String filePath) throws IOException {
    File file = new File(filePath);
    this.filename = file.getName();
    this.bufferedImage = ImageIO.read(file);
  }

  /**
   * Init from a {@link LocalInputSource}.
   * @param source The local source.
   */
  public ImageExtractor(LocalInputSource source) throws IOException {
    this.filename = source.getFilename();
    ByteArrayInputStream input = new ByteArrayInputStream(source.getFile());
    this.bufferedImage = ImageIO.read(input);
  }

  /**
   * Extract images from a list of fields having position data.
   * @param fields List of Fields to extract.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionData> List<ExtractedImage> extractImages(List<FieldT> fields) {
    return extractImages(fields, this.filename);
  }

  /**
   * Extract images from a list of fields having position data.
   * @param fields List of Fields to extract.
   * @param filename The base output filename.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionData> List<ExtractedImage> extractImages(List<FieldT> fields, String filename) {
    List<ExtractedImage> extractedImages = new ArrayList<>();
    for (int i = 0; i < fields.size(); i++) {
      ExtractedImage extractedImage = extractImage(fields.get(i), filename, i+1);
      if (extractedImage != null) {
        extractedImages.add(extractedImage);
      }
    }
    return extractedImages;
  }

  /**
   * Extract an image from a field having position data.
   * @param field The field to extract.
   * @param index The index to use for naming the extracted image.
   * @return The {@link ExtractedImage}, or <code>null</code> if the field does not have valid position data.
   */
  public <FieldT extends PositionData> ExtractedImage extractImage(FieldT field, String filename, int index) {
    String[] splitName = InputSourceUtils.splitNameStrict(filename);
    String saveFormat = splitName[1].toLowerCase();
    Polygon boundingBox = field.getBoundingBox();
    if (boundingBox == null) {
      return null;
    }
    Bbox bbox = BboxUtils.generate(boundingBox);
    String fieldFilename = splitName[0]
        + String.format("_%3s", index).replace(" ", "0")
        + "."
        + splitName[1];
    return new ExtractedImage(extractImage(bbox), fieldFilename, saveFormat);
  }

  /**
   * Extract an image from a field having position data.
   * @param field The field to extract.
   * @param index The index to use for naming the extracted image.
   * @return The {@link ExtractedImage}, or <code>null</code> if the field does not have valid position data.
   */
  public <FieldT extends PositionData> ExtractedImage extractImage(FieldT field, int index) {
    return extractImage(field, this.filename, index);
  }

  private BufferedImage extractImage(Bbox bbox) {
    int width = this.bufferedImage.getWidth();
    int height = this.bufferedImage.getHeight();
    int minX = (int) Math.round(bbox.getMinX() * width);
    int maxX = (int) Math.round(bbox.getMaxX() * width);
    int minY = (int) Math.round(bbox.getMinY() * height);
    int maxY = (int) Math.round(bbox.getMaxY() * height);
    return this.bufferedImage.getSubimage(minX, minY, maxX - minX, maxY - minY);
  }
}
