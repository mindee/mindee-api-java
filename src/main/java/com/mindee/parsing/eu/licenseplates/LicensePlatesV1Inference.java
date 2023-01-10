package com.mindee.parsing.eu.licenseplates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The eu licence plates model for the v1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "license_plates", version = "1")
public class LicensePlatesV1Inference
  extends Inference<LicensePlatesV1DocumentPrediction, LicensePlatesV1DocumentPrediction> {

}
