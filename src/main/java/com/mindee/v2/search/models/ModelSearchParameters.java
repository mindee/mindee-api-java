package com.mindee.v2.search.models;

import com.mindee.v2.clientoptions.BaseSearchParameters;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Search for models within the organization linked to the API key.
 * All search filters are optional.
 * If no search filters are given, all models belonging to the organization are returned.
 * Results are paginated.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class ModelSearchParameters extends BaseSearchParameters<ModelSearchResponse> {
  /**
   * Filter models by partial name match, case-insensitive.
   */
  private final String name;

  /**
   * Filter by an exact model type.
   */
  private final String modelType;

  /**
   * Default constructor.
   */
  private ModelSearchParameters(String name, String modelType, Integer page, Integer perPage) {
    super(ModelSearchResponse.class, page, perPage);
    if ("".equals(name)) {
      throw new IllegalArgumentException("name cannot be an empty string.");
    }
    if (modelType != null && modelType.trim().isEmpty()) {
      throw new IllegalArgumentException("modelType cannot be whitespace");
    }

    this.name = name;
    this.modelType = modelType;
  }

  @Override
  public Map<String, String> getRequestParameters() {
    var parameters = new HashMap<>(super.getRequestParameters());

    if (getName() != null) {
      parameters.put("name", getName());
    }
    if (getModelType() != null) {
      parameters.put("model_type", getModelType());
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
     * Filter models by partial name match, case-insensitive.
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Filter by an exact model type.
     */
    public Builder modelType(String modelType) {
      this.modelType = modelType;
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
