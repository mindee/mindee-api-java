package com.example.product.invoice;

import com.example.product.balticinvoice.BalticInvoiceV1;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.http.EndpointInfo;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.Page;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "invoices", version = "4")
public class InvoiceEst extends Inference<InvoiceEstDocument, InvoiceEstDocument> {
  /**
   * Combine the invoice inference with the baltic invoice inference.
   */
  public void combineWithBaltic(BalticInvoiceV1 balticInvoice) {
    this.getPrediction().combineWithBaltic(balticInvoice.getPrediction());
    this.pages = null;
  }
}
