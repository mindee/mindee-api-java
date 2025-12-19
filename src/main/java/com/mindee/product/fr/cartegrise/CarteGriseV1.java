package com.mindee.product.fr.cartegrise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Carte Grise API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "carte_grise", version = "1")
public class CarteGriseV1 extends Inference<CarteGriseV1Document, CarteGriseV1Document> {
}
