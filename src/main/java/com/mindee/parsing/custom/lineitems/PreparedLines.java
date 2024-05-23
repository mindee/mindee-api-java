package com.mindee.parsing.custom.lineitems;

import java.util.List;
import lombok.Getter;

/**
 * An anchor and its initial lines.
 */
@Getter
public final class PreparedLines {
  private final Anchor anchor;
  private final List<Line> lines;

  /**
   * An anchor and its initial lines.
   */
  public PreparedLines(Anchor anchor, List<Line> lines) {
    this.anchor = anchor;
    this.lines = lines;
  }
}
