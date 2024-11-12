package com.mindee.product.ind.passportindia;

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
public class PassportIndiaV1
    extends Inference<PassportIndiaV1Document, PassportIndiaV1Document> {
}
