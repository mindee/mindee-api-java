package com.mindee.product.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Receipt API version 5 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "expense_receipts", version = "5")
public class ReceiptV5 extends Inference<ReceiptV5Document, ReceiptV5Document> {
}
