package com.mindee.v2.search.ragdocuments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.parsing.search.BaseSearchResponse;
import com.mindee.v2.parsing.search.SearchRagDocuments;
import com.mindee.v2.product.ProductAttributes;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductAttributes(slug = "rag-documents")
public class RagDocumentSearchResponse extends BaseSearchResponse {
  @JsonProperty("rag_documents")
  private SearchRagDocuments ragDocuments;

  @Override
  protected List<String> bodyLines() {
    return List.of("RAG Documents\n#############\n", String.valueOf(ragDocuments));
  }
}
