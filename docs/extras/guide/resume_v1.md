---
title: Resume OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-resume-ocr
---
The Java OCR SDK supports the [Resume API](https://platform.mindee.com/mindee/resume).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/resume/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Resume sample](https://github.com/mindee/client-lib-test-data/blob/main/products/resume/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.resume.ResumeV1;
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
    AsyncPredictResponse<ResumeV1> response = mindeeClient.enqueueAndParse(
        ResumeV1.class,
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
:Mindee ID: bc80bae0-af75-4464-95a9-2419403c75bf
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/resume v1.0
:Rotation applied: No

Prediction
==========
:Document Language: ENG
:Document Type: RESUME
:Given Names: Christopher
:Surnames: Morgan
:Nationality:
:Email Address: christoper.m@gmail.com
:Phone Number: +44 (0) 20 7666 8555
:Address: 177 Great Portland Street, London W5W 6PQ
:Social Networks:
  +----------------------+----------------------------------------------------+
  | Name                 | URL                                                |
  +======================+====================================================+
  | LinkedIn             | linkedin.com/christopher.morgan                    |
  +----------------------+----------------------------------------------------+
:Profession: Senior Web Developer
:Job Applied:
:Languages:
  +----------+----------------------+
  | Language | Level                |
  +==========+======================+
  | SPA      | Fluent               |
  +----------+----------------------+
  | ZHO      | Beginner             |
  +----------+----------------------+
  | DEU      | Intermediate         |
  +----------+----------------------+
:Hard Skills: HTML5
              PHP OOP
              JavaScript
              CSS
              MySQL
:Soft Skills: Project management
              Strong decision maker
              Innovative
              Complex problem solver
              Creative design
              Service-focused
:Education:
  +-----------------+---------------------------+-----------+----------+---------------------------+-------------+------------+
  | Domain          | Degree                    | End Month | End Year | School                    | Start Month | Start Year |
  +=================+===========================+===========+==========+===========================+=============+============+
  | Computer Inf... | Bachelor                  |           |          | Columbia University, NY   |             | 2014       |
  +-----------------+---------------------------+-----------+----------+---------------------------+-------------+------------+
:Professional Experiences:
  +-----------------+------------+---------------------------+-----------+----------+----------------------+-------------+------------+
  | Contract Type   | Department | Employer                  | End Month | End Year | Role                 | Start Month | Start Year |
  +=================+============+===========================+===========+==========+======================+=============+============+
  | Full-Time       |            | Luna Web Design, New York | 05        | 2019     | Web Developer        | 09          | 2015       |
  +-----------------+------------+---------------------------+-----------+----------+----------------------+-------------+------------+
:Certificates:
  +------------+--------------------------------+---------------------------+------+
  | Grade      | Name                           | Provider                  | Year |
  +============+================================+===========================+======+
  |            | PHP Framework (certificate)... |                           | 2014 |
  +------------+--------------------------------+---------------------------+------+
  |            | Programming Languages: Java... |                           |      |
  +------------+--------------------------------+---------------------------+------+
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


### ClassificationField
The classification field `ClassificationField` extends `BaseField`, but also implements:
* **value** (`strong`): corresponds to the field value.
* **confidence** (`double`): the confidence score of the field prediction.

> Note: a classification field's `value is always a `String`.

### StringField
The text field `StringField` extends `BaseField`, but also implements:
* **value** (`String`): corresponds to the field value.
* **rawValue** (`String`): corresponds to the raw value as it appears on the document.

## Specific Fields
Fields which are specific to this product; they are not used in any other product.

### Certificates Field
The list of certificates obtained by the candidate.

A `ResumeV1Certificate` implements the following attributes:

* **grade** (`String`): The grade obtained for the certificate.
* **name** (`String`): The name of certification.
* **provider** (`String`): The organization or institution that issued the certificate.
* **year** (`String`): The year when a certificate was issued or received.
Fields which are specific to this product; they are not used in any other product.

### Education Field
The list of the candidate's educational background.

A `ResumeV1Education` implements the following attributes:

* **degreeDomain** (`String`): The area of study or specialization.
* **degreeType** (`String`): The type of degree obtained, such as Bachelor's, Master's, or Doctorate.
* **endMonth** (`String`): The month when the education program or course was completed.
* **endYear** (`String`): The year when the education program or course was completed.
* **school** (`String`): The name of the school.
* **startMonth** (`String`): The month when the education program or course began.
* **startYear** (`String`): The year when the education program or course began.
Fields which are specific to this product; they are not used in any other product.

### Languages Field
The list of languages that the candidate is proficient in.

A `ResumeV1Language` implements the following attributes:

* **language** (`String`): The language's ISO 639 code.
* **level** (`String`): The candidate's level for the language.
Fields which are specific to this product; they are not used in any other product.

### Professional Experiences Field
The list of the candidate's professional experiences.

A `ResumeV1ProfessionalExperience` implements the following attributes:

* **contractType** (`String`): The type of contract for the professional experience.
* **department** (`String`): The specific department or division within the company.
* **employer** (`String`): The name of the company or organization.
* **endMonth** (`String`): The month when the professional experience ended.
* **endYear** (`String`): The year when the professional experience ended.
* **role** (`String`): The position or job title held by the candidate.
* **startMonth** (`String`): The month when the professional experience began.
* **startYear** (`String`): The year when the professional experience began.
Fields which are specific to this product; they are not used in any other product.

### Social Networks Field
The list of social network profiles of the candidate.

A `ResumeV1SocialNetworksUrl` implements the following attributes:

* **name** (`String`): The name of the social network.
* **url** (`String`): The URL of the social network.

# Attributes
The following fields are extracted for Resume V1:

## Address
**address** : The location information of the candidate, including city, state, and country.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAddress().value);
```

## Certificates
**certificates** (List<[ResumeV1Certificate](#certificates-field)>): The list of certificates obtained by the candidate.

```java
for (certificatesElem : result.getDocument().getInference().getPrediction().getCertificates())
{
    System.out.println(certificatesElem.value);
}
```

## Document Language
**documentLanguage** : The ISO 639 code of the language in which the document is written.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDocumentLanguage().value);
```

## Document Type
**documentType** : The type of the document sent.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDocumentType().value);
```

## Education
**education** (List<[ResumeV1Education](#education-field)>): The list of the candidate's educational background.

```java
for (educationElem : result.getDocument().getInference().getPrediction().getEducation())
{
    System.out.println(educationElem.value);
}
```

## Email Address
**emailAddress** : The email address of the candidate.

```java
System.out.println(result.getDocument().getInference().getPrediction().getEmailAddress().value);
```

## Given Names
**givenNames** : The candidate's first or given names.

```java
for (givenNamesElem : result.getDocument().getInference().getPrediction().getGivenNames())
{
    System.out.println(givenNamesElem.value);
}
```

## Hard Skills
**hardSkills** : The list of the candidate's technical abilities and knowledge.

```java
for (hardSkillsElem : result.getDocument().getInference().getPrediction().getHardSkills())
{
    System.out.println(hardSkillsElem.value);
}
```

## Job Applied
**jobApplied** : The position that the candidate is applying for.

```java
System.out.println(result.getDocument().getInference().getPrediction().getJobApplied().value);
```

## Languages
**languages** (List<[ResumeV1Language](#languages-field)>): The list of languages that the candidate is proficient in.

```java
for (languagesElem : result.getDocument().getInference().getPrediction().getLanguages())
{
    System.out.println(languagesElem.value);
}
```

## Nationality
**nationality** : The ISO 3166 code for the country of citizenship of the candidate.

```java
System.out.println(result.getDocument().getInference().getPrediction().getNationality().value);
```

## Phone Number
**phoneNumber** : The phone number of the candidate.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPhoneNumber().value);
```

