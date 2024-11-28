package com.mindee.product.fr.healthcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Health Card API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "french_healthcard", version = "1")
public class HealthCardV1
    extends Inference<HealthCardV1Document, HealthCardV1Document> {
}
