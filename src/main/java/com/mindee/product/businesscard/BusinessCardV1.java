package com.mindee.product.businesscard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Business Card API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "business_card", version = "1")
public class BusinessCardV1
    extends Inference<BusinessCardV1Document, BusinessCardV1Document> {
}
