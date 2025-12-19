package com.mindee.product.fr.cartegrise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Carte Grise API version 1.1 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarteGriseV1Document extends Prediction {

  /**
   * The vehicle's license plate number.
   */
  @JsonProperty("a")
  protected StringField a;
  /**
   * The vehicle's first release date.
   */
  @JsonProperty("b")
  protected DateField b;
  /**
   * The vehicle owner's full name including maiden name.
   */
  @JsonProperty("c1")
  protected StringField c1;
  /**
   * The vehicle owner's address.
   */
  @JsonProperty("c3")
  protected StringField c3;
  /**
   * Number of owners of the license certificate.
   */
  @JsonProperty("c41")
  protected StringField c41;
  /**
   * Mentions about the ownership of the vehicle.
   */
  @JsonProperty("c4a")
  protected StringField c4A;
  /**
   * The vehicle's brand.
   */
  @JsonProperty("d1")
  protected StringField d1;
  /**
   * The vehicle's commercial name.
   */
  @JsonProperty("d3")
  protected StringField d3;
  /**
   * The Vehicle Identification Number (VIN).
   */
  @JsonProperty("e")
  protected StringField e;
  /**
   * The vehicle's maximum admissible weight.
   */
  @JsonProperty("f1")
  protected StringField f1;
  /**
   * The vehicle's maximum admissible weight within the license's state.
   */
  @JsonProperty("f2")
  protected StringField f2;
  /**
   * The vehicle's maximum authorized weight with coupling.
   */
  @JsonProperty("f3")
  protected StringField f3;
  /**
   * The document's formula number.
   */
  @JsonProperty("formula_number")
  protected StringField formulaNumber;
  /**
   * The vehicle's weight with coupling if tractor different than category M1.
   */
  @JsonProperty("g")
  protected StringField g;
  /**
   * The vehicle's national empty weight.
   */
  @JsonProperty("g1")
  protected StringField g1;
  /**
   * The car registration date of the given certificate.
   */
  @JsonProperty("i")
  protected DateField i;
  /**
   * The vehicle's category.
   */
  @JsonProperty("j")
  protected StringField j;
  /**
   * The vehicle's national type.
   */
  @JsonProperty("j1")
  protected StringField j1;
  /**
   * The vehicle's body type (CE).
   */
  @JsonProperty("j2")
  protected StringField j2;
  /**
   * The vehicle's body type (National designation).
   */
  @JsonProperty("j3")
  protected StringField j3;
  /**
   * Machine Readable Zone, first line.
   */
  @JsonProperty("mrz1")
  protected StringField mrz1;
  /**
   * Machine Readable Zone, second line.
   */
  @JsonProperty("mrz2")
  protected StringField mrz2;
  /**
   * The vehicle's owner first name.
   */
  @JsonProperty("owner_first_name")
  protected StringField ownerFirstName;
  /**
   * The vehicle's owner surname.
   */
  @JsonProperty("owner_surname")
  protected StringField ownerSurname;
  /**
   * The vehicle engine's displacement (cm3).
   */
  @JsonProperty("p1")
  protected StringField p1;
  /**
   * The vehicle's maximum net power (kW).
   */
  @JsonProperty("p2")
  protected StringField p2;
  /**
   * The vehicle's fuel type or energy source.
   */
  @JsonProperty("p3")
  protected StringField p3;
  /**
   * The vehicle's administrative power (fiscal horsepower).
   */
  @JsonProperty("p6")
  protected StringField p6;
  /**
   * The vehicle's power to weight ratio.
   */
  @JsonProperty("q")
  protected StringField q;
  /**
   * The vehicle's number of seats.
   */
  @JsonProperty("s1")
  protected StringField s1;
  /**
   * The vehicle's number of standing rooms (person).
   */
  @JsonProperty("s2")
  protected StringField s2;
  /**
   * The vehicle's sound level (dB).
   */
  @JsonProperty("u1")
  protected StringField u1;
  /**
   * The vehicle engine's rotation speed (RPM).
   */
  @JsonProperty("u2")
  protected StringField u2;
  /**
   * The vehicle's CO2 emission (g/km).
   */
  @JsonProperty("v7")
  protected StringField v7;
  /**
   * Next technical control date.
   */
  @JsonProperty("x1")
  protected StringField x1;
  /**
   * Amount of the regional proportional tax of the registration (in euros).
   */
  @JsonProperty("y1")
  protected StringField y1;
  /**
   * Amount of the additional parafiscal tax of the registration (in euros).
   */
  @JsonProperty("y2")
  protected StringField y2;
  /**
   * Amount of the additional CO2 tax of the registration (in euros).
   */
  @JsonProperty("y3")
  protected StringField y3;
  /**
   * Amount of the fee for managing the registration (in euros).
   */
  @JsonProperty("y4")
  protected StringField y4;
  /**
   * Amount of the fee for delivery of the registration certificate in euros.
   */
  @JsonProperty("y5")
  protected StringField y5;
  /**
   * Total amount of registration fee to be paid in euros.
   */
  @JsonProperty("y6")
  protected StringField y6;

  @Override
  public boolean isEmpty() {
    return (this.a == null
      && this.b == null
      && this.c1 == null
      && this.c3 == null
      && this.c41 == null
      && this.c4A == null
      && this.d1 == null
      && this.d3 == null
      && this.e == null
      && this.f1 == null
      && this.f2 == null
      && this.f3 == null
      && this.g == null
      && this.g1 == null
      && this.i == null
      && this.j == null
      && this.j1 == null
      && this.j2 == null
      && this.j3 == null
      && this.p1 == null
      && this.p2 == null
      && this.p3 == null
      && this.p6 == null
      && this.q == null
      && this.s1 == null
      && this.s2 == null
      && this.u1 == null
      && this.u2 == null
      && this.v7 == null
      && this.x1 == null
      && this.y1 == null
      && this.y2 == null
      && this.y3 == null
      && this.y4 == null
      && this.y5 == null
      && this.y6 == null
      && this.formulaNumber == null
      && this.ownerFirstName == null
      && this.ownerSurname == null
      && this.mrz1 == null
      && this.mrz2 == null);
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":a: %s%n", this.getA()));
    outStr.append(String.format(":b: %s%n", this.getB()));
    outStr.append(String.format(":c1: %s%n", this.getC1()));
    outStr.append(String.format(":c3: %s%n", this.getC3()));
    outStr.append(String.format(":c41: %s%n", this.getC41()));
    outStr.append(String.format(":c4a: %s%n", this.getC4A()));
    outStr.append(String.format(":d1: %s%n", this.getD1()));
    outStr.append(String.format(":d3: %s%n", this.getD3()));
    outStr.append(String.format(":e: %s%n", this.getE()));
    outStr.append(String.format(":f1: %s%n", this.getF1()));
    outStr.append(String.format(":f2: %s%n", this.getF2()));
    outStr.append(String.format(":f3: %s%n", this.getF3()));
    outStr.append(String.format(":g: %s%n", this.getG()));
    outStr.append(String.format(":g1: %s%n", this.getG1()));
    outStr.append(String.format(":i: %s%n", this.getI()));
    outStr.append(String.format(":j: %s%n", this.getJ()));
    outStr.append(String.format(":j1: %s%n", this.getJ1()));
    outStr.append(String.format(":j2: %s%n", this.getJ2()));
    outStr.append(String.format(":j3: %s%n", this.getJ3()));
    outStr.append(String.format(":p1: %s%n", this.getP1()));
    outStr.append(String.format(":p2: %s%n", this.getP2()));
    outStr.append(String.format(":p3: %s%n", this.getP3()));
    outStr.append(String.format(":p6: %s%n", this.getP6()));
    outStr.append(String.format(":q: %s%n", this.getQ()));
    outStr.append(String.format(":s1: %s%n", this.getS1()));
    outStr.append(String.format(":s2: %s%n", this.getS2()));
    outStr.append(String.format(":u1: %s%n", this.getU1()));
    outStr.append(String.format(":u2: %s%n", this.getU2()));
    outStr.append(String.format(":v7: %s%n", this.getV7()));
    outStr.append(String.format(":x1: %s%n", this.getX1()));
    outStr.append(String.format(":y1: %s%n", this.getY1()));
    outStr.append(String.format(":y2: %s%n", this.getY2()));
    outStr.append(String.format(":y3: %s%n", this.getY3()));
    outStr.append(String.format(":y4: %s%n", this.getY4()));
    outStr.append(String.format(":y5: %s%n", this.getY5()));
    outStr.append(String.format(":y6: %s%n", this.getY6()));
    outStr.append(String.format(":Formula Number: %s%n", this.getFormulaNumber()));
    outStr.append(String.format(":Owner's First Name: %s%n", this.getOwnerFirstName()));
    outStr.append(String.format(":Owner's Surname: %s%n", this.getOwnerSurname()));
    outStr.append(String.format(":MRZ Line 1: %s%n", this.getMrz1()));
    outStr.append(String.format(":MRZ Line 2: %s%n", this.getMrz2()));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
