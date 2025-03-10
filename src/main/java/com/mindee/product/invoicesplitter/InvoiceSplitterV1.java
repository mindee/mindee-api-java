package com.mindee.product.invoicesplitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Invoice Splitter API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "invoice_splitter", version = "1")
public class InvoiceSplitterV1
    extends Inference<InvoiceSplitterV1Document, InvoiceSplitterV1Document> {
}
