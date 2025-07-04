package com.mindee;

import com.mindee.input.LocalInputSource;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Parameters for the V2 “predict” endpoint.
 *
 * <p>This is a pure config object – no Jackson annotations are used.</p>
 */
@Getter
public final class PredictOptionsV2 {

  /**
   * Optional alias for the file.
   */
  private final String alias;

  /**
   * ID of the model.
   */
  private final String modelId;

  /**
   * If {@code true}, enable Retrieval-Augmented Generation.
   */
  private final boolean rag;

  /**
   * IDs of webhooks to propagate the API response to (may be empty).
   */
  private final List<String> webhookIds;

  /**
   * Local input source.
   */
  private final LocalInputSource localSource;

  public PredictOptionsV2(
      LocalInputSource localSource,
      String modelId,
      boolean rag,
      String alias,
      List<String> webhookIds
  ) {

    this.localSource = Objects.requireNonNull(localSource, "localSource must not be null");
    this.modelId = Objects.requireNonNull(modelId, "modelId must not be null");
    this.rag = rag;
    this.alias = alias;
    this.webhookIds = webhookIds == null ? Collections.emptyList()
        : webhookIds;
  }

  @Builder(builderMethodName = "builder")
  private static PredictOptionsV2 build(
      LocalInputSource localSource,
      String modelId,
      boolean rag,
      String alias,
      @Singular List<String> webhookIds
  ) {

    return new PredictOptionsV2(localSource, modelId, rag, alias, webhookIds);
  }
}
