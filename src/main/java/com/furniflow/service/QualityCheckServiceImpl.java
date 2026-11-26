
package com.furniflow.service;

import com.furniflow.entity.ProductionStage;
import com.furniflow.entity.QualityCheck;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.ProductionStageRepository;
import com.furniflow.repository.QualityCheckRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class QualityCheckServiceImpl
        implements QualityCheckService {

    private static final Set<String> VALID_RESULTS =
            Set.of("PASSED", "FAILED");

    private final QualityCheckRepository qualityCheckRepository;
    private final ProductionStageRepository stageRepository;

    public QualityCheckServiceImpl(
            QualityCheckRepository qualityCheckRepository,
            ProductionStageRepository stageRepository) {

        this.qualityCheckRepository = qualityCheckRepository;
        this.stageRepository = stageRepository;
    }

    private QualityCheck findQualityCheck(Long id) {
        return qualityCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Quality check not found with ID: " + id));
    }

    private void validateResult(String result, String remarks) {

        if (result == null || !VALID_RESULTS.contains(result)) {
            throw new IllegalArgumentException(
                    "Result must be PASSED or FAILED");
        }

        if ("FAILED".equals(result) &&
                (remarks == null || remarks.isBlank())) {
            throw new IllegalArgumentException(
                    "Remarks are required for a failed quality check");
        }
    }

    @Override
    @Transactional
    public QualityCheck createQualityCheck(
            Long stageId, QualityCheck qualityCheck) {

        ProductionStage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Production stage not found with ID: " + stageId));

        if (!"COMPLETED".equals(stage.getStatus())) {
            throw new IllegalArgumentException(
                    "Production stage must be COMPLETED before quality check");
        }

        validateResult(
                qualityCheck.getResult(),
                qualityCheck.getRemarks());

        if (qualityCheck.getCheckDate() == null) {
            qualityCheck.setCheckDate(LocalDate.now());
        }

        if (qualityCheck.getCheckDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Quality check date cannot be in the future");
        }

        qualityCheck.setQualityCheckId(null);
        qualityCheck.setProductionStage(stage);

        return qualityCheckRepository.save(qualityCheck);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualityCheck> getAllQualityChecks() {
        return qualityCheckRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public QualityCheck getQualityCheckById(Long qualityCheckId) {
        return findQualityCheck(qualityCheckId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualityCheck> getChecksByProductionStage(Long stageId) {

        if (!stageRepository.existsById(stageId)) {
            throw new ResourceNotFoundException(
                    "Production stage not found with ID: " + stageId);
        }

        return qualityCheckRepository
                .findByProductionStage_StageId(stageId);
    }

    @Override
    @Transactional
    public QualityCheck updateQualityCheck(
            Long qualityCheckId, QualityCheck updatedCheck) {

        QualityCheck existing = findQualityCheck(qualityCheckId);

        String result = updatedCheck.getResult() != null
                ? updatedCheck.getResult()
                : existing.getResult();

        String remarks = updatedCheck.getRemarks() != null
                ? updatedCheck.getRemarks()
                : existing.getRemarks();

        validateResult(result, remarks);

        if (updatedCheck.getCheckDate() != null) {
            if (updatedCheck.getCheckDate().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Quality check date cannot be in the future");
            }

            existing.setCheckDate(updatedCheck.getCheckDate());
        }

        existing.setResult(result);
        existing.setRemarks(remarks);

        return qualityCheckRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteQualityCheck(Long qualityCheckId) {

        QualityCheck existing = findQualityCheck(qualityCheckId);
        qualityCheckRepository.delete(existing);
    }
}
