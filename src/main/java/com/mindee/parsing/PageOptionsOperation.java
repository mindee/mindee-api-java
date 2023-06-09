package com.mindee.parsing;

/**
 * Possible page operations on a document.
 */
public enum PageOptionsOperation {
  /**
   * Keep only the specified pages, and remove all others.
   */
  KEEP_ONLY,
  /**
   * Remove the specified pages, and keep all others.
   */
  REMOVE
}
