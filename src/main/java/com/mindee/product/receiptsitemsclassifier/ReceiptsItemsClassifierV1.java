package com.mindee.product.receiptsitemsclassifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Receipts Items Classifier, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "receipts_items_classifier", version = "1")
public class ReceiptsItemsClassifierV1
    extends Inference<ReceiptsItemsClassifierV1Document, ReceiptsItemsClassifierV1Document> {
}
