package com.mindee.model.documenttype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.documenttype.PassportV1Response.PassportDocument;
import com.mindee.model.documenttype.PassportV1Response.PassportPage;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Orientation;
import com.mindee.model.ocr.PageContent;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;


@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
@Data
public class PassportV1Response extends BaseDocumentResponse implements PredictionApiResponse<PassportDocument, PassportPage> {


  private PassportDocument document;
  private List<PassportPage> pages;

  @Override
  public String documentSummary() {
    StringBuilder stringBuilder = new StringBuilder("-----Passport data-----\n");
    stringBuilder.append(String.format("Filename: %s\n", this.getFilename()));
    stringBuilder.append(String.format("Full name: %s\n", this.document.getFullName().getValue()));
    stringBuilder.append(String.format("Given names: %s\n", this.document.getGivenNames().stream()
        .map(Field::getValue)
        .collect(Collectors.joining(" "))));
    stringBuilder.append(String.format("Surname: %s\n", this.document.getSurname().getValue()));
    stringBuilder.append(String.format("Country: %s\n", this.document.getCountry().getValue()));
    stringBuilder.append(String.format("ID Number: %s\n", this.document.getIdNumber().getValue()));
    stringBuilder.append(
        String.format("Issuance date: %s\n", this.document.getIssuanceDate().getValue()));
    stringBuilder.append(
        String.format("Birth date: %s\n", this.document.getBirthDate().getValue()));
    stringBuilder.append(
        String.format("Expiry date: %s\n", this.document.getExpiryDate().getValue()));
    stringBuilder.append(String.format("MRZ 1: %s\n", this.document.getMrz1().getValue()));
    stringBuilder.append(String.format("MRZ 2: %s\n", this.document.getMrz2().getValue()));
    stringBuilder.append(String.format("MRZ: %s\n", this.document.getMrz().getValue()));
    stringBuilder.append("----------------------");
    return stringBuilder.toString();
  }

  @AllArgsConstructor
  @Getter
  @EqualsAndHashCode
  @ToString
  @SuperBuilder(toBuilder = true)
  private static abstract class BasePassport {


    private final Field country;
    @JsonProperty("id_number")
    private final Field idNumber;
    @JsonProperty("birth_date")
    private final Date birthDate;
    @JsonProperty("expiry_date")
    private final Date expiryDate;
    @JsonProperty("issuance_date")
    private final Date issuanceDate;
    @JsonProperty("birth_place")
    private final Field birthPlace;
    private final Field gender;
    private final Field surname;
    private final Field mrz1;
    private final Field mrz2;
    @JsonProperty("given_names")
    private final List<Field> givenNames;
    private final Field mrz;
    private final Field fullName;

  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  @SuperBuilder(toBuilder = true)
  @Jacksonized
  public static final class PassportPage extends BasePassport implements PageLevelResponse {

    private final int page;
    private final Orientation orientation;
    @JsonProperty("page_content")
    private final PageContent fullText;

  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  @SuperBuilder(toBuilder = true)
  @Jacksonized
  public static final class PassportDocument extends BasePassport implements DocumentLevelResponse {

  }


}
