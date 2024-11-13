package com.mindee.product.ind.indianpassport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Passport - India API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "ind_passport", version = "1")
public class IndianPassportV1
    extends Inference<IndianPassportV1Document, IndianPassportV1Document> {
}
