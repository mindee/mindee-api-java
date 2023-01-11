package com.mindee;

import com.mindee.http.MindeeHttpApi;

public class MindeeClientInit {

  private MindeeClientInit() {}

  public static MindeeClient create() {
    return new MindeeClient(new MindeeHttpApi(new MindeeSettings()));
  }

  public static MindeeClient create(String apiKey) {
    MindeeSettings mindeeSettings;
    if (apiKey != null && !apiKey.trim().isEmpty()) {
      mindeeSettings = new MindeeSettings(apiKey);
    } else {
      mindeeSettings = new MindeeSettings();
    }

    return new MindeeClient(new MindeeHttpApi(mindeeSettings));
  }
}
