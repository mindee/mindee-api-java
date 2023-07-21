package com.mindee.product.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Carte Nationale d'Identité, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "idcard_fr", version = "1")
public class IdCardV1
    extends Inference<IdCardV1Page, IdCardV1Document> {
}
