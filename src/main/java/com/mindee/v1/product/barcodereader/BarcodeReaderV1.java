package com.mindee.v1.product.barcodereader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.v1.http.EndpointInfo;
import com.mindee.v1.parsing.common.Inference;
import lombok.Getter;

/**
 * Barcode Reader API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "barcode_reader", version = "1")
public class BarcodeReaderV1 extends Inference<BarcodeReaderV1Document, BarcodeReaderV1Document> {
}
