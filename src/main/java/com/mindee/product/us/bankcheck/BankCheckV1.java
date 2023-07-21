package com.mindee.product.us.bankcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Bank Check, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "bank_check", version = "1")
public class BankCheckV1
    extends Inference<BankCheckV1Page, BankCheckV1Document> {
}
