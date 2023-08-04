package com.mindee.product.us.driverlicense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Driver License, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "us_driver_license", version = "1")
public class DriverLicenseV1
    extends Inference<DriverLicenseV1Page, DriverLicenseV1Document> {
}
