package com.mindee.product.generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Define a simple model to encapsulate data from any API.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneratedV1 extends Inference<GeneratedV1Document, GeneratedV1Document> {

}
