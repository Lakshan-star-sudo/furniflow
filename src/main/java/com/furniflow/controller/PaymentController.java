
package com.furniflow.controller;

import com.furniflow.dto.PaymentRequest;
import com.furniflow.dto.PaymentResponse;
import com.furniflow.entity.Payment;
import com.furniflow.service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private Payment toEntity(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setPaymentDate(request.paymentDate());
        payment.setAmount(request.amount());
        payment.setPaymentMethod(request.paymentMethod());
        return payment;
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getInvoice().getInvoiceId(),
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getPaymentMethod()
        );
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @RequestParam Long invoiceId,
            @RequestBody PaymentRequest request) {

        Payment saved = paymentService.createPayment(
                invoiceId, toEntity(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {

        List<PaymentResponse> responses = paymentService
                .getAllPayments()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                toResponse(paymentService.getPaymentById(id)));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentResponse>> getByInvoice(
            @PathVariable Long invoiceId) {

        List<PaymentResponse> responses = paymentService
                .getPaymentsByInvoice(invoiceId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> update(
            @PathVariable Long id,
            @RequestBody PaymentRequest request) {

        Payment updated = paymentService.updatePayment(
                id, toEntity(request));

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
