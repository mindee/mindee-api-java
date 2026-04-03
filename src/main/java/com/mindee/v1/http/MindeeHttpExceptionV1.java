package com.mindee.v1.http;

import com.mindee.MindeeException;
import lombok.Getter;

/**
 * Represent a Mindee exception.
 */
@Getter
public class MindeeHttpExceptionV1 extends MindeeException {
  /** Standard HTTP status code. */
  private final int statusCode;
  /** Error details. */
  private final String details;
  /** Error code (not HTTP code). */
  private final String code;

  public MindeeHttpExceptionV1(int statusCode, String message, String details, String code) {
    super(message);
    this.statusCode = statusCode;
    this.details = details;
    this.code = code;
  }

  public String toString() {
    String outStr = super.toString() + " - HTTP " + getStatusCode();
    if (!getDetails().isEmpty()) {
      outStr += " - " + getDetails();
    }
    return outStr;
  }
}
