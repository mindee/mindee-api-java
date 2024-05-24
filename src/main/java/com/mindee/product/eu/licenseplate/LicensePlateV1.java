package com.mindee.product.eu.licenseplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * License Plate API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "license_plates", version = "1")
public class LicensePlateV1
    extends Inference<LicensePlateV1Document, LicensePlateV1Document> {
}
