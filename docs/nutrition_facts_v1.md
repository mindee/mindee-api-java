---
title: Nutrition Facts Label OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-nutrition-facts-label-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Nutrition Facts Label API](https://platform.mindee.com/mindee/nutrition_facts).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/nutrition_facts/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Nutrition Facts Label sample](https://github.com/mindee/client-lib-test-data/blob/main/products/nutrition_facts/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.nutritionfactslabel.NutritionFactsLabelV1;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {

  public static void main(String[] args) throws IOException, InterruptedException {
    String apiKey = "my-api-key";
    String filePath = "/path/to/the/file.ext";

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient(apiKey);

    // Load a file from disk
    LocalInputSource inputSource = new LocalInputSource(new File(filePath));

    // Parse the file asynchronously
    AsyncPredictResponse<NutritionFactsLabelV1> response = mindeeClient.enqueueAndParse(
        NutritionFactsLabelV1.class,
        inputSource
    );

    // Print a summary of the response
    System.out.println(response.toString());

    // Print a summary of the predictions
//  System.out.println(response.getDocumentObj().toString());

    // Print the document-level predictions
//    System.out.println(response.getDocumentObj().getInference().getPrediction().toString());

    // Print the page-level predictions
//    response.getDocumentObj().getInference().getPages().forEach(
//        page -> System.out.println(page.toString())
//    );
  }

}

```

**Output (RST):**
```rst
########
Document
########
:Mindee ID: 38a12fe0-5d69-4ca4-9b30-12f1b659311c
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/nutrition_facts v1.0
:Rotation applied: No

Prediction
==========
:Serving per Box: 2.00
:Serving Size:
  :Amount: 228.00
  :Unit: g
:Calories:
  :Daily Value:
  :Per 100g:
  :Per Serving: 250.00
:Total Fat:
  :Daily Value:
  :Per 100g:
  :Per Serving: 12.00
:Saturated Fat:
  :Daily Value: 15.00
  :Per 100g:
  :Per Serving: 3.00
:Trans Fat:
  :Daily Value:
  :Per 100g:
  :Per Serving: 3.00
:Cholesterol:
  :Daily Value: 10.00
  :Per 100g:
  :Per Serving: 30.00
:Total Carbohydrate:
  :Daily Value: 10.00
  :Per 100g:
  :Per Serving: 31.00
:Dietary Fiber:
  :Daily Value: 0.00
  :Per 100g:
  :Per Serving: 0.00
:Total Sugars:
  :Daily Value:
  :Per 100g:
  :Per Serving: 5.00
:Added Sugars:
  :Daily Value:
  :Per 100g:
  :Per Serving:
:Protein:
  :Daily Value:
  :Per 100g:
  :Per Serving: 5.00
:sodium:
  :Daily Value: 20.00
  :Per 100g:
  :Per Serving: 470.00
  :Unit: mg
:nutrients:
  +-------------+----------------------+----------+-------------+------+
  | Daily Value | Name                 | Per 100g | Per Serving | Unit |
  +=============+======================+==========+=============+======+
  | 12.00       | Vitamin A            |          | 4.00        | mcg  |
  +-------------+----------------------+----------+-------------+------+
  | 12.00       | Vitamin C            |          | 2.00        | mg   |
  +-------------+----------------------+----------+-------------+------+
  | 12.00       | Calcium              |          | 45.60       | mg   |
  +-------------+----------------------+----------+-------------+------+
  | 12.00       | Iron                 |          | 0.90        | mg   |
  +-------------+----------------------+----------+-------------+------+
