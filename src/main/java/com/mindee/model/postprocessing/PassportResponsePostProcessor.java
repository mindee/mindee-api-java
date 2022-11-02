package com.mindee.model.postprocessing;

import com.mindee.model.documenttype.PassportV1Response;
import com.mindee.model.documenttype.PassportV1Response.PassportDocument;
import com.mindee.model.documenttype.PassportV1Response.PassportPage;
import com.mindee.model.fields.Field;
import java.util.ArrayList;
import java.util.List;

public class PassportResponsePostProcessor {

  private PassportResponsePostProcessor() {
  }

  public static PassportV1Response reconstructMrz(PassportV1Response passportV1Response) {

    PassportDocument passportDocument = passportV1Response.getDocument();
    if (passportDocument.getMrz1() != null && passportDocument.getMrz1().getValue() != null
        && passportDocument.getMrz2() != null && passportDocument.getMrz2().getValue() != null
        && passportDocument.getMrz() == null) {
      String mrz = new StringBuilder(passportDocument.getMrz1().getValue())
          .append(passportDocument.getMrz2().getValue()).toString();
      PassportDocument document = passportDocument.toBuilder()
          .mrz(Field.builder()
              .value(mrz)
              .reconstructed(Boolean.TRUE)
              .confidence(passportDocument.getMrz1().getConfidence() * passportDocument.getMrz2()
                  .getConfidence())
              .build())
          .build();
      passportV1Response.setDocument(document);
    }

    List<PassportPage> pages = new ArrayList<>();
    for (PassportPage passportPage : passportV1Response.getPages()) {
      if (passportPage.getMrz1() != null && passportPage.getMrz1().getValue() != null
          && passportPage.getMrz2() != null && passportPage.getMrz2().getValue() != null
          && passportPage.getMrz() == null) {
        String mrz = new StringBuilder(passportPage.getMrz1().getValue())
            .append(passportPage.getMrz2().getValue()).toString();
        PassportPage page = passportPage.toBuilder()
            .mrz(Field.builder()
                .value(mrz)
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
    passportV1Response.setPages(pages);
    return passportV1Response;
  }

  public static PassportV1Response reconstructFullName(PassportV1Response passportV1Response) {
    if (passportV1Response.getDocument().getSurname() != null
        && passportV1Response.getDocument().getSurname().getValue() != null
        && passportV1Response.getDocument().getGivenNames().size() > 0
        && passportV1Response.getDocument().getGivenNames().get(0).getValue() != null
        && passportV1Response.getDocument().getFullName() == null
    ) {

      String fullName = new StringBuilder(
          passportV1Response.getDocument().getGivenNames().get(0).getValue())
          .append(" ").append(passportV1Response.getDocument().getSurname().getValue()).toString();
      PassportDocument document = passportV1Response.getDocument().toBuilder()
          .fullName(Field.builder()
              .value(fullName)
              .reconstructed(Boolean.TRUE)
              .confidence(passportV1Response.getDocument().getGivenNames().get(0).getConfidence()
                  * passportV1Response.getDocument().getSurname().getConfidence())
              .build())
          .build();
      passportV1Response.setDocument(document);
    }

    List<PassportPage> pages = new ArrayList<>();
    for (PassportPage passportPage : passportV1Response.getPages()) {
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

    passportV1Response.setPages(pages);
    return passportV1Response;
  }

}
