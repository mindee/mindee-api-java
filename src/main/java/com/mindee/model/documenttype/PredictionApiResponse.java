package com.mindee.model.documenttype;

import java.util.List;

public interface PredictionApiResponse<T extends DocumentLevelResponse,S extends PageLevelResponse> {
  public void setDocument(T document);
  public void setPages(List<S> pages);

}
