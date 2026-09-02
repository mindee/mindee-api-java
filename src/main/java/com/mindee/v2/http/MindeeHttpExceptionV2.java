package com.mindee.v2.http;

import com.mindee.MindeeException;
import com.mindee.v2.parsing.error.ErrorResponse;
import lombok.Getter;

/**
 * Represent a Mindee exception.
 */
@Getter
public class MindeeHttpExceptionV2 extends MindeeException {
  /** Standard HTTP status code. */
  private final int status;

  /** Error response. */
  private final ErrorResponse response;

  public MindeeHttpExceptionV2(ErrorResponse response) {
    super(response.toString());
    this.response = response;
    this.status = response.getStatus();
  }

  public MindeeHttpExceptionV2(ErrorResponse response, Throwable cause) {
    super(response.toString(), cause);
    this.response = response;
    this.status = response.getStatus();
  }

  public String toString() {
    return response.toString();
  }
}
