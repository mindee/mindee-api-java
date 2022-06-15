package com.mindee.model.documenttype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.deserialization.PassportDeserializer;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Orientation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = PassportDeserializer.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
@Data
public class PassportResponse extends BaseDocumentResponse {


  private PassportDocument passport;
  private List<PassportPage> passports;

  @AllArgsConstructor
  @Getter
  @EqualsAndHashCode
  @ToString
  private static abstract class BasePassport {

    private final Field country;
    private final Field id_number;
    private final Date birthDate;
    private final Date expiryDate;
    private final Date issuanceDate;
    private final Field birthPlace;
    private final Field gender;
    private final Field surname;
    private final Field mrz1;
    private final Field mrz2;
    private final List<Field> givenNames;
    private final Field mrz;
    private final Field fullName;

  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  public static final class PassportPage extends BasePassport {

    private final int page;
    private final Orientation orientation;

    @Builder(toBuilder = true)
    public PassportPage(@Builder.ObtainVia(method = "getCountry") Field country,
        @Builder.ObtainVia(method = "getId_number") Field id_number,
        @Builder.ObtainVia(method = "getBirthDate") Date birthDate,
        @Builder.ObtainVia(method = "getExpiryDate") Date expiryDate,
        @Builder.ObtainVia(method = "getIssuanceDate") Date issuanceDate,
        @Builder.ObtainVia(method = "getBirthPlace") Field birthPlace,
        @Builder.ObtainVia(method = "getGender") Field gender,
        @Builder.ObtainVia(method = "getSurname") Field surname,
        @Builder.ObtainVia(method = "getMrz1") Field mrz1,
        @Builder.ObtainVia(method = "getMrz2") Field mrz2,
        @Builder.ObtainVia(method = "getGivenNames") List<Field> givenNames,
        @Builder.ObtainVia(method = "getMrz") Field mrz,
        @Builder.ObtainVia(method = "getFullName") Field fullName,
        int page, Orientation orientation) {
      super(country, id_number, birthDate, expiryDate, issuanceDate, birthPlace, gender, surname,
          mrz1,
          mrz2, givenNames, mrz, fullName);
      this.page = page;
      this.orientation = orientation;
    }


  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  public static final class PassportDocument extends BasePassport {

    @Builder(toBuilder = true)
    public PassportDocument(@Builder.ObtainVia(method = "getCountry") Field country,
        @Builder.ObtainVia(method = "getId_number") Field id_number,
        @Builder.ObtainVia(method = "getBirthDate") Date birthDate,
        @Builder.ObtainVia(method = "getExpiryDate") Date expiryDate,
        @Builder.ObtainVia(method = "getIssuanceDate") Date issuanceDate,
        @Builder.ObtainVia(method = "getBirthPlace") Field birthPlace,
        @Builder.ObtainVia(method = "getGender") Field gender,
        @Builder.ObtainVia(method = "getSurname") Field surname,
        @Builder.ObtainVia(method = "getMrz1") Field mrz1,
        @Builder.ObtainVia(method = "getMrz2") Field mrz2,
        @Builder.ObtainVia(method = "getGivenNames") List<Field> givenNames,
        @Builder.ObtainVia(method = "getMrz") Field mrz,
        @Builder.ObtainVia(method = "getFullName") Field fullName) {
      super(country, id_number, birthDate, expiryDate, issuanceDate, birthPlace, gender, surname,
          mrz1,
          mrz2, givenNames, mrz, fullName);
    }
  }


}
