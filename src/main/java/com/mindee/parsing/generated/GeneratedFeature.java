package com.mindee.parsing.generated;

import com.mindee.MindeeException;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import lombok.Getter;
import java.util.ArrayList;

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
 *   <ul>
 *     <li>StringField <-- asStringField</li>
 *     <li>AmountField <-- asAmountField</li>
 *   </ul>
 *   This will not work for any feature which is a list in the Mindee return.
 * </p>
 */
@Getter
public class GeneratedFeature extends ArrayList<GeneratedObject> {
  private final boolean isList;

  public GeneratedFeature(boolean isList) {
    this.isList = isList;
  }

  public StringField asStringField() {
    if (this.isList) {
      throw new MindeeException("Cannot convert a list feature into a StringField.");
    }
    return this.get(0).asStringField();
  }

  public AmountField asAmountField() {
    if (this.isList) {
      throw new MindeeException("Cannot convert a list feature into an AmountField.");
    }
    return this.get(0).asAmountField();
  }

  public DateField asDateField() {
    if (this.isList) {
      throw new MindeeException("Cannot convert a list feature into a DateField.");
    }
    return this.get(0).asDateField();
  }
}
