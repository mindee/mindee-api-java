package com.mindee.product.billoflading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Bill of Lading API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "bill_of_lading", version = "1")
public class BillOfLadingV1
    extends Inference<BillOfLadingV1Document, BillOfLadingV1Document> {
}
