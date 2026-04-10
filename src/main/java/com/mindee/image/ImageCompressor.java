package com.mindee.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;

/**
 * Image compression class.
 */
public final class ImageCompressor {
  public static BufferedImage resize(
      BufferedImage inputImage,
      Integer newWidth,
      Integer newHeight
  ) {
    var scaledImage = inputImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    var outImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

    var g2d = outImage.createGraphics();
    g2d.drawImage(scaledImage, 0, 0, null);
    g2d.dispose();

    return outImage;
  }

  public static byte[] compressImage(
      byte[] imageData,
      Integer quality,
      Integer maxWidth,
      Integer maxHeight
  ) throws IOException {

    var bis = new ByteArrayInputStream(imageData);
    var original = ImageIO.read(bis);
    var dimensions = ImageUtils.calculateNewDimensions(original, maxWidth, maxHeight);
    return compressImage(original, quality, dimensions.width, dimensions.height);
  }

  public static byte[] compressImage(
      byte[] imageData,
      Integer quality,
      Integer finalWidth
  ) throws IOException {
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
    var resizedImage = resize(original, finalWidth, finalHeight);
    return encodeToJpegByteArray(resizedImage, (float) quality / 100f);
  }

  public static BufferedImage removeAlphaChannel(BufferedImage original) {
    if (original.getType() == BufferedImage.TYPE_INT_RGB) {
      return original;
    }
    var newImage = new BufferedImage(
      original.getWidth(),
      original.getHeight(),
      BufferedImage.TYPE_INT_RGB
    );
    var graphics = newImage.createGraphics();
    graphics.drawImage(original, 0, 0, null);
    graphics.dispose();
    return newImage;
  }

  public static byte[] encodeToJpegByteArray(
      BufferedImage image,
      float quality
  ) throws IOException {
    var outputStream = new ByteArrayOutputStream();

    var writers = ImageIO.getImageWritersByFormatName("jpg");
    var writer = writers.next();

    var params = writer.getDefaultWriteParam();
    params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    params.setCompressionQuality(quality);

    writer.setOutput(ImageIO.createImageOutputStream(outputStream));
    var alphaCheckedImage = removeAlphaChannel(image);
    writer.write(null, new IIOImage(alphaCheckedImage, null, null), params);
    writer.dispose();

    return outputStream.toByteArray();
  }
}
