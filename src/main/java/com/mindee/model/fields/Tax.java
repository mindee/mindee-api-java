package com.mindee.model.fields;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
public class Tax extends BaseField {

  private final Double value;
  private final String code;
  private final Double rate;


  @Builder
  public Tax(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, Integer page, Double value, String code, Double rate) {
    super(reconstructed, rawValue, confidence, polygon, page);
    this.value = value;
    this.code = code;
    this.rate = rate;
  }

  public String getTaxSummary()
  {
    StringBuilder stringBuilder = new StringBuilder("");
    if(value!=null)
      stringBuilder.append(value);
    if(rate!=null){
      stringBuilder.append(" ");
      stringBuilder.append(rate);
      stringBuilder.append("%");
    }
    if(code!=null)
    {
      stringBuilder.append(" ");
      stringBuilder.append(code);
    }
    return stringBuilder.toString().trim();
  }
}
