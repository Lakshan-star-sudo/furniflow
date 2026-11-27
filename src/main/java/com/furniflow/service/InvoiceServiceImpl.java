
package com.furniflow.service;

import com.furniflow.entity.Invoice;
import com.furniflow.entity.Order;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.InvoiceRepository;
import com.furniflow.repository.OrderRepository;

import com.furniflow.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

   
public InvoiceServiceImpl(
        InvoiceRepository invoiceRepository,
        OrderRepository orderRepository,
        PaymentRepository paymentRepository) {

    this.invoiceRepository = invoiceRepository;
    this.orderRepository = orderRepository;
    this.paymentRepository = paymentRepository;
}


    private Invoice findInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice not found with ID: " + invoiceId));
    }

    private void validateDates(
            LocalDate invoiceDate, LocalDate dueDate) {

        if (invoiceDate == null || dueDate == null) {
            throw new IllegalArgumentException(
                    "Invoice date and due date are required");
        }

        if (dueDate.isBefore(invoiceDate)) {
            throw new IllegalArgumentException(
                    "Due date cannot be before invoice date");
        }
    }

    @Override
    @Transactional
    public Invoice createInvoice(Long orderId, Invoice invoice) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + orderId));

        if (invoiceRepository.existsByOrder_OrderId(orderId)) {
            throw new IllegalArgumentException(
                    "Invoice already exists for this order");
        }

        BigDecimal orderTotal = order.getTotalAmount();

        if (orderTotal == null ||
                orderTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Order must have a positive total amount");
        }

        LocalDate invoiceDate = invoice.getInvoiceDate();

        if (invoiceDate == null) {
            invoiceDate = LocalDate.now();
        }

        validateDates(invoiceDate, invoice.getDueDate());

        Invoice newInvoice = new Invoice();
        newInvoice.setOrder(order);
        newInvoice.setInvoiceDate(invoiceDate);
        newInvoice.setDueDate(invoice.getDueDate());
        newInvoice.setTotalAmount(orderTotal);
        newInvoice.setStatus("UNPAID");

        return invoiceRepository.save(newInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice getInvoiceById(Long invoiceId) {
        return findInvoice(invoiceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice getInvoiceByOrderId(Long orderId) {
        return invoiceRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice not found for Order ID: " + orderId));
    }

    @Override
    @Transactional
    public Invoice updateInvoice(
            Long invoiceId, Invoice updatedInvoice) {

        Invoice existing = findInvoice(invoiceId);

        if (!"UNPAID".equals(existing.getStatus())) {
            throw new IllegalArgumentException(
                    "Only unpaid invoices can be updated");
        }

        if (updatedInvoice.getDueDate() != null) {
            validateDates(
                    existing.getInvoiceDate(),
                    updatedInvoice.getDueDate());

            existing.setDueDate(updatedInvoice.getDueDate());
        }

        return invoiceRepository.save(existing);
    }

    
@Override
@Transactional
public void deleteInvoice(Long invoiceId) {

    Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Invoice not found with ID: " + invoiceId));

    if (paymentRepository.existsByInvoice_InvoiceId(invoiceId)) {
        throw new IllegalArgumentException(
                "Cannot delete invoice with recorded payments");
    }

    if (!"UNPAID".equals(invoice.getStatus())) {
        throw new IllegalArgumentException(
                "Only unpaid invoices can be deleted");
    }

    invoiceRepository.delete(invoice);
}

}
