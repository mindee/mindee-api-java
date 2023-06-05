package com.mindee.parsing.financialdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for financial_document v1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "financial_document", version = "1")
public class FinancialDocumentV1Inference
    extends Inference<FinancialDocumentV1DocumentPrediction, FinancialDocumentV1DocumentPrediction> {
}
