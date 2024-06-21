package com.mindee;

public class TestingUtilities {
  public static String getVersion(String rstStr) {
    int versionLineStartPos = rstStr.indexOf(":Product: ");
    int versionEndPos = rstStr.indexOf("\n", versionLineStartPos);

    String substring = rstStr.substring(versionLineStartPos, versionEndPos);
    int versionStartPos = substring.lastIndexOf(" v");

    return substring.substring(versionStartPos + 2);
  }

  public static String getId(String rstStr) {
    int idStartPos = rstStr.indexOf(":Mindee ID: ") + 12;
    int idEndPos = rstStr.indexOf("\n:Filename:");

    return rstStr.substring(idStartPos, idEndPos);
  }

  public static String getFileName(String rstStr) {
    int idStartPos = rstStr.indexOf(":Filename: ") + 11;
    int idEndPos = rstStr.indexOf("\n\nInference");

    return rstStr.substring(idStartPos, idEndPos);
  }
}

