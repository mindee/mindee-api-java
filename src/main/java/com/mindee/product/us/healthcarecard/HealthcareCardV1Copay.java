package com.mindee.product.us.healthcarecard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Copayments for covered services.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthcareCardV1Copay extends BaseField implements LineItemField {

  /**
   * The price of the service.
   */
  @JsonProperty("service_fees")
  Double serviceFees;
  /**
   * The name of the service.
   */
  @JsonProperty("service_name")
  String serviceName;

  public boolean isEmpty() {
    return (
        serviceFees == null
        && (serviceName == null || serviceName.isEmpty())
      );
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put(
        "serviceFees",
        SummaryHelper.formatAmount(this.serviceFees)
    );
    printable.put("serviceName", SummaryHelper.formatForDisplay(this.serviceName, 20));
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-12s ", printable.get("serviceFees"))
      + String.format("| %-20s |", printable.get("serviceName"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Service Fees: %s", printable.get("serviceFees"))
      + String.format(", Service Name: %s", printable.get("serviceName"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put(
        "serviceFees",
        SummaryHelper.formatAmount(this.serviceFees)
    );
    printable.put("serviceName", SummaryHelper.formatForDisplay(this.serviceName, null));
    return printable;
  }
}
