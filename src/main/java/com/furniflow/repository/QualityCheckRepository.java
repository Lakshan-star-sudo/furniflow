package com.furniflow.repository;

import com.furniflow.entity.QualityCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualityCheckRepository
        extends JpaRepository<QualityCheck, Long> {

    List<QualityCheck> findByProductionStage_StageId(Long stageId);

}