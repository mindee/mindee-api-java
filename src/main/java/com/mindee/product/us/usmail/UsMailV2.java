package com.mindee.product.us.usmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * US Mail API version 2 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "us_mail", version = "2")
public class UsMailV2
    extends Inference<UsMailV2Document, UsMailV2Document> {
}
