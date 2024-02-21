package com.mindee.product.resume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The list of social network profiles of the candidate.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeV1SocialNetworksUrl extends BaseField implements LineItemField {

  /**
   * The name of the social network.
   */
  @JsonProperty("name")
  String name;
  /**
   * The URL of the social network.
   */
  @JsonProperty("url")
  String url;

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
    return String.format("| %-20s ", printable.get("name"))
      + String.format("| %-50s |", printable.get("url"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Name: %s", printable.get("name"))
      + String.format(", URL: %s", printable.get("url"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("name", SummaryHelper.formatForDisplay(this.name, 20));
    printable.put("url", SummaryHelper.formatForDisplay(this.url, 50));
    return printable;
  }
}
