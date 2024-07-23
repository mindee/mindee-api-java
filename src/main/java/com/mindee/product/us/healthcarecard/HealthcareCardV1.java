package com.mindee.product.us.healthcarecard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Healthcare Card API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "us_healthcare_cards", version = "1")
public class HealthcareCardV1
    extends Inference<HealthcareCardV1Document, HealthcareCardV1Document> {
}
