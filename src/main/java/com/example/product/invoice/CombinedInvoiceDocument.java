package com.example.product.invoice;

import com.example.product.balticinvoice.BalticInvoiceV1Document;
import com.mindee.parsing.standard.StringField;
import com.mindee.product.invoice.InvoiceV4Document;
import lombok.Getter;

@Getter
abstract public class CombinedInvoiceDocument extends InvoiceV4Document {
  protected StringField invoiceSerialNumber;

  public abstract void combineWithBaltic(BalticInvoiceV1Document document);

  public String toString() {
    String outStr = super.toString();
    outStr += String.format(":Invoice Serial Number: %s%n", this.getInvoiceSerialNumber());
    return outStr;
  }
}
