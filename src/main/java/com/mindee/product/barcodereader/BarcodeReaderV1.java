package com.mindee.product.barcodereader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Barcode Reader, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "barcode_reader", version = "1")
public class BarcodeReaderV1
    extends Inference<BarcodeReaderV1Document, BarcodeReaderV1Document> {
}
