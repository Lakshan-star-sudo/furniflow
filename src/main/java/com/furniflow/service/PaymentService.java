
package com.furniflow.service;

import com.furniflow.entity.Payment;
import java.util.List;

public interface PaymentService {

    Payment createPayment(Long invoiceId, Payment payment);

    List<Payment> getAllPayments();

    Payment getPaymentById(Long paymentId);

    List<Payment> getPaymentsByInvoice(Long invoiceId);

    Payment updatePayment(Long paymentId, Payment payment);

    void deletePayment(Long paymentId);
}