## Profession
**profession** : The candidate's current profession.

```java
System.out.println(result.getDocument().getInference().getPrediction().getProfession().value);
```

## Professional Experiences
**professionalExperiences** (List<[ResumeV1ProfessionalExperience](#professional-experiences-field)>): The list of the candidate's professional experiences.

```java
for (professionalExperiencesElem : result.getDocument().getInference().getPrediction().getProfessionalExperiences())
{
    System.out.println(professionalExperiencesElem.value);
}
```

## Social Networks
**socialNetworksUrls** (List<[ResumeV1SocialNetworksUrl](#social-networks-field)>): The list of social network profiles of the candidate.

```java
for (socialNetworksUrlsElem : result.getDocument().getInference().getPrediction().getSocialNetworksUrls())
{
    System.out.println(socialNetworksUrlsElem.value);
}
```

## Soft Skills
**softSkills** : The list of the candidate's interpersonal and communication abilities.

```java
for (softSkillsElem : result.getDocument().getInference().getPrediction().getSoftSkills())
{
    System.out.println(softSkillsElem.value);
}
```

## Surnames
**surnames** : The candidate's last names.

```java
for (surnamesElem : result.getDocument().getInference().getPrediction().getSurnames())
{
    System.out.println(surnamesElem.value);
}
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
