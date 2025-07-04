package com.mindee.http;

import org.apache.hc.core5.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class MindeeApiCommon {

  protected String getUserAgent() {
    String javaVersion = System.getProperty("java.version");
    String sdkVersion = getClass().getPackage().getImplementationVersion();
    String osName = System.getProperty("os.name").toLowerCase();

    if (osName.contains("windows")) {
      osName = "windows";
    } else if (osName.contains("darwin")) {
      osName = "macos";
    } else if (osName.contains("mac")) {
      osName = "macos";
    } else if (osName.contains("linux")) {
      osName = "linux";
    } else if (osName.contains("bsd")) {
      osName = "bsd";
    } else if (osName.contains("aix")) {
      osName = "aix";
    }
    return String.format("mindee-api-java@v%s java-v%s %s", sdkVersion, javaVersion, osName);
  }

  protected boolean is2xxStatusCode(int statusCode) {
    return statusCode >= 200 && statusCode <= 299;
  }

  protected String readRawResponse(HttpEntity responseEntity) throws IOException {
    ByteArrayOutputStream contentRead = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    for (int length; (length = responseEntity.getContent().read(buffer)) != -1; ) {
      contentRead.write(buffer, 0, length);
    }
    return contentRead.toString("UTF-8");
  }
}
