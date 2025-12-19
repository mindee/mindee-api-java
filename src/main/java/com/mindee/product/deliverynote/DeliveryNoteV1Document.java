package com.mindee.product.deliverynote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Delivery note API version 1.2 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryNoteV1Document extends Prediction {

  /**
   * The address of the customer receiving the goods.
   */
  @JsonProperty("customer_address")
  protected StringField customerAddress;
  /**
   * The name of the customer receiving the goods.
   */
  @JsonProperty("customer_name")
  protected StringField customerName;
  /**
   * The date on which the delivery is scheduled to arrive.
   */
  @JsonProperty("delivery_date")
  protected DateField deliveryDate;
  /**
   * A unique identifier for the delivery note.
   */
  @JsonProperty("delivery_number")
  protected StringField deliveryNumber;
  /**
   * The address of the supplier providing the goods.
   */
  @JsonProperty("supplier_address")
  protected StringField supplierAddress;
  /**
   * The name of the supplier providing the goods.
   */
  @JsonProperty("supplier_name")
  protected StringField supplierName;
  /**
   * The total monetary value of the goods being delivered.
   */
  @JsonProperty("total_amount")
  protected AmountField totalAmount;

  @Override
  public boolean isEmpty() {
    return (this.deliveryDate == null
      && this.deliveryNumber == null
      && this.supplierName == null
      && this.supplierAddress == null
      && this.customerName == null
      && this.customerAddress == null
      && this.totalAmount == null);
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Delivery Date: %s%n", this.getDeliveryDate()));
    outStr.append(String.format(":Delivery Number: %s%n", this.getDeliveryNumber()));
    outStr.append(String.format(":Supplier Name: %s%n", this.getSupplierName()));
    outStr.append(String.format(":Supplier Address: %s%n", this.getSupplierAddress()));
    outStr.append(String.format(":Customer Name: %s%n", this.getCustomerName()));
    outStr.append(String.format(":Customer Address: %s%n", this.getCustomerAddress()));
    outStr.append(String.format(":Total Amount: %s%n", this.getTotalAmount()));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
