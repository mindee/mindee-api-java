package com.mindee.parsing.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.geometry.Polygon;
import com.mindee.parsing.standard.PositionData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class GeneratedObjectField implements PositionData {
  /**
   * List of all the values.
   */
  private final Map<String, Object> values;
  /**
   * List of all the values.
   */
  private final List<String> printableValues;
  @JsonProperty("page_id")
  private int pageId;

  /**
   * The confidence about the zone of the value extracted.
   * A value from 0 to 1.
   */
  @JsonProperty("confidence")
  private double confidence;
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
  public GeneratedObjectField(JsonNode node) {
    Map<String, Object> values = new HashMap<>();
    ArrayList<String> printableValues = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    JsonNode polygonNode = node.get("polygon");
    JsonNode quadrangleNode = node.get("quadrangle");
    JsonNode rectangleNode = node.get("rectangle");
    JsonNode boundingBoxNode = node.get("bounding_box");

    if (polygonNode != null) {
      this.polygon = objectMapper.convertValue(polygonNode, Polygon.class);
    }
    if (quadrangleNode != null) {
      this.quadrangle = objectMapper.convertValue(quadrangleNode, Polygon.class);
    }
    if (rectangle != null) {
      this.rectangle = objectMapper.convertValue(rectangleNode, Polygon.class);
    }
    if (boundingBox != null) {
      this.boundingBox = objectMapper.convertValue(boundingBoxNode, Polygon.class);
    }

    JsonNode pageIdNode = node.get("page_id");
    if (pageIdNode != null && !pageIdNode.isNull()){
      this.pageId = pageIdNode.asInt();
    }

    JsonNode rawValueNode = node.get("raw_value");
    if (rawValueNode != null && !rawValueNode.isNull()){
      this.rawValue = rawValueNode.toString();
    }

    for (Iterator<Map.Entry<String, JsonNode>> subNode = node.fields(); subNode.hasNext(); ) {
      Map.Entry<String, JsonNode> generatedObjectNode = subNode.next();
      JsonNode nodeValue = generatedObjectNode.getValue();
      String nodeKey = generatedObjectNode.getKey();

      if (Objects.equals(nodeKey, "page_id")) {
        values.put("pageId", nodeValue.isNull() ? null : nodeValue.asInt());
      } else if (!Arrays.asList(new String[] {"polygon", "rectangle", "quadrangle", "bounding_box", "confidence", "raw_value", "page_id"}).contains(nodeKey)){
        if (!nodeValue.isNull()) {
          values.put(nodeKey, nodeValue.toString());
        } else {
          values.put(nodeKey, null);
        }
        if (!nodeKey.equals("degree")) {
          printableValues.add(nodeKey);
        }
      }
    }
    this.values = values;
    this.printableValues = printableValues;
  }

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
}
