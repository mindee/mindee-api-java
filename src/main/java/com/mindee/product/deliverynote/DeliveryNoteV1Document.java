package com.mindee.product.deliverynote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Delivery note API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryNoteV1Document extends Prediction {

  /**
   * The Customer Address field is used to store the address of the customer receiving the goods.
   */
  @JsonProperty("customer_address")
  protected StringField customerAddress;
  /**
   * The Customer Name field is used to store the name of the customer who is receiving the goods.
   */
  @JsonProperty("customer_name")
  protected StringField customerName;
  /**
   * Delivery Date is the date when the goods are expected to be delivered to the customer.
   */
  @JsonProperty("delivery_date")
  protected StringField deliveryDate;
  /**
   * Delivery Number is a unique identifier for a Global Delivery Note document.
   */
  @JsonProperty("delivery_number")
  protected StringField deliveryNumber;
  /**
   * The Supplier Address field is used to store the address of the supplier from whom the goods were purchased.
   */
  @JsonProperty("supplier_address")
  protected StringField supplierAddress;
  /**
   * Supplier Name field is used to capture the name of the supplier from whom the goods are being received.
   */
  @JsonProperty("supplier_name")
  protected StringField supplierName;
  /**
   * Total Amount field is the sum of all line items on the Global Delivery Note.
   */
  @JsonProperty("total_amount")
  protected StringField totalAmount;

  @Override
  public boolean isEmpty() {
    return (
      this.deliveryDate == null
      && this.deliveryNumber == null
      && this.supplierName == null
      && this.supplierAddress == null
      && this.customerName == null
      && this.customerAddress == null
      && this.totalAmount == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Delivery Date: %s%n", this.getDeliveryDate())
    );
    outStr.append(
        String.format(":Delivery Number: %s%n", this.getDeliveryNumber())
    );
    outStr.append(
        String.format(":Supplier Name: %s%n", this.getSupplierName())
    );
    outStr.append(
        String.format(":Supplier Address: %s%n", this.getSupplierAddress())
    );
    outStr.append(
        String.format(":Customer Name: %s%n", this.getCustomerName())
    );
    outStr.append(
        String.format(":Customer Address: %s%n", this.getCustomerAddress())
    );
    outStr.append(
        String.format(":Total Amount: %s%n", this.getTotalAmount())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
