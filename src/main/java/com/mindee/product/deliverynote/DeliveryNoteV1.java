package com.mindee.product.deliverynote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Delivery note API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "delivery_notes", version = "1")
public class DeliveryNoteV1 extends Inference<DeliveryNoteV1Document, DeliveryNoteV1Document> {
}
