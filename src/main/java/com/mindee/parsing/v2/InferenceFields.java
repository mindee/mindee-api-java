package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

/**
 * Inference fields map.
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InferenceFields extends HashMap<String, DynamicField> {
}
