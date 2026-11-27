package com.furniflow.repository;

import com.furniflow.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByOrder_OrderId(Long orderId);

    boolean existsByOrder_OrderId(Long orderId);

@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT i FROM Invoice i WHERE i.invoiceId = :id")
Optional<Invoice> findByIdForUpdate(@Param("id") Long id);


}