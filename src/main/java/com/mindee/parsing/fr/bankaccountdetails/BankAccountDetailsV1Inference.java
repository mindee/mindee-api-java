package com.mindee.parsing.fr.bankaccountdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for bank_account_details v1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "bank_account_details", version = "1")
public class BankAccountDetailsV1Inference
    extends Inference<BankAccountDetailsV1DocumentPrediction, BankAccountDetailsV1DocumentPrediction> {
}
