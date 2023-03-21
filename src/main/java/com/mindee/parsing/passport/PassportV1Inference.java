package com.mindee.parsing.passport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The passport V1 inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "passport", version = "1")
public class PassportV1Inference extends Inference<PassportV1DocumentPrediction, PassportV1DocumentPrediction> {

}
