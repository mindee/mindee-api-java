package com.mindee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;

public class TestingUtilities {
  public static Path getResourcePath(String filePath) {
    return Paths.get("src/test/resources/" + filePath);
  }

  public static Path getV1ResourcePath(String filePath) {
    return Paths.get("src/test/resources/v1/" + filePath);
  }

  public static Path getV2ResourcePath(String filePath) {
    return Paths.get("src/test/resources/v2/" + filePath);
  }

  public static String getV1ResourcePathString(String filePath) {
    return getV1ResourcePath(filePath).toString();
  }

  public static String readFileAsString(Path path) throws IOException {
    byte[] encoded = Files.readAllBytes(path);
    return new String(encoded);
  }

  public static void assertStringEqualsFile(String expected, String filePath) throws IOException {
    String[] actualLines = expected.split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(Paths.get(filePath));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  /**
   * Retrieves the version from an RST prediction output.
   *
   * @param rstStr An RST prediction output string.
   * @return The version.
   */
  public static String getVersion(String rstStr) {
    int versionLineStartPos = rstStr.indexOf(":Product: ");
    int versionEndPos = rstStr.indexOf("\n", versionLineStartPos);

    String substring = rstStr.substring(versionLineStartPos, versionEndPos);
    int versionStartPos = substring.lastIndexOf(" v");

    return substring.substring(versionStartPos + 2);
  }

  /**
   * Retrieves an ID from an RST prediction output string.
   *
   * @param rstStr An RST prediction output string.
   * @return The ID.
   */
  public static String getId(String rstStr) {
    int idStartPos = rstStr.indexOf(":Mindee ID: ") + 12;
    int idEndPos = rstStr.indexOf("\n:Filename:");

    return rstStr.substring(idStartPos, idEndPos);
  }

  /**
   * Retrieves a filename from an RST prediction output string.
   *
   * @param rstStr An RST output string.
   * @return The filename.
   */
  public static String getFileName(String rstStr) {
    int idStartPos = rstStr.indexOf(":Filename: ") + 11;
    int idEndPos = rstStr.indexOf("\n\nInference");

    return rstStr.substring(idStartPos, idEndPos);
  }

  /**
   * Compute the Levenshtein distance between two strings.
   * Taken & adapted from <a href=
   * "https://rosettacode.org/wiki/Levenshtein_distance#Iterative_space_optimized_(even_bounded)">here</a>
   *
   * @param refStr Source string to compare.
   * @param targetStr Target string to compare.
   * @return The Levenshtein distance between the two strings.
   */
  private static int levenshteinDistance(String refStr, String targetStr) {
    if (Objects.equals(refStr, targetStr)) {
      return 0;
    }
    int sourceLength = refStr.length();
    int targetLength = targetStr.length();
    if (sourceLength == 0) {
      return targetLength;
    }
    if (targetLength == 0) {
      return sourceLength;
    }
    if (sourceLength < targetLength) {
      int tempLength = sourceLength;
      sourceLength = targetLength;
      targetLength = tempLength;
      String tempString = refStr;
      refStr = targetStr;
      targetStr = tempString;
    }

    int[] distanceVector = new int[targetLength + 1];
    for (int i = 0; i <= targetLength; i++) {
      distanceVector[i] = i;
    }

    for (int i = 1; i <= sourceLength; i++) {
      distanceVector[0] = i;
      int previousDistance = i - 1;
      int minDistance = previousDistance;
      for (int j = 1; j <= targetLength; j++) {
        int currentDistance = previousDistance
          + (refStr.charAt(i - 1) == targetStr.charAt(j - 1) ? 0 : 1);
        distanceVector[j] = Math
          .min(
            Math.min(1 + (previousDistance = distanceVector[j]), 1 + distanceVector[j - 1]),
            currentDistance
          );
        if (previousDistance < minDistance) {
          minDistance = previousDistance;
        }
      }
    }
    return distanceVector[targetLength];
  }

  /**
   * Computes the Levenshtein ratio between two given strings.
   *
   * @param referenceString First string.
   * @param targetString Second string.
   * @return The ratio of similarities between the two strings.
   */
  public static double levenshteinRatio(String referenceString, String targetString) {
    int referenceLength = referenceString.length();
    int targetLength = targetString.length();
    int maxLength = Math.max(referenceLength, targetLength);

    if (referenceLength == 0 && targetLength == 0) {
      return 1.0;
    }

    return 1.0 - (double) levenshteinDistance(referenceString, targetString) / maxLength;
  }

}
