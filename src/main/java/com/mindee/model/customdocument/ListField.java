package com.mindee.model.customdocument;

import java.util.List;

public class ListField {
  private String name;
  private Double confidence;
  private Boolean reconstructed;
  private Integer pageId;
  protected List<ListFieldValue> values;

  /**
   * @param name
   * @param confidence
   * @param reconstructed
   * @param pageId        the number of the page
   * @param values
   */
  public ListField(
      String name,
      Double confidence,
      Boolean reconstructed,
      Integer pageId,
      List<ListFieldValue> values) {
    this.name = name;
    this.confidence = confidence;
    this.reconstructed = reconstructed;
    this.pageId = pageId;
    this.values = values;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the possibleValues
   */
  public List<ListFieldValue> getValues() {
    return values;
  }

  /**
   * @return the confidence
   */
  public Double getConfidence() {
    return confidence;
  }

  /**
   * @return the reconstructed
   */
  public Boolean getReconstructed() {
    return reconstructed;
  }

  /**
   * @return the pageId
   */
  public Integer getPageId() {
    return pageId;
  }

}
