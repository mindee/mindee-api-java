package com.mindee.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface DocumentParsingHttpClient {

  Map parse(InputStream inputStream, String filename, String apiKey, String endPoint)
      throws IOException;

}
