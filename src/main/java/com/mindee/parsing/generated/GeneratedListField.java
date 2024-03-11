package com.mindee.parsing.generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneratedListField {
  /** List of values */
  public final List<Object> values;
  /**
   * The index of the page where the current field was found.
   */
  @JsonProperty("page_id")
  private Integer pageId;
  /**
   * Get a list of contents.
   *
   * @return List of contents.
   */
  public List<String> getContentsList() {
    List<String> contentsList = new ArrayList<>();
    for (Object value : values) {
      contentsList.add(value.toString());
    }
    return contentsList;
  }

  /**
   * Get a string representation of all values.
   *
   * @param separator Separator to use when concatenating fields.
   * @return String representation of all values.
   */
  public String getContentsString(String separator) {
    return String.join(separator, getContentsList());
  }

  /**
   * Get a string representation of the object.
   *
   * @return String representation of the object.
   */
  @Override
  public String toString() {
    return getContentsString(" ");
  }
}
