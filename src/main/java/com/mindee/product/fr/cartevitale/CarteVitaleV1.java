package com.mindee.product.fr.cartevitale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Carte Vitale, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "carte_vitale", version = "1")
public class CarteVitaleV1
    extends Inference<CarteVitaleV1Document, CarteVitaleV1Document> {
}
