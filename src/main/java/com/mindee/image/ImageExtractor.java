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
    this.filename = source.getFilename();

    if (source.isPDF()) {
      this.saveFormat = "jpg";
      var pdfPageImages = getPDFRasterizer().PDFToImages(source.getFile(), source.getFilename());
      for (PDFPageImage pdfPageImage : pdfPageImages) {
        this.pageImages.add(pdfPageImage.getImage());
      }
    } else {
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
   * @param pageId The page index to extract, begins at 0.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionDataField> ExtractedImages extractImagesFromPage(
      List<FieldT> fields,
      int pageId
  ) {
    return extractFromPage(fields, pageId, this.filename);
  }

  private <FieldT extends PositionDataField> ExtractedImages extractFromPage(
      List<FieldT> fields,
      int pageId,
      String outputName
  ) {
    var extractedImages = new ExtractedImages();
    for (int elementId = 0; elementId < fields.size(); elementId++) {
      ExtractedImage extractedImage = extractImage(
        fields.get(elementId),
        pageId,
        elementId,
        outputName
      );
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
   * @param elementId The index to use for naming the extracted image.
   * @param filename Name of the file.
   * @param pageId The page index to extract, begins at 0.
   * @return The {@link ExtractedImage}, or <code>null</code> if the field does not have valid
   * position data.
   */
  public <FieldT extends PositionDataField> ExtractedImage extractImage(
      FieldT field,
      int pageId,
      int elementId,
      String filename
  ) {
    String[] splitName = InputSourceUtils.splitNameStrict(filename);
    var polygon = field.getPolygon();
    if (polygon == null) {
      return null;
    }
    return new ExtractedImage(
      extractImage(polygon.getAsBbox(), pageId),
      String
        .format("%s_page-%3s-item-%3s.%s", splitName[0], pageId + 1, elementId + 1, this.saveFormat)
        .replace(" ", "0"),
      this.saveFormat,
      pageId,
      elementId
    );
  }

  /**
   * Extract a single image from a field having position data.
   *
   * @param <FieldT> Type of field (needs to support positioning data).
   * @param field The field to extract.
   * @param elementId The index to use for naming the extracted image.
   * @param pageId The 0-based page index to extract.
   * @return The {@link ExtractedImage}, or <code>null</code> if the field does not have valid
   * position data.
   */
  public <FieldT extends PositionDataField> ExtractedImage extractImage(
      FieldT field,
      int pageId,
      int elementId
  ) {
    return extractImage(field, pageId, elementId, this.filename);
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
