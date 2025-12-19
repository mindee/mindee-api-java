package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * File info for V2 API.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class InferenceFile {
  /**
   * File name.
   */
  @JsonProperty("name")
  private String name;

  /**
   * Optional file alias.
   */
  @JsonProperty("alias")
  private String alias;

  /**
   * Page Count.
   */
  @JsonProperty("page_count")
  private int pageCount;

  /**
   * Mime type, as read by the server.
   */
  @JsonProperty("mime_type")
  private String mimeType;

  public String toString() {

    StringJoiner joiner = new StringJoiner("\n");
    joiner
      .add("File")
      .add("====")
      .add(":Name: " + name)
      .add(":Alias:" + (alias != null ? " " + alias : ""))
      .add(":Page Count: " + pageCount)
      .add(":MIME Type: " + mimeType);
    return joiner.toString();
  }
}
