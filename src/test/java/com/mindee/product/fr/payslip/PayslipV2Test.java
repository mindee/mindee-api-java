package com.mindee.product.fr.payslip;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV1ResourcePathString;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for PayslipV2.
 */
public class PayslipV2Test {

  protected PredictResponse<PayslipV2> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      PayslipV2.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/payslip_fra/response_v2/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<PayslipV2> response = getPrediction("empty");
    PayslipV2Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getEmployee().getAddress());
    Assertions.assertNull(docPrediction.getEmployee().getDateOfBirth());
    Assertions.assertNull(docPrediction.getEmployee().getFirstName());
    Assertions.assertNull(docPrediction.getEmployee().getLastName());
    Assertions.assertNull(docPrediction.getEmployee().getPhoneNumber());
    Assertions.assertNull(docPrediction.getEmployee().getRegistrationNumber());
    Assertions.assertNull(docPrediction.getEmployee().getSocialSecurityNumber());
    Assertions.assertNull(docPrediction.getEmployer().getAddress());
    Assertions.assertNull(docPrediction.getEmployer().getCompanyId());
    Assertions.assertNull(docPrediction.getEmployer().getCompanySite());
    Assertions.assertNull(docPrediction.getEmployer().getNafCode());
    Assertions.assertNull(docPrediction.getEmployer().getName());
    Assertions.assertNull(docPrediction.getEmployer().getPhoneNumber());
    Assertions.assertNull(docPrediction.getEmployer().getUrssafNumber());
    Assertions.assertNull(docPrediction.getBankAccountDetails().getBankName());
    Assertions.assertNull(docPrediction.getBankAccountDetails().getIban());
    Assertions.assertNull(docPrediction.getBankAccountDetails().getSwift());
    Assertions.assertNull(docPrediction.getEmployment().getCategory());
    Assertions.assertNull(docPrediction.getEmployment().getCoefficient());
    Assertions.assertNull(docPrediction.getEmployment().getCollectiveAgreement());
    Assertions.assertNull(docPrediction.getEmployment().getJobTitle());
    Assertions.assertNull(docPrediction.getEmployment().getPositionLevel());
    Assertions.assertNull(docPrediction.getEmployment().getStartDate());
    Assertions.assertTrue(docPrediction.getSalaryDetails().isEmpty());
    Assertions.assertNull(docPrediction.getPayDetail().getGrossSalary());
    Assertions.assertNull(docPrediction.getPayDetail().getGrossSalaryYtd());
    Assertions.assertNull(docPrediction.getPayDetail().getIncomeTaxRate());
    Assertions.assertNull(docPrediction.getPayDetail().getIncomeTaxWithheld());
    Assertions.assertNull(docPrediction.getPayDetail().getNetPaid());
    Assertions.assertNull(docPrediction.getPayDetail().getNetPaidBeforeTax());
    Assertions.assertNull(docPrediction.getPayDetail().getNetTaxable());
    Assertions.assertNull(docPrediction.getPayDetail().getNetTaxableYtd());
    Assertions.assertNull(docPrediction.getPayDetail().getTotalCostEmployer());
    Assertions.assertNull(docPrediction.getPayDetail().getTotalTaxesAndDeductions());
    Assertions.assertNull(docPrediction.getPto().getAccruedThisPeriod());
    Assertions.assertNull(docPrediction.getPto().getBalanceEndOfPeriod());
    Assertions.assertNull(docPrediction.getPto().getUsedThisPeriod());
    Assertions.assertNull(docPrediction.getPayPeriod().getEndDate());
    Assertions.assertNull(docPrediction.getPayPeriod().getMonth());
    Assertions.assertNull(docPrediction.getPayPeriod().getPaymentDate());
    Assertions.assertNull(docPrediction.getPayPeriod().getStartDate());
    Assertions.assertNull(docPrediction.getPayPeriod().getYear());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<PayslipV2> response = getPrediction("complete");
    Document<PayslipV2> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/payslip_fra/response_v2/summary_full.rst")
    );
  }

}
