package com.mindee.parsing.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The receipt V4 inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "expense_receipts", version = "4")
public class ReceiptV4Inference
    extends Inference<ReceiptV4DocumentPrediction, ReceiptV4DocumentPrediction> {
}
