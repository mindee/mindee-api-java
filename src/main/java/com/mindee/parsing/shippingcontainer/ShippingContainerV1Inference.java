package com.mindee.parsing.shippingcontainer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
* The definition for shipping_containers v1.
*/
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "shipping_containers", version = "1")
public class ShippingContainerV1Inference
    extends Inference<ShippingContainerV1DocumentPrediction, ShippingContainerV1DocumentPrediction> {
}
