package com.mindee.http;

import com.mindee.MindeeException;
import lombok.Getter;

/**
 * Represent a Mindee exception.
 */
@Getter
public class MindeeHttpExceptionV2 extends MindeeException {
  /** Standard HTTP status code. */
  private final int status;
  /** Error details. */
  private final String detail;

  public MindeeHttpExceptionV2(int status,  String detail) {
    super(detail);
    this.status = status;
    this.detail = detail;
  }

  public String toString() {
    String outStr = super.toString() + " - HTTP " + getStatus();
    if (!getDetail().isEmpty()) {
      outStr += " - " + getDetail();
    }
    return outStr;
  }
}
