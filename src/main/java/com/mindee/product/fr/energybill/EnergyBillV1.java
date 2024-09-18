package com.mindee.product.fr.energybill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Energy Bill API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "energy_bill_fra", version = "1")
public class EnergyBillV1
    extends Inference<EnergyBillV1Document, EnergyBillV1Document> {
}
