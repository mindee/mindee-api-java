package com.mindee.v2.product.classification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.v2.CommonResponse;
import com.mindee.v2.http.ProductInfo;
import lombok.Getter;

/**
 * Response for a classification utility inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductInfo(slug = "classification")
public class ClassificationResponse extends CommonResponse {

  /**
   * The inference result for a classification utility request.
   */
  @JsonProperty("inference")
  private ClassificationInference inference;
}
