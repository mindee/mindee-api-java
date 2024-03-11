package com.mindee.product.generated;

import com.mindee.parsing.common.Prediction;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GeneratedV1Prediction extends Prediction {
  private final Map<String, Object> fields = new HashMap<>();

  @Override
  public boolean isEmpty() {
    return fields.isEmpty();
  }
}
