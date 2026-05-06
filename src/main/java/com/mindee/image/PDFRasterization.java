package com.mindee.image;

import java.io.IOException;
import java.util.List;

/**
 * Rasterize a PDF into images.
 */
public interface PDFRasterization {
  /**
   * Rasterize a PDF into a list of images, one image per page.
   */
  List<PDFPageImage> PDFToImages(byte[] fileBytes, String filename) throws IOException;
}
