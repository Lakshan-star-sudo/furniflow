
package com.furniflow.controller;

import com.furniflow.dto.InvoiceRequest;
import com.furniflow.dto.InvoiceResponse;
import com.furniflow.entity.Invoice;
import com.furniflow.service.InvoiceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    private Invoice toEntity(InvoiceRequest request) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(request.invoiceDate());
        invoice.setDueDate(request.dueDate());
        return invoice;
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getInvoiceId(),
                invoice.getOrder().getOrderId(),
                invoice.getInvoiceDate(),
                invoice.getDueDate(),
                invoice.getTotalAmount(),
                invoice.getStatus()
        );
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> create(
            @RequestParam Long orderId,
            @RequestBody InvoiceRequest request) {

        Invoice saved = invoiceService.createInvoice(
                orderId, toEntity(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAll() {

        List<InvoiceResponse> responses = invoiceService
                .getAllInvoices()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                toResponse(invoiceService.getInvoiceById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<InvoiceResponse> getByOrder(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                toResponse(invoiceService.getInvoiceByOrderId(orderId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> update(
            @PathVariable Long id,
            @RequestBody InvoiceRequest request) {

        Invoice updated = invoiceService.updateInvoice(
                id, toEntity(request));

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
