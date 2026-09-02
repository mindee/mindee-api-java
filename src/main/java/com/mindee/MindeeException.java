package com.mindee;

/**
 * Represent a Mindee exception.
 */
public class MindeeException extends RuntimeException {

  public MindeeException(String message, Throwable cause) {
    super(message, cause);
  }

  public MindeeException(String message) {
    super(message);
  }
}
