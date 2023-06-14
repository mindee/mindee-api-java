package com.mindee.parsing.invoicesplitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The invoice splitter V1 inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "invoice_splitter", version = "1")
public class InvoiceSplitterV1Inference extends
    Inference<InvoiceSplitterV1DocumentPrediction, InvoiceSplitterV1DocumentPrediction> {

}
