package com.mindee.v2.search.models;

import com.mindee.v2.clientoptions.BaseSearchParameters;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Search parameters for models.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class ModelSearchParameters extends BaseSearchParameters<ModelSearchResponse> {
  /**
   * Case-insensitive search term for the model name
   */
  private final String name;

  /**
   * Case-insensitive search term for the model type
   */
  private final String modelType;

  private ModelSearchParameters(String name, String modelType, Integer page, Integer perPage) {
    super(ModelSearchResponse.class, page, perPage);
    this.name = name;
    this.modelType = modelType;
  }

  @Override
  public Map<String, String> getRequestParameters() {
    var parameters = new HashMap<>(super.getRequestParameters());

    if (this.getName() != null && !this.getName().isEmpty()) {
      parameters.put("name", this.getName());
    }
    if (this.getModelType() != null && !this.getModelType().isEmpty()) {
      parameters.put("model_type", this.getModelType());
    }

    return parameters;
  }

  /**
   * Create a new builder.
   *
   * @return a fresh {@link Builder}
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Fluent builder for {@link ModelSearchParameters}.
   */
  public static final class Builder extends BaseSearchParameters.BaseBuilder<Builder> {
    private String name;
    private String modelType;

    Builder() {
    }

    /**
     * Case-insensitive search term for the model name
     */
    public Builder name(String name) {
      if (name != null && !name.isEmpty()) {
        this.name = name;
      }
      return this;
    }

    /**
     * Case-insensitive search term for the model type
     */
    public Builder modelType(String modelType) {
      if (modelType != null && !modelType.trim().isEmpty()) {
        this.modelType = modelType;
      }
      return this;
    }

    /**
     * Build an immutable {@link ModelSearchParameters} instance.
     */
    public ModelSearchParameters build() {
      return new ModelSearchParameters(this.name, this.modelType, this.page, this.perPage);
    }
  }
}
