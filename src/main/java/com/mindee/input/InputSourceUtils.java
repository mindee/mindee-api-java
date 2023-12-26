package com.mindee.input;

import com.mindee.MindeeException;
import java.net.URL;

/**
 * Utilities for working with files.
 */
public class InputSourceUtils {

  private InputSourceUtils() {
  }

  private static final String WINDOWS_FILE_SEPARATOR = "\\";
  private static final String UNIX_FILE_SEPARATOR = "/";
  private static final String FILE_EXTENSION = ".";

  /**
   * Returns the file's extension.
   */
  public static String getFileExtension(String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("fileName must not be null!");
    }

    String extension = "";

    int indexOfLastExtension = fileName.lastIndexOf(FILE_EXTENSION);

    // check last file separator, windows and unix
    int lastSeparatorPosWindows = fileName.lastIndexOf(WINDOWS_FILE_SEPARATOR);
    int lastSeparatorPosUnix = fileName.lastIndexOf(UNIX_FILE_SEPARATOR);

    // takes the greater of the two values, which means last file separator
    int indexOflastSeparator = Math.max(lastSeparatorPosWindows, lastSeparatorPosUnix);

    // make sure the file extension appears after the last file separator
    if (indexOfLastExtension > indexOflastSeparator) {
      extension = fileName.substring(indexOfLastExtension + 1);
    }

    return extension;
  }

  /**
   * Split the filename into a name and an extension.
   * @param filename the filename to split.
   * @return first element is name, second is extension.
   */
  public static String[] splitNameStrict(String filename) throws MindeeException {
    String name;
    String extension;
    int index = filename.lastIndexOf(FILE_EXTENSION);
    if (index > 0) {
      extension = filename.substring(index + 1);
      name = filename.substring(0, index);
    } else {
      throw new MindeeException("File name must include a valid extension.");
    }
    return new String[]{name, extension};
  }

  /**
   * Returns true if the file is a PDF.
   */
  public static boolean isPdf(String fileName) {
    return getFileExtension(fileName).equalsIgnoreCase("pdf");
  }

  /**
   * Ensures the URL can be sent to the Mindee server.
   */
  public static void validateUrl(URL inputUrl) {
    if (!"https".equalsIgnoreCase(inputUrl.getProtocol())) {
      throw new MindeeException("Only HTTPS source URLs are allowed");
    }
  }
}
