
package com.furniflow.service;

import com.furniflow.entity.Invoice;
import com.furniflow.entity.Payment;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.InvoiceRepository;
import com.furniflow.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    private Payment findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with ID: " + id));
    }

    private Invoice lockInvoice(Long invoiceId) {
        return invoiceRepository.findByIdForUpdate(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice not found with ID: " + invoiceId));
    }

    private void validatePayment(Payment payment) {

        if (payment.getAmount() == null ||
                payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero");
        }

        if (payment.getPaymentMethod() == null ||
                payment.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException(
                    "Payment method is required");
        }

        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        if (payment.getPaymentDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Payment date cannot be in the future");
        }
    }

    private BigDecimal calculatePaidAmount(
            Long invoiceId, Long excludedPaymentId) {

        return paymentRepository
                .findByInvoice_InvoiceId(invoiceId)
                .stream()
                .filter(p -> !p.getPaymentId()
                        .equals(excludedPaymentId))
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateInvoiceStatus(
            Invoice invoice, BigDecimal totalPaid) {

        int comparison = totalPaid.compareTo(
                invoice.getTotalAmount());

        if (comparison > 0) {
            throw new IllegalArgumentException(
                    "Total payments cannot exceed invoice amount");
        }

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus("UNPAID");
        } else if (comparison == 0) {
            invoice.setStatus("PAID");
        } else {
            invoice.setStatus("PARTIALLY_PAID");
        }

        invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Payment createPayment(
            Long invoiceId, Payment payment) {

        Invoice invoice = lockInvoice(invoiceId);

        validatePayment(payment);

        BigDecimal alreadyPaid =
                calculatePaidAmount(invoiceId, null);

        BigDecimal newTotal =
                alreadyPaid.add(payment.getAmount());

        if (newTotal.compareTo(invoice.getTotalAmount()) > 0) {
            throw new IllegalArgumentException(
                    "Total payments cannot exceed invoice amount");
        }

        Payment newPayment = new Payment();
        newPayment.setInvoice(invoice);
        newPayment.setAmount(payment.getAmount());
        newPayment.setPaymentDate(payment.getPaymentDate());
        newPayment.setPaymentMethod(payment.getPaymentMethod());

        Payment saved = paymentRepository.save(newPayment);

        updateInvoiceStatus(invoice, newTotal);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(Long paymentId) {
        return findPayment(paymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByInvoice(Long invoiceId) {

        if (!invoiceRepository.existsById(invoiceId)) {
            throw new ResourceNotFoundException(
                    "Invoice not found with ID: " + invoiceId);
        }

        return paymentRepository
                .findByInvoice_InvoiceId(invoiceId);
    }

    @Override
    @Transactional
    public Payment updatePayment(
            Long paymentId, Payment updatedPayment) {

        Payment existing = findPayment(paymentId);

        Invoice invoice = lockInvoice(
                existing.getInvoice().getInvoiceId());

        Payment candidate = new Payment();

        candidate.setAmount(
                updatedPayment.getAmount() != null
                        ? updatedPayment.getAmount()
                        : existing.getAmount());

        candidate.setPaymentMethod(
                updatedPayment.getPaymentMethod() != null
                        ? updatedPayment.getPaymentMethod()
                        : existing.getPaymentMethod());

        candidate.setPaymentDate(
                updatedPayment.getPaymentDate() != null
                        ? updatedPayment.getPaymentDate()
                        : existing.getPaymentDate());

        validatePayment(candidate);

        BigDecimal otherPayments = calculatePaidAmount(
                invoice.getInvoiceId(), paymentId);

        BigDecimal newTotal =
                otherPayments.add(candidate.getAmount());

        if (newTotal.compareTo(invoice.getTotalAmount()) > 0) {
            throw new IllegalArgumentException(
                    "Total payments cannot exceed invoice amount");
        }

        existing.setAmount(candidate.getAmount());
        existing.setPaymentMethod(candidate.getPaymentMethod());
        existing.setPaymentDate(candidate.getPaymentDate());

        Payment saved = paymentRepository.save(existing);

        updateInvoiceStatus(invoice, newTotal);

        return saved;
    }

    
@Override
@Transactional
public void deletePayment(Long paymentId) {

    Payment existing = findPayment(paymentId);

    Invoice invoice = lockInvoice(
            existing.getInvoice().getInvoiceId());

    BigDecimal remainingPaid = calculatePaidAmount(
            invoice.getInvoiceId(), paymentId);

    paymentRepository.delete(existing);

    updateInvoiceStatus(invoice, remainingPaid);
}

}
