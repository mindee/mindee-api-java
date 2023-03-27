package com.mindee;

import com.mindee.http.MindeeHttpApi;
import com.mindee.parsing.MindeeApi;
import com.mindee.pdf.PdfBoxApi;

/**
 * Factory to get an instantiated MindeeClient.
 */
public class MindeeClientInit {

  private MindeeClientInit() {
  }

  /**
   * Create a default MindeeClient.
   */
  public static MindeeClient create() {
    return new MindeeClient(
      new PdfBoxApi(),
      createDefault(""));
  }

  /**
   * Create a default MindeeClient.
   *
   * @param apiKey The api key to use.
   */
  public static MindeeClient create(String apiKey) {

    return new MindeeClient(
      new PdfBoxApi(),
      createDefault(apiKey));
  }

  /**
   * Create a default MindeeApi.
   *
   * @param apiKey The api key to use.
   */
  public static MindeeApi createDefault(String apiKey) {

    MindeeSettings mindeeSettings;
    if (apiKey != null && !apiKey.trim().isEmpty()) {
      mindeeSettings = new MindeeSettings(apiKey);
    } else {
      mindeeSettings = new MindeeSettings();
    }

    return new MindeeHttpApi(mindeeSettings);
  }
}
