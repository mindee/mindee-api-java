package com.mindee.v2.product.crop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.http.ProductInfo;
import com.mindee.v2.parsing.CommonResponse;
import lombok.Getter;

/**
 * Response for a crop utility inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductInfo(slug = "crop")
public class CropResponse extends CommonResponse {

  /**
   * The inference result for a crop utility request.
   */
  @JsonProperty("inference")
  private CropInference inference;
}
