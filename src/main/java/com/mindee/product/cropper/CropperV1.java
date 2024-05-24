package com.mindee.product.cropper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Cropper API version 1 inference prediction.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "cropper", version = "1")
public class CropperV1
    extends Inference<CropperV1Page, CropperV1Document> {
}
