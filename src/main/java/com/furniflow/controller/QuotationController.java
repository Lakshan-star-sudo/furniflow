package com.furniflow.controller;

import com.furniflow.entity.Quotation;
import com.furniflow.service.QuotationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @PostMapping
    public ResponseEntity<Quotation> createQuotation(
            @RequestBody Quotation quotation) {

        return ResponseEntity.ok(
                quotationService.createQuotation(quotation)
        );
    }

    @GetMapping
    public ResponseEntity<List<Quotation>> getAllQuotations() {

        return ResponseEntity.ok(
                quotationService.getAllQuotations()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quotation> getQuotationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                quotationService.getQuotationById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quotation> updateQuotation(
            @PathVariable Long id,
            @RequestBody Quotation quotation) {

        return ResponseEntity.ok(
                quotationService.updateQuotation(id, quotation)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotation(
            @PathVariable Long id) {

        quotationService.deleteQuotation(id);

        return ResponseEntity.noContent().build();
    }
}