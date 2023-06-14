package com.mindee.parsing.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for expense_receipts v5.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "expense_receipts", version = "5")
public class ReceiptV5Inference
    extends Inference<ReceiptV5DocumentPrediction, ReceiptV5DocumentPrediction> {
}
