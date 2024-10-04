package com.mindee.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

/**
 * Image compression class.
 */
public final class ImageCompressor {
  public static BufferedImage resize(
      BufferedImage inputImage, Integer newWidth,
      Integer newHeight
  ) {
    Image scaledImage = inputImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    BufferedImage outImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = outImage.createGraphics();
    g2d.drawImage(scaledImage, 0, 0, null);
    g2d.dispose();

    return outImage;
  }


  public static byte[] compressImage(
      byte[] imageData, Integer quality, Integer maxWidth,
      Integer maxHeight
  ) throws IOException {

    ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
    BufferedImage original = ImageIO.read(bis);
    ImageUtils.Dimensions dimensions =
        ImageUtils.calculateNewDimensions(original, maxWidth, maxHeight);
    return compressImage(original, quality, dimensions.width, dimensions.height);
  }

  public static byte[] compressImage(byte[] imageData, Integer quality, Integer finalWidth)
      throws IOException {
    return compressImage(imageData, quality, finalWidth, null);
  }

  public static byte[] compressImage(byte[] imageData, Integer quality) throws IOException {
    return compressImage(imageData, quality, null, null);
  }

  public static byte[] compressImage(byte[] imageData) throws IOException {
    return compressImage(imageData, null, null, null);
  }

  public static byte[] compressImage(
      BufferedImage original,
      Integer quality,
      Integer finalWidth,
      Integer finalHeight
  ) throws IOException {
    if (quality == null) {
      quality = 85;
    }
    BufferedImage resizedImage = resize(original, finalWidth, finalHeight);
    return encodeToJpegByteArray(resizedImage, (float) quality / 100f);
  }

  public static BufferedImage removeAlphaChannel(BufferedImage original) {
    if (original.getType() == BufferedImage.TYPE_INT_RGB) {
      return original;
    }
    BufferedImage newImage = new BufferedImage(
        original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics2D g = newImage.createGraphics();
    g.drawImage(original, 0, 0, null);
    g.dispose();
    return newImage;
  }

  public static byte[] encodeToJpegByteArray(
      BufferedImage image, float quality
  ) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
    ImageWriter writer = writers.next();

    ImageWriteParam params = writer.getDefaultWriteParam();
    params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    params.setCompressionQuality(quality);

    writer.setOutput(ImageIO.createImageOutputStream(outputStream));
    BufferedImage alphaCheckedImage = removeAlphaChannel(image);
    writer.write(null, new IIOImage(alphaCheckedImage, null, null), params);
    writer.dispose();

    return outputStream.toByteArray();
  }
}
