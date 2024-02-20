package com.mindee.product.resume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for Resume, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "resume", version = "1")
public class ResumeV1
    extends Inference<ResumeV1Document, ResumeV1Document> {
}
