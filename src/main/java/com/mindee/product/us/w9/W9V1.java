package com.mindee.product.us.w9;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * W9 API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "us_w9", version = "1")
public class W9V1
    extends Inference<W9V1Page, W9V1Document> {
}
