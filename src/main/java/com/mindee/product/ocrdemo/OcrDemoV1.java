package com.mindee.product.ocrdemo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * The definition for OCR Demo, API version 1.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "ocr_demo", version = "1")
public class OcrDemoV1
    extends Inference<OcrDemoV1Page, OcrDemoV1Document> {
}
