package com.mindee.product.fr.bankaccountdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Bank Account Details, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "bank_account_details", version = "1")
public class BankAccountDetailsV1
    extends Inference<BankAccountDetailsV1Document, BankAccountDetailsV1Document> {
}
