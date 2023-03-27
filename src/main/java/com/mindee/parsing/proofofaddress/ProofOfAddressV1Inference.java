package com.mindee.parsing.proofofaddress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
* The definition for proof_of_address v1.
*/
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "proof_of_address", version = "1")
public class ProofOfAddressV1Inference
    extends Inference<ProofOfAddressV1DocumentPrediction, ProofOfAddressV1DocumentPrediction> {
}
