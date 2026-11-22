package com.furniflow.service;

import com.furniflow.entity.Quotation;

import java.util.List;

public interface QuotationService {

    Quotation createQuotation(Quotation quotation);

    List<Quotation> getAllQuotations();

    Quotation getQuotationById(Long id);

    Quotation updateQuotation(Long id, Quotation quotation);

    void deleteQuotation(Long id);
}