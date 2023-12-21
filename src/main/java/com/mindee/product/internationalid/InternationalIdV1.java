package com.mindee.product.internationalid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for International ID, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "international_id", version = "1")
public class InternationalIdV1
    extends Inference<InternationalIdV1Document, InternationalIdV1Document> {
}
