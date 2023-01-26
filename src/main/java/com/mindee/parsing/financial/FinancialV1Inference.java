package com.mindee.parsing.financial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The financial V1 inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "financial_document", version = "1")
public class FinancialV1Inference extends Inference<FinancialV1DocumentPrediction, FinancialV1DocumentPrediction> {

}
