package com.mindee.image;

import com.mindee.geometry.Bbox;
import com.mindee.geometry.PositionDataField;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Extract sub-images from an image.
 */
public class ImageExtractor {
  private final List<BufferedImage> pageImages;
  private final String filename;
  private final String saveFormat;

  public ImageExtractor(LocalInputSource source) throws IOException {

    this.pageImages = new ArrayList<>();

    if (source.isPDF()) {
      this.saveFormat = "jpg";
      var pdfPageImages = getPDFRasterizer().PDFToImages(source.getFile(), source.getFilename());
      for (PDFPageImage pdfPageImage : pdfPageImages) {
        this.pageImages.add(pdfPageImage.getImage());
      }
      this.filename = source.getFilename() + "." + this.saveFormat;
    } else {
      this.filename = source.getFilename();
      String[] splitName = InputSourceUtils.splitNameStrict(this.filename);
      this.saveFormat = splitName[1].toLowerCase();

      var input = new ByteArrayInputStream(source.getFile());
      this.pageImages.add(ImageIO.read(input));
    }
  }

  /**
   * Get the PDF rasterization implementation.
   * Override this method to provide custom PDF rasterization handling.
   *
   * @return The PDF rasterization implementation.
   */
  protected PDFRasterization getPDFRasterizer() {
    return new PDFRasterizer();
  }

  /**
   * Get the number of pages in the file.
   *
   * @return The number of pages in the file.
   */
  public int getPageCount() {
    return this.pageImages.size();
  }

  /**
   * Extract multiple images on a given page from a list of fields having position data.
   *
   * @param <FieldT> Type of field (needs to support positioning data).
   * @param fields List of Fields to extract.
   * @param pageIndex The page index to extract, begins at 0.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionDataField> ExtractedImages extractImagesFromPage(
      List<FieldT> fields,
      int pageIndex
  ) {
    return extractImagesFromPage(fields, pageIndex, this.filename);
  }

  /**
   * Extract multiple images on a given page from a list of fields having position data.
   *
   * @param <FieldT> Type of field (needs to support positioning data).
   * @param fields List of Fields to extract.
   * @param pageIndex The page index to extract, begins at 0.
   * @param outputName The base output filename, must have an image extension.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionDataField> ExtractedImages extractImagesFromPage(
      List<FieldT> fields,
      int pageIndex,
      String outputName
  ) {
    String filename;
    if (this.getPageCount() > 1) {
      String[] splitName = InputSourceUtils.splitNameStrict(outputName);
      filename = splitName[0] + "." + this.saveFormat;
    } else {
      filename = outputName;
    }
    return extractFromPage(fields, pageIndex, filename);
  }

  private <FieldT extends PositionDataField> ExtractedImages extractFromPage(
      List<FieldT> fields,
      int pageIndex,
      String outputName
  ) {
    String[] splitName = InputSourceUtils.splitNameStrict(outputName);
    var filename = String
      .format("%s_page-%3s.%s", splitName[0], pageIndex + 1, splitName[1])
      .replace(" ", "0");

    var extractedImages = new ExtractedImages();
    for (int i = 0; i < fields.size(); i++) {
      ExtractedImage extractedImage = extractImage(fields.get(i), pageIndex, i + 1, filename);
      if (extractedImage != null) {
        extractedImages.add(extractedImage);
      }
    }
    return extractedImages;
  }

  /**
   * Extract a single image from a field having position data.
   *
   * @param <FieldT> Type of field (needs to support positioning data).
   * @param field The field to extract.
   * @param index The index to use for naming the extracted image.
   * @param filename Name of the file.
   * @param pageIndex The page index to extract, begins at 0.
   * @return The {@link ExtractedImage}, or <code>null</code> if the field does not have valid
   * position data.
   */
  public <FieldT extends PositionDataField> ExtractedImage extractImage(
      FieldT field,
      int pageIndex,
      int index,
      String filename
  ) {
    String[] splitName = InputSourceUtils.splitNameStrict(filename);
    String saveFormat = splitName[1].toLowerCase();
    var polygon = field.getPolygon();
    if (polygon == null) {
      return null;
    }
    String fieldFilename = splitName[0]
      + String.format("_%3s", index).replace(" ", "0")
      + "."
      + saveFormat;
    return new ExtractedImage(
      extractImage(polygon.getAsBbox(), pageIndex),
      fieldFilename,
      saveFormat,
      pageIndex
    );
  }

  /**
   * Extract a single image from a field having position data.
   *
   * @param <FieldT> Type of field (needs to support positioning data).
   * @param field The field to extract.
   * @param index The index to use for naming the extracted image.
   * @param pageIndex The 0-based page index to extract.
   * @return The {@link ExtractedImage}, or <code>null</code> if the field does not have valid
   * position data.
   */
  public <FieldT extends PositionDataField> ExtractedImage extractImage(
      FieldT field,
      int pageIndex,
      int index
  ) {
    return extractImage(field, pageIndex, index, this.filename);
  }

  private BufferedImage extractImage(Bbox bbox, int pageIndex) {
    var image = this.pageImages.get(pageIndex);
    int width = image.getWidth();
    int height = image.getHeight();
    int minX = (int) Math.round(bbox.getMinX() * width);
    int maxX = (int) Math.round(bbox.getMaxX() * width);
    int minY = (int) Math.round(bbox.getMinY() * height);
    int maxY = (int) Math.round(bbox.getMaxY() * height);
    return image.getSubimage(minX, minY, maxX - minX, maxY - minY);
  }
}
