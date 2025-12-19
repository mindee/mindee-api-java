package com.mindee.product.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Carte Nationale d'Identité API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "idcard_fr", version = "1")
public class IdCardV1 extends Inference<IdCardV1Page, IdCardV1Document> {
}
