---
title: FR Payslip OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-fr-payslip-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Payslip API](https://platform.mindee.com/mindee/payslip_fra).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/payslip_fra/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Payslip sample](https://github.com/mindee/client-lib-test-data/blob/main/products/payslip_fra/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.fr.payslip.PayslipV3;
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
    AsyncPredictResponse<PayslipV3> response = mindeeClient.enqueueAndParse(
        PayslipV3.class,
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
:Mindee ID: a479e3e7-6838-4e82-9a7d-99289f34ec7f
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/payslip_fra v3.0
:Rotation applied: Yes

Prediction
==========
:Pay Period:
  :End Date: 2023-03-31
  :Month: 03
  :Payment Date: 2023-03-29
  :Start Date: 2023-03-01
  :Year: 2023
:Employee:
  :Address: 52 RUE DES FLEURS 33500 LIBOURNE FRANCE
  :Date of Birth:
  :First Name: Jean Luc
  :Last Name: Picard
  :Phone Number:
  :Registration Number:
  :Social Security Number: 123456789012345
:Employer:
  :Address: 1 RUE DU TONNOT 25210 DOUBS
  :Company ID: 12345678901234
  :Company Site:
  :NAF Code: 1234A
  :Name: DEMO COMPANY
  :Phone Number:
  :URSSAF Number:
:Bank Account Details:
  :Bank Name:
  :IBAN:
  :SWIFT:
:Employment:
  :Category: Cadre
  :Coefficient: 600,000
  :Collective Agreement: Construction -- Promotion
  :Job Title: Directeur Régional du Développement
  :Position Level: Niveau 5 Echelon 3
  :Seniority Date:
  :Start Date: 2022-05-01
:Salary Details:
  +--------------+-----------+--------------------------------------+--------+-----------+
  | Amount       | Base      | Description                          | Number | Rate      |
  +==============+===========+======================================+========+===========+
  | 6666.67      |           | Salaire de base                      |        |           |
  +--------------+-----------+--------------------------------------+--------+-----------+
  | 9.30         |           | Part patronale Mutuelle NR           |        |           |
  +--------------+-----------+--------------------------------------+--------+-----------+
  | 508.30       |           | Avantages en nature voiture          |        |           |
  +--------------+-----------+--------------------------------------+--------+-----------+
:Pay Detail:
  :Gross Salary: 7184.27
  :Gross Salary YTD: 18074.81
  :Income Tax Rate: 17.60
  :Income Tax Withheld: 1030.99
  :Net Paid: 3868.32
  :Net Paid Before Tax: 4899.31
  :Net Taxable: 5857.90
  :Net Taxable YTD: 14752.73
  :Total Cost Employer: 10486.94
  :Total Taxes and Deductions: 1650.36
:Paid Time Off:
  +-----------+--------+-------------+-----------+-----------+
  | Accrued   | Period | Type        | Remaining | Used      |
  +===========+========+=============+===========+===========+
  |           | N-1    | VACATION    |           |           |
  +-----------+--------+-------------+-----------+-----------+
  | 6.17      | N      | VACATION    | 6.17      |           |
  +-----------+--------+-------------+-----------+-----------+
  | 2.01      | N      | RTT         | 2.01      |           |
  +-----------+--------+-------------+-----------+-----------+
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

## Specific Fields
Fields which are specific to this product; they are not used in any other product.

### Bank Account Details Field
Information about the employee's bank account.

A `PayslipV3BankAccountDetail` implements the following attributes:

* **bankName** (`String`): The name of the bank.
* **iban** (`String`): The IBAN of the bank account.
* **swift** (`String`): The SWIFT code of the bank.
Fields which are specific to this product; they are not used in any other product.

### Employee Field
Information about the employee.

A `PayslipV3Employee` implements the following attributes:

* **address** (`String`): The address of the employee.
* **dateOfBirth** (`String`): The date of birth of the employee.
* **firstName** (`String`): The first name of the employee.
* **lastName** (`String`): The last name of the employee.
* **phoneNumber** (`String`): The phone number of the employee.
* **registrationNumber** (`String`): The registration number of the employee.
* **socialSecurityNumber** (`String`): The social security number of the employee.
Fields which are specific to this product; they are not used in any other product.

### Employer Field
Information about the employer.

A `PayslipV3Employer` implements the following attributes:

* **address** (`String`): The address of the employer.
* **companyId** (`String`): The company ID of the employer.
* **companySite** (`String`): The site of the company.
* **nafCode** (`String`): The NAF code of the employer.
* **name** (`String`): The name of the employer.
* **phoneNumber** (`String`): The phone number of the employer.
* **urssafNumber** (`String`): The URSSAF number of the employer.
Fields which are specific to this product; they are not used in any other product.

### Employment Field
Information about the employment.

A `PayslipV3Employment` implements the following attributes:

* **category** (`String`): The category of the employment.
* **coefficient** (`String`): The coefficient of the employment.
* **collectiveAgreement** (`String`): The collective agreement of the employment.
* **jobTitle** (`String`): The job title of the employee.
* **positionLevel** (`String`): The position level of the employment.
* **seniorityDate** (`String`): The seniority date of the employment.
* **startDate** (`String`): The start date of the employment.
Fields which are specific to this product; they are not used in any other product.

### Paid Time Off Field
Information about paid time off.

A `PayslipV3PaidTimeOff` implements the following attributes:

* **accrued** (`Double`): The amount of paid time off accrued in the period.
* **period** (`String`): The paid time off period.

#### Possible values include:
 - N
 - N-1
 - N-2

* **ptoType** (`String`): The type of paid time off.

#### Possible values include:
 - VACATION
 - RTT
 - COMPENSATORY

* **remaining** (`Double`): The remaining amount of paid time off at the end of the period.
* **used** (`Double`): The amount of paid time off used in the period.
Fields which are specific to this product; they are not used in any other product.

### Pay Detail Field
Detailed information about the pay.

A `PayslipV3PayDetail` implements the following attributes:

* **grossSalary** (`Double`): The gross salary of the employee.
* **grossSalaryYtd** (`Double`): The year-to-date gross salary of the employee.
* **incomeTaxRate** (`Double`): The income tax rate of the employee.
* **incomeTaxWithheld** (`Double`): The income tax withheld from the employee's pay.
* **netPaid** (`Double`): The net paid amount of the employee.
* **netPaidBeforeTax** (`Double`): The net paid amount before tax of the employee.
* **netTaxable** (`Double`): The net taxable amount of the employee.
* **netTaxableYtd** (`Double`): The year-to-date net taxable amount of the employee.
* **totalCostEmployer** (`Double`): The total cost to the employer.
* **totalTaxesAndDeductions** (`Double`): The total taxes and deductions of the employee.
Fields which are specific to this product; they are not used in any other product.

### Pay Period Field
Information about the pay period.

A `PayslipV3PayPeriod` implements the following attributes:

* **endDate** (`String`): The end date of the pay period.
* **month** (`String`): The month of the pay period.
* **paymentDate** (`String`): The date of payment for the pay period.
* **startDate** (`String`): The start date of the pay period.
* **year** (`String`): The year of the pay period.
Fields which are specific to this product; they are not used in any other product.

### Salary Details Field
Detailed information about the earnings.

A `PayslipV3SalaryDetail` implements the following attributes:

* **amount** (`Double`): The amount of the earning.
* **base** (`Double`): The base rate value of the earning.
* **description** (`String`): The description of the earnings.
* **number** (`Double`): The number of units in the earning.
* **rate** (`Double`): The rate of the earning.

# Attributes
The following fields are extracted for Payslip V3:

## Bank Account Details
**bankAccountDetails**([PayslipV3BankAccountDetail](#bank-account-details-field)): Information about the employee's bank account.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBankAccountDetails().value);
```

## Employee
**employee**([PayslipV3Employee](#employee-field)): Information about the employee.

```java
System.out.println(result.getDocument().getInference().getPrediction().getEmployee().value);
```

## Employer
**employer**([PayslipV3Employer](#employer-field)): Information about the employer.

```java
System.out.println(result.getDocument().getInference().getPrediction().getEmployer().value);
```

## Employment
**employment**([PayslipV3Employment](#employment-field)): Information about the employment.

```java
System.out.println(result.getDocument().getInference().getPrediction().getEmployment().value);
```

## Paid Time Off
**paidTimeOff**(List<[PayslipV3PaidTimeOff](#paid-time-off-field)>): Information about paid time off.

```java
for (paidTimeOffElem : result.getDocument().getInference().getPrediction().getPaidTimeOff())
{
    System.out.println(paidTimeOffElem.value);
}
```

## Pay Detail
**payDetail**([PayslipV3PayDetail](#pay-detail-field)): Detailed information about the pay.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPayDetail().value);
```

## Pay Period
**payPeriod**([PayslipV3PayPeriod](#pay-period-field)): Information about the pay period.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPayPeriod().value);
```

## Salary Details
**salaryDetails**(List<[PayslipV3SalaryDetail](#salary-details-field)>): Detailed information about the earnings.

```java
for (salaryDetailsElem : result.getDocument().getInference().getPrediction().getSalaryDetails())
{
    System.out.println(salaryDetailsElem.value);
}
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
