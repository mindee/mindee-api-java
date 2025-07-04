package com.mindee.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.MindeeException;
import com.mindee.MindeeSettingsV2;
import com.mindee.PredictOptionsV2;
import com.mindee.parsing.common.*;
import com.mindee.parsing.v2.AsyncInferenceResponse;
import com.mindee.parsing.v2.AsyncJobResponse;
import com.mindee.parsing.v2.CommonResponse;
import lombok.Builder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * HTTP Client class for the V2 API.
 */
public final class MindeeHttpApiV2 extends MindeeApiV2 {

  private static final ObjectMapper mapper = new ObjectMapper();
  private final Function<Endpoint, String> buildProductPredicBasetUrl = this::buildProductPredictBaseUrl;
  private final Function<String, String> buildWorkflowPredictBaseUrl = this::buildWorkflowPredictBaseUrl;
  private final Function<String, String> buildWorkflowExecutionBaseUrl = this::buildWorkflowExecutionUrl;
  /**
   * The MindeeSetting needed to make the api call.
   */
  private final MindeeSettingsV2 mindeeSettings;
  /**
   * The HttpClientBuilder used to create HttpClient objects used to make api calls over http.
   * Defaults to HttpClientBuilder.create().useSystemProperties()
   */
  private final HttpClientBuilder httpClientBuilder;
  /**
   * The function used to generate the synchronous API endpoint URL.
   * Only needs to be set if the api calls need to be directed through internal URLs.
   */
  private final Function<PredictOptionsV2, AsyncJobResponse> enqueuePost;
  private final Function<String, CommonResponse> urlFromEndpoint;

  public MindeeHttpApiV2(MindeeSettingsV2 mindeeSettings) {
    this(
        mindeeSettings,
        null,
        null,
        null,
        null,
        null,
        null
    );
  }

  @Builder
  private MindeeHttpApiV2(
      MindeeSettingsV2 mindeeSettings,
      HttpClientBuilder httpClientBuilder,
      Function<PredictOptionsV2, AsyncJobResponse> enqueuePost,
      Function<String, CommonResponse> getInferenceFromQueue
  ) {
    this.mindeeSettings = mindeeSettings;

    if (httpClientBuilder != null) {
      this.httpClientBuilder = httpClientBuilder;
    } else {
      this.httpClientBuilder = HttpClientBuilder.create().useSystemProperties();
    }
    // TODO
  }
}
