package com.mindee.parsing.us.bankcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The bank check V1 inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "bank_check", version = "1")
public class BankCheckV1Inference
    extends Inference<BankCheckV1DocumentPrediction, BankCheckV1DocumentPrediction> {
}
