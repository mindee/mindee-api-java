package com.mindee.v2.parsing.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.v2.product.ProductAttributes;
import com.mindee.v2.search.models.ModelSearchResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Models search response.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Deprecated
@ProductAttributes(slug = "models")
public class SearchResponse extends ModelSearchResponse {

}
