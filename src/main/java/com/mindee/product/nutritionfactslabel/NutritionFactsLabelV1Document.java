package com.mindee.product.nutritionfactslabel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.AmountField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Nutrition Facts Label API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutritionFactsLabelV1Document extends Prediction {

  /**
   * The amount of added sugars in the product.
   */
  @JsonProperty("added_sugars")
  protected NutritionFactsLabelV1AddedSugar addedSugars;
  /**
   * The amount of calories in the product.
   */
  @JsonProperty("calories")
  protected NutritionFactsLabelV1Calorie calories;
  /**
   * The amount of cholesterol in the product.
   */
  @JsonProperty("cholesterol")
  protected NutritionFactsLabelV1Cholesterol cholesterol;
  /**
   * The amount of dietary fiber in the product.
   */
  @JsonProperty("dietary_fiber")
  protected NutritionFactsLabelV1DietaryFiber dietaryFiber;
  /**
   * The amount of nutrients in the product.
   */
  @JsonProperty("nutrients")
  protected List<NutritionFactsLabelV1Nutrient> nutrients = new ArrayList<>();
  /**
   * The amount of protein in the product.
   */
  @JsonProperty("protein")
  protected NutritionFactsLabelV1Protein protein;
  /**
   * The amount of saturated fat in the product.
   */
  @JsonProperty("saturated_fat")
  protected NutritionFactsLabelV1SaturatedFat saturatedFat;
  /**
   * The number of servings in each box of the product.
   */
  @JsonProperty("serving_per_box")
  protected AmountField servingPerBox;
  /**
   * The size of a single serving of the product.
   */
  @JsonProperty("serving_size")
  protected NutritionFactsLabelV1ServingSize servingSize;
  /**
   * The amount of sodium in the product.
   */
  @JsonProperty("sodium")
  protected NutritionFactsLabelV1Sodium sodium;
  /**
   * The total amount of carbohydrates in the product.
   */
  @JsonProperty("total_carbohydrate")
  protected NutritionFactsLabelV1TotalCarbohydrate totalCarbohydrate;
  /**
   * The total amount of fat in the product.
   */
  @JsonProperty("total_fat")
  protected NutritionFactsLabelV1TotalFat totalFat;
  /**
   * The total amount of sugars in the product.
   */
  @JsonProperty("total_sugars")
  protected NutritionFactsLabelV1TotalSugar totalSugars;
  /**
   * The amount of trans fat in the product.
   */
  @JsonProperty("trans_fat")
  protected NutritionFactsLabelV1TransFat transFat;

  @Override
  public boolean isEmpty() {
    return (this.servingPerBox == null
      && this.servingSize == null
      && this.calories == null
      && this.totalFat == null
      && this.saturatedFat == null
      && this.transFat == null
      && this.cholesterol == null
      && this.totalCarbohydrate == null
      && this.dietaryFiber == null
      && this.totalSugars == null
      && this.addedSugars == null
      && this.protein == null
      && this.sodium == null
      && (this.nutrients == null || this.nutrients.isEmpty()));
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Serving per Box: %s%n", this.getServingPerBox()));
    outStr.append(String.format(":Serving Size:%n%s", this.getServingSize().toFieldList()));
    outStr.append(String.format(":Calories:%n%s", this.getCalories().toFieldList()));
    outStr.append(String.format(":Total Fat:%n%s", this.getTotalFat().toFieldList()));
    outStr.append(String.format(":Saturated Fat:%n%s", this.getSaturatedFat().toFieldList()));
    outStr.append(String.format(":Trans Fat:%n%s", this.getTransFat().toFieldList()));
    outStr.append(String.format(":Cholesterol:%n%s", this.getCholesterol().toFieldList()));
    outStr
      .append(String.format(":Total Carbohydrate:%n%s", this.getTotalCarbohydrate().toFieldList()));
    outStr.append(String.format(":Dietary Fiber:%n%s", this.getDietaryFiber().toFieldList()));
    outStr.append(String.format(":Total Sugars:%n%s", this.getTotalSugars().toFieldList()));
    outStr.append(String.format(":Added Sugars:%n%s", this.getAddedSugars().toFieldList()));
    outStr.append(String.format(":Protein:%n%s", this.getProtein().toFieldList()));
    outStr.append(String.format(":sodium:%n%s", this.getSodium().toFieldList()));
    String nutrientsSummary = "";
    if (!this.getNutrients().isEmpty()) {
      int[] nutrientsColSizes = new int[] { 13, 22, 10, 13, 6 };
      nutrientsSummary = String
        .format("%n%s%n  ", SummaryHelper.lineSeparator(nutrientsColSizes, "-"))
        + "| Daily Value "
        + "| Name                 "
        + "| Per 100g "
        + "| Per Serving "
        + "| Unit "
        + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(nutrientsColSizes, "="));
      nutrientsSummary += SummaryHelper.arrayToString(this.getNutrients(), nutrientsColSizes);
      nutrientsSummary += String
        .format("%n%s", SummaryHelper.lineSeparator(nutrientsColSizes, "-"));
    }
    outStr.append(String.format(":nutrients: %s%n", nutrientsSummary));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
