package com.mindee.product.generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.generated.GeneratedV1PredictionDeserializer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.util.Map;

/**
 * Document data for generated documents.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = GeneratedV1PredictionDeserializer.class)
public class GeneratedV1Document extends GeneratedV1Prediction {
  /**
   * Dictionary containing the fields of the document.
   */
  private Map<String, Object> fields;
}
