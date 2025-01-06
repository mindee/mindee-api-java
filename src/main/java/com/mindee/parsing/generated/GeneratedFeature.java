package com.mindee.parsing.generated;

import com.mindee.MindeeException;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.BooleanField;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import lombok.Getter;

/**
 * <p>
 *   A generic feature which can represent any OTS Mindee return prediction.
 * </p>
 * <p>
 *   The Mindee API can return either lists or objects.
 *   Here we represent all features as a list, to simplify any code that interacts with this class.
 * </p>
 * <p>
 *   If you want, you can "cast" the raw hashmap into one of the standard Mindee fields:
 * </p>
 * <ul>
 *   <li>StringField - asStringField()</li>
 *   <li>AmountField - asAmountField()</li>
 *   <li>DateField - asDateField()</li>
 *   <li>ClassificationField - asClassificationField()</li>
 * </ul>
 * This will not work for any feature which is a list in the Mindee return.
 *
 */
@Getter
public class GeneratedFeature extends ArrayList<GeneratedObject> {
  private final boolean isList;

  /**
   * Whether the original feature is a list.
   * @param isList Whether the feature is a list.
   */
  public GeneratedFeature(boolean isList) {
    this.isList = isList;
  }

  /**
   * Represent the feature as a standard {@link StringField}.
   * Only works for non-list features.
   * @return An instance of a {@link StringField}.
   */
  public StringField asStringField() {
    if (this.isList) {
      throw new MindeeException("Cannot convert a list feature into a StringField.");
    }
    return this.get(0).asStringField();
  }

  /**
   * @return An instance of a {@link BooleanField}.
   */
  public BooleanField asBooleanField() {
    if (this.isList) {
      throw new MindeeException("Cannot convert a list feature into a BooleanField.");
    }
    return this.get(0).asBooleanField();
  }

  /**
   * Represent the feature as a standard {@link AmountField}.
   * Only works for non-list features.
   * @return An instance of a {@link AmountField}.
   */
  public AmountField asAmountField() {
    if (this.isList) {
      throw new MindeeException("Cannot convert a list feature into an AmountField.");
    }
    return this.get(0).asAmountField();
  }

  /**
   * Represent the feature as a standard {@link DateField}.
   * Only works for non-list features.
   * @return An instance of a {@link DateField}.
   */
  public DateField asDateField() {
    if (this.isList) {
      throw new MindeeException("Cannot convert a list feature into a DateField.");
    }
    return this.get(0).asDateField();
  }

  /**
   * Represent the feature as a standard {@link ClassificationField}.
   * Only works for non-list features.
   * @return An instance of a {@link ClassificationField}.
   */
  public ClassificationField asClassificationField() {
    if (this.isList) {
      throw new MindeeException("Cannot convert a list feature into a ClassificationField.");
    }
    return this.get(0).asClassificationField();
  }
}
