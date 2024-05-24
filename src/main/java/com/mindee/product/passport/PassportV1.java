package com.mindee.product.passport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Passport API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "passport", version = "1")
public class PassportV1
    extends Inference<PassportV1Document, PassportV1Document> {
}
