
package com.furniflow.service;

import com.furniflow.entity.Invoice;
import java.util.List;

public interface InvoiceService {

    Invoice createInvoice(Long orderId, Invoice invoice);

    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(Long invoiceId);

    Invoice getInvoiceByOrderId(Long orderId);

    Invoice updateInvoice(Long invoiceId, Invoice invoice);

    void deleteInvoice(Long invoiceId);
}
