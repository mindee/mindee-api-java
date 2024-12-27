package com.mindee.product.us.usmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * US Mail API version 3 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "us_mail", version = "3")
public class UsMailV3
    extends Inference<UsMailV3Document, UsMailV3Document> {
}
