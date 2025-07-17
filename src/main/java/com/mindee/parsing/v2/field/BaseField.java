package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Base class for V2 fields.
 */
public abstract class BaseField {
  /**
   * Field's location.
   */
  @JsonProperty("locations")
  private List<FieldLocation> page;

  /**
   * Confidence associated with the field.
   */
  @JsonProperty("confidence")
  private FieldConfidence confidence;
}
