package com.mindee.image;

import java.awt.image.BufferedImage;

public class ImageUtils {

  public static class Dimensions {
    public final Integer width;
    public final Integer height;

    public Dimensions(Integer width, Integer height) {
      this.width = width;
      this.height = height;
    }
  }

  public static Dimensions calculateNewDimensions(BufferedImage original, Integer maxWidth,
                                                  Integer maxHeight) {
    if (original == null) {
      throw new IllegalArgumentException("Generated image could not be processed for resizing.");
    }

    if (maxWidth == null && maxHeight == null) {
      return new Dimensions(original.getWidth(), original.getHeight());
    }

    double widthRatio =
      maxWidth != null ? (double) maxWidth / original.getWidth() : Double.POSITIVE_INFINITY;
    double heightRatio =
      maxHeight != null ? (double) maxHeight / original.getHeight() : Double.POSITIVE_INFINITY;

    double scaleFactor = Math.min(widthRatio, heightRatio);

    int newWidth = (int) (original.getWidth() * scaleFactor);
    int newHeight = (int) (original.getHeight() * scaleFactor);

    return new Dimensions(newWidth, newHeight);
  }
}
