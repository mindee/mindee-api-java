package com.mindee.v1.product.internationalid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.v1.http.EndpointInfo;
import com.mindee.v1.parsing.common.Inference;
import lombok.Getter;

/**
 * International ID API version 2 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "international_id", version = "2")
public class InternationalIdV2
    extends Inference<InternationalIdV2Document, InternationalIdV2Document> {
}
