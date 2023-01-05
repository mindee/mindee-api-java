package com.mindee.parsing;

import com.mindee.ParseParameter;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.utils.MindeeException;

import java.io.IOException;

public interface MindeeApi {

  <T extends Inference> Document<T> predict(
    Class<T> clazz,
    ParseParameter parseParameter) throws MindeeException, IOException;
}
