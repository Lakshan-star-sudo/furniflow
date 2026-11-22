package com.furniflow.service;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.entity.Customer;
import com.furniflow.entity.Quotation;

import com.furniflow.repository.CustomerRepository;
import com.furniflow.repository.QuotationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final CustomerRepository customerRepository;

    public QuotationServiceImpl(
            QuotationRepository quotationRepository,
            CustomerRepository customerRepository) {

        this.quotationRepository = quotationRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Quotation createQuotation(Quotation quotation) {

        if (quotation.getCustomer() == null ||
                quotation.getCustomer().getCustomerId() == null) {

            throw new RuntimeException("Customer is required");
        }

        Long customerId = quotation.getCustomer().getCustomerId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        quotation.setCustomer(customer);

        if (quotation.getQuotationDate() == null) {
            throw new ResourceNotFoundException("Quotation not found");
        }

        if (quotation.getValidUntil() == null) {
            throw new RuntimeException("Valid until date is required");
        }

        if (quotation.getValidUntil()
                .isBefore(quotation.getQuotationDate())) {

            throw new RuntimeException(
                    "Valid until date cannot be before quotation date");
        }

        if (quotation.getEstimatedPrice() == null ||
                quotation.getEstimatedPrice().signum() < 0) {

            throw new RuntimeException(
                    "Estimated price must be zero or greater");
        }

        if (quotation.getStatus() == null ||
                quotation.getStatus().isBlank()) {

            quotation.setStatus("DRAFT");
        }

        return quotationRepository.save(quotation);
    }

    @Override
    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }

    @Override
    public Quotation getQuotationById(Long id) {

        return quotationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));
    }

    @Override
    public Quotation updateQuotation(
            Long id,
            Quotation quotation) {

        Quotation existing = getQuotationById(id);

        existing.setQuotationDate(quotation.getQuotationDate());
        existing.setFurnitureDetails(quotation.getFurnitureDetails());
        existing.setEstimatedPrice(quotation.getEstimatedPrice());
        existing.setValidUntil(quotation.getValidUntil());
        existing.setStatus(quotation.getStatus());

        if (quotation.getCustomer() != null &&
                quotation.getCustomer().getCustomerId() != null) {

            Customer customer = customerRepository.findById(
                    quotation.getCustomer().getCustomerId()
            ).orElseThrow(() ->
                   new ResourceNotFoundException("Customer not found"));

            existing.setCustomer(customer);
        }

        return quotationRepository.save(existing);
    }

    @Override
    public void deleteQuotation(Long id) {

        Quotation existing = getQuotationById(id);

        quotationRepository.delete(existing);
    }
}