package com.mindee.v1.product.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.v1.http.EndpointInfo;
import com.mindee.v1.parsing.common.Inference;
import lombok.Getter;

/**
 * Carte Nationale d'Identité API version 2 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "idcard_fr", version = "2")
public class IdCardV2 extends Inference<IdCardV2Page, IdCardV2Document> {
}
