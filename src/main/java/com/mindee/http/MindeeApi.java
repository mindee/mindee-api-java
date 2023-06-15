package com.mindee.http;

import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import java.io.IOException;

/**
 * Defines required methods for an API.
 */
abstract public class MindeeApi {

  /**
   * Get a document from the predict queue.
   */
  abstract public <DocT extends Inference> AsyncPredictResponse<DocT> documentQueueGet(
      Class<DocT> documentClass,
      Endpoint endpoint,
      String jobId
  );

  /**
   * Send a file to the prediction queue.
   */
  abstract public <DocT extends Inference> AsyncPredictResponse<DocT> predictAsyncPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException;

  /**
   * Send a file to the prediction API.
   */
  abstract public <DocT extends Inference> PredictResponse<DocT> predictPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException;

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
    } else if (osName.contains("freebsd")) {
      osName = "freebsd";
    } else if (osName.contains("aix")) {
      osName = "aix";
    }
    return String.format("mindee-api-java@v%s java-v%s %s", sdkVersion, javaVersion, osName);
  }

  protected boolean is2xxStatusCode(int statusCode) {
    return statusCode >= 200 && statusCode <= 299;
  }
}