```

# Field Types
## Standard Fields
These fields are generic and used in several products.

### BaseField
Each prediction object contains a set of fields that inherit from the generic `BaseField` class.
A typical `BaseField` object will have the following attributes:

* **confidence** (`Double`): the confidence score of the field prediction.
* **boundingBox** (`Polygon`): contains exactly 4 relative vertices (points) coordinates of a right rectangle containing the field in the document.
* **polygon** (`Polygon`): contains the relative vertices coordinates (`polygon` extends `List<Point>`) of a polygon containing the field in the image.
* **pageId** (`Integer`): the ID of the page, always `null` when at document-level.

> **Note:** A `Point` simply refers to a List of `Double`.


Aside from the previous attributes, all basic fields have access to a custom `toString` method that can be used to print their value as a string.

### AmountField
An amount field `AmountField` extends `BaseField`, but also implements:
* **value** (`Double`): corresponds to the field value. Can be `null` if no value was extracted.

## Specific Fields
Fields which are specific to this product; they are not used in any other product.

### Added Sugars Field
The amount of added sugars in the product.

A `NutritionFactsLabelV1AddedSugar` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of added sugars to consume or not to exceed each day.
* **per100G** (`Double`): The amount of added sugars per 100g of the product.
* **perServing** (`Double`): The amount of added sugars per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### Calories Field
The amount of calories in the product.

A `NutritionFactsLabelV1Calorie` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of calories to consume or not to exceed each day.
* **per100G** (`Double`): The amount of calories per 100g of the product.
* **perServing** (`Double`): The amount of calories per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### Cholesterol Field
The amount of cholesterol in the product.

A `NutritionFactsLabelV1Cholesterol` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of cholesterol to consume or not to exceed each day.
* **per100G** (`Double`): The amount of cholesterol per 100g of the product.
* **perServing** (`Double`): The amount of cholesterol per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### Dietary Fiber Field
The amount of dietary fiber in the product.

A `NutritionFactsLabelV1DietaryFiber` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of dietary fiber to consume or not to exceed each day.
* **per100G** (`Double`): The amount of dietary fiber per 100g of the product.
* **perServing** (`Double`): The amount of dietary fiber per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### nutrients Field
The amount of nutrients in the product.

A `NutritionFactsLabelV1Nutrient` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of nutrients to consume or not to exceed each day.
* **name** (`String`): The name of nutrients of the product.
* **per100G** (`Double`): The amount of nutrients per 100g of the product.
* **perServing** (`Double`): The amount of nutrients per serving of the product.
* **unit** (`String`): The unit of measurement for the amount of nutrients.
Fields which are specific to this product; they are not used in any other product.

### Protein Field
The amount of protein in the product.

A `NutritionFactsLabelV1Protein` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of protein to consume or not to exceed each day.
* **per100G** (`Double`): The amount of protein per 100g of the product.
* **perServing** (`Double`): The amount of protein per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### Saturated Fat Field
The amount of saturated fat in the product.

A `NutritionFactsLabelV1SaturatedFat` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of saturated fat to consume or not to exceed each day.
* **per100G** (`Double`): The amount of saturated fat per 100g of the product.
* **perServing** (`Double`): The amount of saturated fat per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### Serving Size Field
The size of a single serving of the product.

A `NutritionFactsLabelV1ServingSize` implements the following attributes:

* **amount** (`Double`): The amount of a single serving.
* **unit** (`String`): The unit for the amount of a single serving.
Fields which are specific to this product; they are not used in any other product.

### sodium Field
The amount of sodium in the product.

A `NutritionFactsLabelV1Sodium` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of sodium to consume or not to exceed each day.
* **per100G** (`Double`): The amount of sodium per 100g of the product.
* **perServing** (`Double`): The amount of sodium per serving of the product.
* **unit** (`String`): The unit of measurement for the amount of sodium.
Fields which are specific to this product; they are not used in any other product.

### Total Carbohydrate Field
The total amount of carbohydrates in the product.

A `NutritionFactsLabelV1TotalCarbohydrate` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of total carbohydrates to consume or not to exceed each day.
* **per100G** (`Double`): The amount of total carbohydrates per 100g of the product.
* **perServing** (`Double`): The amount of total carbohydrates per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### Total Fat Field
The total amount of fat in the product.

A `NutritionFactsLabelV1TotalFat` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of total fat to consume or not to exceed each day.
* **per100G** (`Double`): The amount of total fat per 100g of the product.
* **perServing** (`Double`): The amount of total fat per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### Total Sugars Field
The total amount of sugars in the product.

A `NutritionFactsLabelV1TotalSugar` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of total sugars to consume or not to exceed each day.
* **per100G** (`Double`): The amount of total sugars per 100g of the product.
* **perServing** (`Double`): The amount of total sugars per serving of the product.
Fields which are specific to this product; they are not used in any other product.

### Trans Fat Field
The amount of trans fat in the product.

A `NutritionFactsLabelV1TransFat` implements the following attributes:

* **dailyValue** (`Double`): DVs are the recommended amounts of trans fat to consume or not to exceed each day.
* **per100G** (`Double`): The amount of trans fat per 100g of the product.
* **perServing** (`Double`): The amount of trans fat per serving of the product.

# Attributes
The following fields are extracted for Nutrition Facts Label V1:

## Added Sugars
**addedSugars**([NutritionFactsLabelV1AddedSugar](#added-sugars-field)): The amount of added sugars in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAddedSugars().value);
```

## Calories
**calories**([NutritionFactsLabelV1Calorie](#calories-field)): The amount of calories in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCalories().value);
```

## Cholesterol
**cholesterol**([NutritionFactsLabelV1Cholesterol](#cholesterol-field)): The amount of cholesterol in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCholesterol().value);
```

## Dietary Fiber
**dietaryFiber**([NutritionFactsLabelV1DietaryFiber](#dietary-fiber-field)): The amount of dietary fiber in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDietaryFiber().value);
```

## nutrients
**nutrients**(List<[NutritionFactsLabelV1Nutrient](#nutrients-field)>): The amount of nutrients in the product.

```java
for (nutrientsElem : result.getDocument().getInference().getPrediction().getNutrients())
{
    System.out.println(nutrientsElem.value);
}
```

## Protein
**protein**([NutritionFactsLabelV1Protein](#protein-field)): The amount of protein in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getProtein().value);
```

## Saturated Fat
**saturatedFat**([NutritionFactsLabelV1SaturatedFat](#saturated-fat-field)): The amount of saturated fat in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSaturatedFat().value);
```

## Serving per Box
**servingPerBox**: The number of servings in each box of the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getServingPerBox().value);
```

## Serving Size
**servingSize**([NutritionFactsLabelV1ServingSize](#serving-size-field)): The size of a single serving of the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getServingSize().value);
```

## sodium
**sodium**([NutritionFactsLabelV1Sodium](#sodium-field)): The amount of sodium in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSodium().value);
```

## Total Carbohydrate
**totalCarbohydrate**([NutritionFactsLabelV1TotalCarbohydrate](#total-carbohydrate-field)): The total amount of carbohydrates in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalCarbohydrate().value);
```

## Total Fat
**totalFat**([NutritionFactsLabelV1TotalFat](#total-fat-field)): The total amount of fat in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalFat().value);
```

## Total Sugars
**totalSugars**([NutritionFactsLabelV1TotalSugar](#total-sugars-field)): The total amount of sugars in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalSugars().value);
```

## Trans Fat
**transFat**([NutritionFactsLabelV1TransFat](#trans-fat-field)): The amount of trans fat in the product.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTransFat().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
