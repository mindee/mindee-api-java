package com.mindee.product.proofofaddress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for proof_of_address v1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "proof_of_address", version = "1")
public class ProofOfAddressV1
    extends Inference<ProofOfAddressV1Document, ProofOfAddressV1Document> {
}
