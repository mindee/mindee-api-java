package com.example.product.balticinvoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Baltic Invoice API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "baltic_invoice", version = "1")
public class BalticInvoiceV1
    extends Inference<BalticInvoiceV1Document, BalticInvoiceV1Document> {
}
