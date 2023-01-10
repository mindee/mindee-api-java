package com.mindee.parsing.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The french identity card model for the v1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "idcard_fr", version = "1")
public class IdCardV1Inference
  extends Inference<IdCardV1PagePrediction, IdCardV1DocumentPrediction> {

}
