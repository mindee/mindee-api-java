package com.mindee.product.nutritionfactslabel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Nutrition Facts Label API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "nutrition_facts", version = "1")
public class NutritionFactsLabelV1
    extends Inference<NutritionFactsLabelV1Document, NutritionFactsLabelV1Document> {
}
