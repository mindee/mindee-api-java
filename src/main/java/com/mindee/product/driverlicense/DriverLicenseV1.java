package com.mindee.product.driverlicense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Driver License API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "driver_license", version = "1")
public class DriverLicenseV1
    extends Inference<DriverLicenseV1Document, DriverLicenseV1Document> {
}
