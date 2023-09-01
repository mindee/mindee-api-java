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
  private final String details;

  public MindeeHttpException(int statusCode, String message, String details) {
    super(message);
    this.statusCode = statusCode;
    this.details = details;
  }
}
