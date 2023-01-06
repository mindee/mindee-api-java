package com.mindee.parsing.customdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Define a simple model to encapsulate data from a custom API builder model.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomDocumentInference extends Inference<CustomDocumentPrediction, CustomDocumentPrediction> {

}
