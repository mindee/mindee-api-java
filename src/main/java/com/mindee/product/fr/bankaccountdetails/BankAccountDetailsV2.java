package com.mindee.product.fr.bankaccountdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Bank Account Details API version 2 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "bank_account_details", version = "2")
public class BankAccountDetailsV2
    extends Inference<BankAccountDetailsV2Document, BankAccountDetailsV2Document> {
}
