---
title: FR Carte Grise OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-fr-carte-grise-ocr
---
The Java OCR SDK supports the [Carte Grise API](https://platform.mindee.com/mindee/carte_grise).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/carte_grise/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Carte Grise sample](https://github.com/mindee/client-lib-test-data/blob/main/products/carte_grise/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.fr.cartegrise.CarteGriseV1;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {

  public static void main(String[] args) throws IOException {
    String apiKey = "my-api-key";
    String filePath = "/path/to/the/file.ext";

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient(apiKey);

    // Load a file from disk
    LocalInputSource inputSource = new LocalInputSource(filePath);

    // Parse the file
    PredictResponse<CarteGriseV1> response = mindeeClient.parse(
        CarteGriseV1.class,
        inputSource
    );

    // Print a summary of the response
    System.out.println(response.toString());

    // Print a summary of the predictions
//  System.out.println(response.getDocument().toString());

    // Print the document-level predictions
//    System.out.println(response.getDocument().getInference().getPrediction().toString());

    // Print the page-level predictions
//    response.getDocument().getInference().getPages().forEach(
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
:Mindee ID: 4443182b-57c1-4426-a288-01b94f226e84
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/carte_grise v1.1
:Rotation applied: Yes

Prediction
==========
:a: AB-123-CD
:b: 1998-01-05
:c1: DUPONT YVES
:c3: 27 RUE DES ROITELETS 59169 FERIN LES BAINS FRANCE
:c41: 2 DELAROCHE
:c4a: EST LE PROPRIETAIRE DU VEHICULE
:d1:
:d3: MODELE
:e: VFS1V2009AS1V2009
:f1: 1915
:f2: 1915
:f3: 1915
:g: 3030
:g1: 1307
:i: 2009-12-04
:j: N1
:j1: VP
:j2: AA
:j3: CI
:p1: 1900
:p2: 90
:p3: GO
:p6: 6
:q: 006
:s1: 5
:s2:
:u1: 77
:u2: 3000
:v7: 155
:x1: 2011-07-06
:y1: 17835
:y2:
:y3: 0
:y4: 4
:y5: 2.5
:y6: 178.35
:Formula Number: 2009AS05284
:Owner's First Name: YVES
:Owner's Surname: DUPONT
:MRZ Line 1:
:MRZ Line 2: CI<<MARQUES<<<<<<<MODELE<<<<<<<2009AS0528402

Page Predictions
================

Page 0
------
:a: AB-123-CD
:b: 1998-01-05
:c1: DUPONT YVES
:c3: 27 RUE DES ROITELETS 59169 FERIN LES BAINS FRANCE
:c41: 2 DELAROCHE
:c4a: EST LE PROPRIETAIRE DU VEHICULE
:d1:
:d3: MODELE
:e: VFS1V2009AS1V2009
:f1: 1915
:f2: 1915
:f3: 1915
:g: 3030
:g1: 1307
:i: 2009-12-04
:j: N1
:j1: VP
:j2: AA
:j3: CI
:p1: 1900
:p2: 90
:p3: GO
:p6: 6
:q: 006
:s1: 5
:s2:
:u1: 77
:u2: 3000
:v7: 155
:x1: 2011-07-06
:y1: 17835
:y2:
:y3: 0
:y4: 4
:y5: 2.5
:y6: 178.35
:Formula Number: 2009AS05284
:Owner's First Name: YVES
:Owner's Surname: DUPONT
:MRZ Line 1:
:MRZ Line 2: CI<<MARQUES<<<<<<<MODELE<<<<<<<2009AS0528402
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

### StringField
The text field `StringField` extends `BaseField`, but also implements:
* **value** (`String`): corresponds to the field value.
* **rawValue** (`String`): corresponds to the raw value as it appears on the document.

### DateField
The date field `DateField` extends `BaseField`, but also implements:

* **value** (`LocalDate`): an accessible representation of the value as a Java object. Can be `null`.

# Attributes
The following fields are extracted for Carte Grise V1:

## a
**a** : The vehicle's license plate number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getA().value);
```

## b
**b** : The vehicle's first release date.

```java
System.out.println(result.getDocument().getInference().getPrediction().getB().value);
```

## c1
**c1** : The vehicle owner's full name including maiden name.

```java
System.out.println(result.getDocument().getInference().getPrediction().getC1().value);
```

## c3
**c3** : The vehicle owner's address.

```java
System.out.println(result.getDocument().getInference().getPrediction().getC3().value);
```

## c41
**c41** : Number of owners of the license certificate.

```java
System.out.println(result.getDocument().getInference().getPrediction().getC41().value);
```

## c4a
**c4A** : Mentions about the ownership of the vehicle.

```java
System.out.println(result.getDocument().getInference().getPrediction().getC4A().value);
```

## d1
**d1** : The vehicle's brand.

```java
System.out.println(result.getDocument().getInference().getPrediction().getD1().value);
```

## d3
**d3** : The vehicle's commercial name.

```java
System.out.println(result.getDocument().getInference().getPrediction().getD3().value);
```

## e
**e** : The Vehicle Identification Number (VIN).

```java
System.out.println(result.getDocument().getInference().getPrediction().getE().value);
```

## f1
**f1** : The vehicle's maximum admissible weight.

```java
System.out.println(result.getDocument().getInference().getPrediction().getF1().value);
```

## f2
**f2** : The vehicle's maximum admissible weight within the license's state.

```java
System.out.println(result.getDocument().getInference().getPrediction().getF2().value);
```

## f3
**f3** : The vehicle's maximum authorized weight with coupling.

```java
System.out.println(result.getDocument().getInference().getPrediction().getF3().value);
```

## Formula Number
**formulaNumber** : The document's formula number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getFormulaNumber().value);
```

## g
**g** : The vehicle's weight with coupling if tractor different than category M1.

```java
System.out.println(result.getDocument().getInference().getPrediction().getG().value);
```

## g1
**g1** : The vehicle's national empty weight.

```java
System.out.println(result.getDocument().getInference().getPrediction().getG1().value);
```

## i
**i** : The car registration date of the given certificate.

```java
System.out.println(result.getDocument().getInference().getPrediction().getI().value);
```

## j
**j** : The vehicle's category.

```java
System.out.println(result.getDocument().getInference().getPrediction().getJ().value);
```

## j1
**j1** : The vehicle's national type.

```java
System.out.println(result.getDocument().getInference().getPrediction().getJ1().value);
```

## j2
**j2** : The vehicle's body type (CE).

```java
System.out.println(result.getDocument().getInference().getPrediction().getJ2().value);
```

## j3
**j3** : The vehicle's body type (National designation).

```java
System.out.println(result.getDocument().getInference().getPrediction().getJ3().value);
```

## MRZ Line 1
**mrz1** : Machine Readable Zone, first line.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz1().value);
```

## MRZ Line 2
**mrz2** : Machine Readable Zone, second line.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz2().value);
```

## Owner's First Name
**ownerFirstName** : The vehicle's owner first name.

```java
System.out.println(result.getDocument().getInference().getPrediction().getOwnerFirstName().value);
```

## Owner's Surname
**ownerSurname** : The vehicle's owner surname.

```java
System.out.println(result.getDocument().getInference().getPrediction().getOwnerSurname().value);
```

## p1
**p1** : The vehicle engine's displacement (cm3).

```java
System.out.println(result.getDocument().getInference().getPrediction().getP1().value);
```

## p2
**p2** : The vehicle's maximum net power (kW).

```java
System.out.println(result.getDocument().getInference().getPrediction().getP2().value);
```

## p3
**p3** : The vehicle's fuel type or energy source.

```java
System.out.println(result.getDocument().getInference().getPrediction().getP3().value);
```

## p6
**p6** : The vehicle's administrative power (fiscal horsepower).

```java
System.out.println(result.getDocument().getInference().getPrediction().getP6().value);
```

## q
**q** : The vehicle's power to weight ratio.

```java
System.out.println(result.getDocument().getInference().getPrediction().getQ().value);
```

## s1
**s1** : The vehicle's number of seats.

```java
System.out.println(result.getDocument().getInference().getPrediction().getS1().value);
```

## s2
**s2** : The vehicle's number of standing rooms (person).

```java
System.out.println(result.getDocument().getInference().getPrediction().getS2().value);
```

## u1
**u1** : The vehicle's sound level (dB).

```java
System.out.println(result.getDocument().getInference().getPrediction().getU1().value);
```

## u2
**u2** : The vehicle engine's rotation speed (RPM).

```java
System.out.println(result.getDocument().getInference().getPrediction().getU2().value);
```

## v7
**v7** : The vehicle's CO2 emission (g/km).

```java
System.out.println(result.getDocument().getInference().getPrediction().getV7().value);
```

## x1
**x1** : Next technical control date.

```java
System.out.println(result.getDocument().getInference().getPrediction().getX1().value);
```

## y1
**y1** : Amount of the regional proportional tax of the registration (in euros).

```java
System.out.println(result.getDocument().getInference().getPrediction().getY1().value);
```

## y2
**y2** : Amount of the additional parafiscal tax of the registration (in euros).

```java
System.out.println(result.getDocument().getInference().getPrediction().getY2().value);
```

## y3
**y3** : Amount of the additional CO2 tax of the registration (in euros).

```java
System.out.println(result.getDocument().getInference().getPrediction().getY3().value);
```

## y4
**y4** : Amount of the fee for managing the registration (in euros).

```java
System.out.println(result.getDocument().getInference().getPrediction().getY4().value);
```

## y5
**y5** : Amount of the fee for delivery of the registration certificate in euros.

```java
System.out.println(result.getDocument().getInference().getPrediction().getY5().value);
```

## y6
**y6** : Total amount of registration fee to be paid in euros.

```java
System.out.println(result.getDocument().getInference().getPrediction().getY6().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
