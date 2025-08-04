package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base class for V2 fields.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseField {
  /**
   * Confidence associated with the field.
   */
  @JsonProperty("confidence")
  private FieldConfidence confidence;

  /**
   * Field's location.
   */
  @JsonProperty("locations")
  private List<FieldLocation> locations;
}
