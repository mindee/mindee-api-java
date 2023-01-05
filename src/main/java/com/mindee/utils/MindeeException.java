package com.mindee.utils;

/**
 * Represent a Mindee exception.
 */
public class MindeeException extends RuntimeException {

  public MindeeException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public MindeeException(String errorMessage) {
    super(errorMessage);
  }
}
