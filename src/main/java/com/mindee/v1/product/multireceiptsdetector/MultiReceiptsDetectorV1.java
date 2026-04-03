package com.mindee.v1.product.multireceiptsdetector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.v1.http.EndpointInfo;
import com.mindee.v1.parsing.common.Inference;
import lombok.Getter;

/**
 * Multi Receipts Detector API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "multi_receipts_detector", version = "1")
public class MultiReceiptsDetectorV1
    extends Inference<MultiReceiptsDetectorV1Document, MultiReceiptsDetectorV1Document> {
}
