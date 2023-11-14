package com.mindee.product.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Invoice, API version 4.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "invoices", version = "4")
public class InvoiceV4
    extends Inference<InvoiceV4Document, InvoiceV4Document> {
}
