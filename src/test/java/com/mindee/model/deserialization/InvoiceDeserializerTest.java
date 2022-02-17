package com.mindee.model.deserialization;

import static org.junit.jupiter.api.Assertions.*;

import com.mindee.model.documenttype.InvoiceResponse;
import com.mindee.model.documenttype.InvoiceResponse.InvoiceDocument;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Locale;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale.Builder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceDeserializerTest {

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void givenAJsonInvoice_whenDeserialized_thenReturnsCorrectInvoiceResponse()
  {
    InvoiceResponse response = new InvoiceResponse();
    List<Field> companyRegistrationList = new ArrayList<>();
    companyRegistrationList.add(Field.builder()
            .rawValue("501124705")
            .value("501124705")
            .confidence(0.39)
            .reconstructed(Boolean.FALSE)
            .extraField("type","SIREN")
            .polygon(Arrays.asList(
                Arrays.asList(0.759,0.125),
                Arrays.asList(0.835,0.125),
                Arrays.asList(0.835,0.137),
                Arrays.asList(0.759,0.137)))
        .build());
    companyRegistrationList.add(Field.builder()
        .rawValue("FR33501124705")
        .value("FR33501124705")
        .confidence(0.39)
        .reconstructed(Boolean.FALSE)
        .extraField("type","VAT NUMBER")
        .polygon(Arrays.asList(
            Arrays.asList(0.759,0.125),
            Arrays.asList(0.835,0.125),
            Arrays.asList(0.835,0.137),
            Arrays.asList(0.759,0.137)))
        .build());

    companyRegistrationList.add(Field.builder()
        .rawValue("501124705")
        .value("501124705")
        .confidence(0.39)
        .reconstructed(Boolean.FALSE)
        .extraField("type","SIREN")
        .polygon(Arrays.asList(
            Arrays.asList(0.759,0.125),
            Arrays.asList(0.835,0.125),
            Arrays.asList(0.835,0.137),
            Arrays.asList(0.835,0.125)))
        .build());

    InvoiceDocument invoiceDocument = InvoiceDocument.builder()
        .companyNumber(companyRegistrationList)
        .locale(Locale.builder()
            .currency("EUR")
            .language("fr")
            .rawValue("fr")
            .value(new Builder().setLanguageTag("fr").build())
            .confidence(0.94)
            .build())
        .totalIncl(Amount.builder()
            .reconstructed(Boolean.FALSE)
            .value(587.95)
            .confidence(0.99)
            .polygon(Arrays.asList(
                Arrays.asList(0.889,0.783),
                Arrays.asList(0.94,0.783),
                Arrays.asList(0.94,0.797),
                Arrays.asList(0.889,0.797)))
            .build())
        .invoiceDate(Date.builder()
            .value(LocalDate.parse("2020-02-17"))
            .rawValue("2020-02-17")
            .confidence(0.99)
            .reconstructed(Boolean.FALSE)
            .build())
        .build();
  }
}