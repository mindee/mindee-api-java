package com.mindee.v2.clientoptions;

import com.mindee.v2.parsing.search.BaseSearchResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Data;

/**
 * Base parameters for searches.
 */
@Data
public abstract class BaseSearchParameters<TSearchResponse extends BaseSearchResponse> {
  private final Class<TSearchResponse> responseClass;

  /**
   * 1-based page index.
   */
  protected final Integer page;
  /**
   * Number of items per page.
   */
  protected final Integer perPage;

  /**
   * Base constructor.
   */
  protected BaseSearchParameters(
      Class<TSearchResponse> responseClass,
      Integer page,
      Integer perPage
  ) {
    this.responseClass = Objects.requireNonNull(responseClass, "responseClass cannot be null");
    if (page != null && page <= 0) {
      throw new IllegalArgumentException("page must be greater than 0");
    }
    if (perPage != null && perPage <= 0) {
      throw new IllegalArgumentException("perPage must be greater than 0");
    }

    this.page = page;
    this.perPage = perPage;
  }

  /**
   * Gets the request parameters for the search request.
   */
  public Map<String, String> getRequestParameters() {
    var parameters = new HashMap<String, String>();

    if (this.getPage() != null) {
      parameters.put("page", String.valueOf(getPage()));
    }
    if (this.getPerPage() != null) {
      parameters.put("per_page", String.valueOf(getPerPage()));
    }

    return parameters;
  }

  protected static abstract class BaseBuilder<T extends BaseBuilder<T>> {
    protected Integer page;
    protected Integer perPage;

    @SuppressWarnings("unchecked")
    protected T self() {
      return (T) this;
    }

    protected BaseBuilder() {
    }

    /**
     * 1-based page index.
     */
    public T page(Integer page) {
      this.page = page;
      return self();
    }

    /**
     * Number of items per page.
     */
    public T perPage(Integer perPage) {
      this.perPage = perPage;
      return self();
    }
  }

}
