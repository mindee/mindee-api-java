package com.mindee.product.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Define a simple model to encapsulate data from a custom API builder model.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomV1 extends Inference<CustomV1Page, CustomV1Document> {

}
