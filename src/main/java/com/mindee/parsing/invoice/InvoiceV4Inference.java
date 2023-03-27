package com.mindee.parsing.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The invoice V4 inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "invoices", version = "4")
public class InvoiceV4Inference
    extends Inference<InvoiceV4DocumentPrediction, InvoiceV4DocumentPrediction> {
}
