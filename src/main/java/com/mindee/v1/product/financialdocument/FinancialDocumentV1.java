package com.mindee.v1.product.financialdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.v1.http.EndpointInfo;
import com.mindee.v1.parsing.common.Inference;
import lombok.Getter;

/**
 * Financial Document API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "financial_document", version = "1")
public class FinancialDocumentV1
    extends Inference<FinancialDocumentV1Document, FinancialDocumentV1Document> {
}
