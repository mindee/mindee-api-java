package com.mindee.product.financialdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for financial_document v1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "financial_document", version = "1")
public class FinancialDocumentV1
    extends Inference<FinancialDocumentV1Document, FinancialDocumentV1Document> {
}
