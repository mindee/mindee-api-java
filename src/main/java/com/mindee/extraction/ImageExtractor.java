package com.mindee.extraction;

import com.mindee.MindeeException;
import com.mindee.geometry.Bbox;
import com.mindee.geometry.BboxUtils;
import com.mindee.geometry.Polygon;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.standard.PositionData;
import com.mindee.pdf.PDFUtils;
import com.mindee.pdf.PdfPageImage;
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

  /**
   * Init from a path.
   * @param filePath Path to the file.
   */
  public ImageExtractor(String filePath) throws IOException {
    this(new LocalInputSource(filePath));
  }

  /**
   * Init from a {@link LocalInputSource}.
   * @param source The local source.
   */
  public ImageExtractor(LocalInputSource source) throws IOException {
    this.filename = source.getFilename();
    this.pageImages = new ArrayList<>();

    if (source.isPdf()) {
      this.saveFormat = "jpg";
      List<PdfPageImage> pdfPageImages = PDFUtils.pdfToImages(source);
      for (PdfPageImage pdfPageImage : pdfPageImages) {
        this.pageImages.add(pdfPageImage.getImage());
      }
    } else {
      String[] splitName = InputSourceUtils.splitNameStrict(this.filename);
      this.saveFormat = splitName[1].toLowerCase();

      ByteArrayInputStream input = new ByteArrayInputStream(source.getFile());
      this.pageImages.add(ImageIO.read(input));
    }
  }

  /**
   * @return The number of pages in the file.
   */
  public int getPageCount() {
    return this.pageImages.size();
  }

  /**
   * Extract images from a list of fields having position data.
   * Use this when the input file is an image or a single-page PDF.
   * @param fields List of Fields to extract.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionData> List<ExtractedImage> extractImages(List<FieldT> fields) {
    return extractImages(fields, this.filename);
  }

  /**
   * Extract images from a list of fields having position data.
   * Use this when the input file is an image or a single-page PDF.
   * @param fields List of Fields to extract.
   * @param outputName The base output filename, must have an image extension.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionData> List<ExtractedImage> extractImages(List<FieldT> fields, String outputName) {
    if (this.getPageCount() > 1) {
      throw new MindeeException("Input file has more than one page, use the `extractPageImages` method instead.");
    }
    return extractFromPage(fields, 0, outputName);
  }

  /**
   * Extract images from a list of fields having position data.
   * Use this when the input file is a PDF with multiple pages.
   * @param fields List of Fields to extract.
   * @param pageIndex The page index to extract, begins at 0.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionData> List<ExtractedImage> extractPageImages(
      List<FieldT> fields,
      int pageIndex
  ) {
    return extractPageImages(fields, pageIndex, this.filename);
  }

  /**
   * Extract images from a list of fields having position data.
   * Use this when the input file is a PDF with multiple pages.
   * @param fields List of Fields to extract.
   * @param pageIndex The page index to extract, begins at 0.
   * @param outputName The base output filename, must have an image extension.
   * @return A list of {@link ExtractedImage}.
   */
  public <FieldT extends PositionData> List<ExtractedImage> extractPageImages(
      List<FieldT> fields,
      int pageIndex,
      String outputName
  ) {
    String filename;
    if (this.getPageCount() > 1) {
      String[] splitName = InputSourceUtils.splitNameStrict(outputName);
      filename = splitName[0] + "." + this.saveFormat;
    } else {
      filename = this.filename;
    }
    return extractFromPage(fields, pageIndex, filename);
  }

  private <FieldT extends PositionData> List<ExtractedImage> extractFromPage(
      List<FieldT> fields,
      int pageIndex,
      String outputName
  ) {
    String[] splitName = InputSourceUtils.splitNameStrict(outputName);
    String filename = String.format("%s_page-%3s.%s", splitName[0], pageIndex + 1, splitName[1])
        .replace(" ", "0");

    List<ExtractedImage> extractedImages = new ArrayList<>();
    for (int i = 0; i < fields.size(); i++) {
      ExtractedImage extractedImage = extractImage(fields.get(i), pageIndex, i+1, filename);
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
   * @param pageIndex The page index to extract, begins at 0.
   * @return The {@link ExtractedImage}, or <code>null</code> if the field does not have valid position data.
   */
  public <FieldT extends PositionData> ExtractedImage extractImage(
      FieldT field,
      int pageIndex,
      int index,
      String filename
  ) {
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
        + saveFormat;
    return new ExtractedImage(extractImage(bbox, pageIndex), fieldFilename, saveFormat);
  }

  /**
   * Extract an image from a field having position data.
   * @param field The field to extract.
   * @param index The index to use for naming the extracted image.
   * @param pageIndex The page index to extract, begins at 0.
   * @return The {@link ExtractedImage}, or <code>null</code> if the field does not have valid position data.
   */
  public <FieldT extends PositionData> ExtractedImage extractImage(FieldT field, int pageIndex, int index) {
    return extractImage(field, pageIndex, index, this.filename);
  }

  private BufferedImage extractImage(Bbox bbox, int pageIndex) {
    BufferedImage image = this.pageImages.get(pageIndex);
    int width = image.getWidth();
    int height = image.getHeight();
    int minX = (int) Math.round(bbox.getMinX() * width);
    int maxX = (int) Math.round(bbox.getMaxX() * width);
    int minY = (int) Math.round(bbox.getMinY() * height);
    int maxY = (int) Math.round(bbox.getMaxY() * height);
    return image.getSubimage(minX, minY, maxX - minX, maxY - minY);
  }
}
