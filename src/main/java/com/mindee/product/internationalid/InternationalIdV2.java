package com.mindee.product.internationalid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for International ID, API version 2.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "international_id", version = "2")
public class InternationalIdV2
    extends Inference<InternationalIdV2Document, InternationalIdV2Document> {
}
