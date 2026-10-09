package com.mindee.exceptions;

import com.mindee.MindeeException;

/**
 * Represent an invalid or malformed input.
 */
public class MindeeInputException extends MindeeException {

  /**
   * {@link Exception}
   */
  public MindeeInputException(String message) {
    super(message);
  }

  /**
   * {@link Exception}
   */
  public MindeeInputException(String message, Exception innerException) {
    super(message, innerException);
  }
}
