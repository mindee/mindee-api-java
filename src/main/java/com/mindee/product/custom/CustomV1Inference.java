package com.mindee.product.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Define a simple model to encapsulate data from a custom API builder model.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomV1Inference extends Inference<CustomV1PagePrediction, CustomV1DocumentPrediction> {

}
