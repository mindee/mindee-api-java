package com.mindee.model.postprocessing;

import com.mindee.model.documenttype.PassportResponse;
import com.mindee.model.documenttype.PassportResponse.PassportDocument;
import com.mindee.model.documenttype.PassportResponse.PassportPage;
import com.mindee.model.fields.Field;
import java.util.ArrayList;
import java.util.List;

public class PassportResponsePostProcessor {

  private PassportResponsePostProcessor() {
  }

  public static PassportResponse reconstructMrz(PassportResponse passportResponse) {

    PassportDocument passportDocument = passportResponse.getPassport();
    if (passportDocument.getMrz1() != null && passportDocument.getMrz1().getValue() != null
        && passportDocument.getMrz2() != null && passportDocument.getMrz2().getValue() != null
        && passportDocument.getMrz() == null) {
      String mrz = new StringBuilder(passportDocument.getMrz1().getValue())
          .append(passportDocument.getMrz2().getValue()).toString();
      PassportDocument document = passportDocument.toBuilder()
          .mrz(Field.builder()
              .value(mrz)
              .rawValue(mrz)
              .reconstructed(Boolean.TRUE)
              .confidence(passportDocument.getMrz1().getConfidence() * passportDocument.getMrz2()
                  .getConfidence())
              .build())
          .build();
      passportResponse.setPassport(document);
    }

    List<PassportPage> pages = new ArrayList<>();
    for (PassportPage passportPage : passportResponse.getPassports()) {
      if (passportPage.getMrz1() != null && passportPage.getMrz1().getValue() != null
          && passportPage.getMrz2() != null && passportPage.getMrz2().getValue() != null
          && passportPage.getMrz() == null) {
        String mrz = new StringBuilder(passportPage.getMrz1().getValue())
            .append(passportPage.getMrz2().getValue()).toString();
        PassportPage page = passportPage.toBuilder()
            .mrz(Field.builder()
                .value(mrz)
                .rawValue(mrz)
                .reconstructed(Boolean.TRUE)
                .confidence(
                    passportPage.getMrz1().getConfidence() * passportPage.getMrz2().getConfidence())
                .build())
            .build();
        pages.add(page);

      } else {
        pages.add(passportPage);
      }
    }
    passportResponse.setPassports(pages);
    return passportResponse;
  }

  public static PassportResponse reconstructFullName(PassportResponse passportResponse) {
    if (passportResponse.getPassport().getSurname() != null
        && passportResponse.getPassport().getSurname().getValue() != null
        && passportResponse.getPassport().getGivenNames().size() > 0
        && passportResponse.getPassport().getGivenNames().get(0).getValue() != null
        && passportResponse.getPassport().getFullName() == null
    ) {

      String fullName = new StringBuilder(
          passportResponse.getPassport().getGivenNames().get(0).getValue())
          .append(" ").append(passportResponse.getPassport().getSurname().getValue()).toString();
      PassportDocument document = passportResponse.getPassport().toBuilder()
          .fullName(Field.builder()
              .rawValue(fullName)
              .value(fullName)
              .reconstructed(Boolean.TRUE)
              .confidence(passportResponse.getPassport().getGivenNames().get(0).getConfidence()
                  * passportResponse.getPassport().getSurname().getConfidence())
              .build())
          .build();
      passportResponse.setPassport(document);
    }

    List<PassportPage> pages = new ArrayList<>();
    for (PassportPage passportPage : passportResponse.getPassports()) {
      if (passportPage.getSurname() != null
          && passportPage.getSurname().getValue() != null
          && passportPage.getGivenNames().size() > 0
          && passportPage.getGivenNames().get(0).getValue() != null
          && passportPage.getFullName() == null
      ) {
        String fullName = new StringBuilder(passportPage.getGivenNames().get(0).getValue())
            .append(" ").append(passportPage.getSurname().getValue()).toString();
        PassportPage page = passportPage.toBuilder()
            .fullName(Field.builder()
                .reconstructed(Boolean.TRUE)
                .rawValue(fullName)
                .value(fullName)
                .confidence(passportPage.getGivenNames().get(0).getConfidence()
                    * passportPage.getSurname().getConfidence())
                .build())
            .build();
        pages.add(page);
      } else {
        pages.add(passportPage);
      }
    }

    passportResponse.setPassports(pages);
    return passportResponse;
  }

}
