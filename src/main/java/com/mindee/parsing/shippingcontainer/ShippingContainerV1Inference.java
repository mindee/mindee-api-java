package com.mindee.parsing.shippingcontainer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The shipping container V1 inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "shipping_containers", version = "1")
public class ShippingContainerV1Inference
    extends Inference<ShippingContainerV1DocumentPrediction, ShippingContainerV1DocumentPrediction> {
}
