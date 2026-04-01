package com.mindee.v1.product.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.v1.http.EndpointInfo;
import com.mindee.v1.parsing.common.Inference;
import lombok.Getter;

/**
 * Invoice API version 4 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "invoices", version = "4")
public class InvoiceV4 extends Inference<InvoiceV4Document, InvoiceV4Document> {
}
