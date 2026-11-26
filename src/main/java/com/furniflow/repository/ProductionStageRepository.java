package com.furniflow.repository;

import com.furniflow.entity.ProductionStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionStageRepository
        extends JpaRepository<ProductionStage, Long> {

    List<ProductionStage> findByJob_JobId(Long jobId);

}