package com.mindee.parsing.generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = GeneratedObjectFieldDeserializer.class)
public class GeneratedObjectField {
  /**
   * List of all the values.
   */
  private final Map<String, Object> values;
  /**
   * List of all the values.
   */
  private final List<String> printableValues;

  /**
   * ID of the page the object was found on.
   */
  @JsonProperty("page_id")
  private Integer pageId;

  /**
   * The confidence about the zone of the value extracted.
   * A value from 0 to 1.
   */
  @JsonProperty("confidence")
  private Double confidence;
  /**
   * Raw unprocessed value, as it was sent by the server.
   */
  @JsonProperty("raw_value")
  private String rawValue;
  /**
   * A polygon position.
   */
  @JsonProperty("polygon")
  private Polygon polygon;
  /**
   * A rectangle representation.
   */
  @JsonProperty("rectangle")
  private Polygon rectangle;
  /**
   * A quadrilateral representation.
   */
  @JsonProperty("quadrangle")
  private Polygon quadrangle;
  /**
   * A bounding box representation.
   */
  @JsonProperty("bounding_box")
  private Polygon boundingBox;

  /**
   * Returns <code>true</code> if there are no values.
   *
   * @return <code>true</code> if there are no values.
   */
  public boolean isEmpty() {
    return this.values.isEmpty();
  }

  /**
   * Get all the value contents.
   *
   * @return all the values as a list of strings.
   */
  public List<String> getContentsList() {
    return values.values().stream().map(Object::toString).collect(Collectors.toList());
  }

  /**
   * Get all the joined value contents.
   *
   * @param separator the separator to use between values.
   * @return all the values as a single string.
   */
  public String getContentsString(String separator) {
    return String.format("%s",
      values.values().stream().map(Object::toString).collect(Collectors.joining(separator)));
  }

  /**
   * Get a string representation of the object.
   *
   * @return String representation of the object.
   */
  @Override
  public String toString() {
    return strLevel(0);
  }

  /**
   * ReSTructured-compliant string representation.
   * <p>
   * Takes into account level of indentation & displays elements as list elements.
   *
   * @param level Level of indentation (times 2 spaces).
   * @return String ReSTructured-compliant string representation.
   */
  public String strLevel(int level) {
    StringBuilder indent = new StringBuilder();
    for (int i = 0; i < level; i++) {
      indent.append("  ");
    }
    StringBuilder outStr = new StringBuilder();
    for (String attr : printableValues) {
      Object value = this.values.get(attr);
      String strValue = value != null ? value.toString() : "";
      outStr.append("\n").append(indent).append(":").append(attr).append(": ").append(strValue);
    }
    return "\n" + indent + outStr.toString().trim();
  }

  /**
   * Checks whether a field is a custom object or not.
   *
   * @param strDict Input JsonNode to check.
   * @return boolean Whether the field is a custom object.
   */
  public static boolean isGeneratedObject(JsonNode strDict) {
    List<String> commonKeys = Arrays.asList(
      "value",
      "polygon",
      "rectangle",
      "page_id",
      "confidence",
      "quadrangle",
      "values",
      "raw_value"
    );
    Iterator<String> fieldNames = strDict.fieldNames();
    while (fieldNames.hasNext()) {
      String key = fieldNames.next();
      if (!commonKeys.contains(key)) {
        return true;
      }
    }
    return false;
  }
}
