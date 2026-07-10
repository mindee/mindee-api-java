package com.mindee.v2.http;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.MindeeException;
import com.mindee.v2.MindeeSettings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - HTTP API URL validation")
class MindeeHttpApiV2Test {

  private static MindeeHttpApiV2 apiWithBase(String baseUrl) {
    return MindeeHttpApiV2
      .builder()
      .mindeeSettings(new MindeeSettings("dummy-key", baseUrl))
      .build();
  }

  @Nested
  @DisplayName("validateInferenceUrl() – prevents Authorization leak")
  class ValidateInferenceUrl {
    private final MindeeHttpApiV2 api = apiWithBase("https://api-v2.mindee.net/v2");

    @Test
    void sameHostAndPath_isAccepted() {
      assertDoesNotThrow(
        () -> api.validateInferenceUrl("https://api-v2.mindee.net/v2/inferences/abc-123")
      );
    }

    @Test
    void differentHost_isRejected() {
      MindeeException e = assertThrows(
        MindeeException.class,
        () -> api.validateInferenceUrl("https://evil.example.com/v2/inferences/abc-123")
      );
      assertTrue(e.getMessage().contains("does not match"));
    }

    @Test
    void differentPort_isRejected() {
      assertThrows(
        MindeeException.class,
        () -> api.validateInferenceUrl("https://api-v2.mindee.net:8443/v2/inferences/abc-123")
      );
    }

    @Test
    void httpScheme_isRejected() {
      assertThrows(
        MindeeException.class,
        () -> api.validateInferenceUrl("http://api-v2.mindee.net/v2/inferences/abc-123")
      );
    }

    @Test
    void pathOutsideBase_isRejected() {
      MindeeException e = assertThrows(
        MindeeException.class,
        () -> api.validateInferenceUrl("https://api-v2.mindee.net/other/inferences/abc-123")
      );
      assertTrue(e.getMessage().contains("not under"));
    }

    @Test
    void embeddedUserInfo_isRejected() {
      assertThrows(
        MindeeException.class,
        () -> api.validateInferenceUrl("https://leak:leak@api-v2.mindee.net/v2/inferences/abc-123")
      );
    }

    @Test
    void relativeUrl_isRejected() {
      assertThrows(MindeeException.class, () -> api.validateInferenceUrl("/v2/inferences/abc-123"));
    }

    @Test
    void invalidUri_isRejected() {
      assertThrows(
        MindeeException.class,
        () -> api.validateInferenceUrl("https://api-v2.mindee.net/v2/inferences/{bad")
      );
    }

    @Test
    void hostCaseInsensitive_isAccepted() {
      assertDoesNotThrow(
        () -> api.validateInferenceUrl("https://API-V2.MINDEE.NET/v2/inferences/abc-123")
      );
    }

    @Test
    void customBaseUrl_isEnforced() {
      var customApi = apiWithBase("https://custom.mindee.internal/api");
      assertDoesNotThrow(
        () -> customApi.validateInferenceUrl("https://custom.mindee.internal/api/inferences/abc")
      );
      assertThrows(
        MindeeException.class,
        () -> customApi.validateInferenceUrl("https://api-v2.mindee.net/v2/inferences/abc")
      );
    }
  }
}
