package com.mindee.documentparser;

import com.mindee.http.Endpoint;
import com.mindee.model.documenttype.BaseDocumentResponse;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public final class DocumentConfig<T extends BaseDocumentResponse> {

  private final BiFunction<Class<T>, Map, T> converter;
  private final String documentType;
  private final String apiType;
  private final String singularName;
  private final String pluralName;
  @Singular
  private final List<Endpoint> endpoints;
  private final Function<T, T> builtInPostProcessing;


}
