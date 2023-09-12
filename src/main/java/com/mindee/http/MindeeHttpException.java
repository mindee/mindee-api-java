package com.mindee.http;

import com.mindee.MindeeException;
import lombok.Getter;

/**
 * Represent a Mindee exception.
 */
@Getter
public class MindeeHttpException extends MindeeException {
  /** Standard HTTP status code. */
  private final int statusCode;
  /** Error details. */
  private final String details;
  /** Error code (not HTTP code). */
  private final String code;

  public MindeeHttpException(int statusCode, String message, String details, String code) {
    super(message);
    this.statusCode = statusCode;
    this.details = details;
    this.code = code;
  }
}
