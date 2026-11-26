
package com.furniflow.service;

import com.furniflow.entity.Job;
import com.furniflow.entity.ProductionStage;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.JobRepository;
import com.furniflow.repository.ProductionStageRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class ProductionStageServiceImpl
        implements ProductionStageService {

    private static final Set<String> VALID_STATUSES =
            Set.of("PENDING", "IN_PROGRESS", "COMPLETED");

    private final ProductionStageRepository stageRepository;
    private final JobRepository jobRepository;

    public ProductionStageServiceImpl(
            ProductionStageRepository stageRepository,
            JobRepository jobRepository) {

        this.stageRepository = stageRepository;
        this.jobRepository = jobRepository;
    }

    private ProductionStage findStage(Long stageId) {
        return stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Production stage not found with ID: " + stageId));
    }

    private void validateStatus(String status) {
        if (status == null || !VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException(
                    "Status must be PENDING, IN_PROGRESS or COMPLETED");
        }
    }

    private void validateDates(
            LocalDate startDate, LocalDate endDate) {

        if (startDate != null && endDate != null
                && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date");
        }
    }

    @Override
    @Transactional
    public ProductionStage createProductionStage(
            Long jobId, ProductionStage stage) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job not found with ID: " + jobId));

        if (stage.getStageName() == null ||
                stage.getStageName().isBlank()) {
            throw new IllegalArgumentException(
                    "Stage name is required");
        }

        validateDates(
                stage.getStartDate(), stage.getEndDate());

        if (stage.getStatus() == null ||
                stage.getStatus().isBlank()) {
            stage.setStatus("PENDING");
        }

        validateStatus(stage.getStatus());

        stage.setStageId(null);
        stage.setJob(job);

        return stageRepository.save(stage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionStage> getAllProductionStages() {
        return stageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductionStage getProductionStageById(Long stageId) {
        return findStage(stageId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionStage> getStagesByJob(Long jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException(
                    "Job not found with ID: " + jobId);
        }

        return stageRepository.findByJob_JobId(jobId);
    }

    @Override
    @Transactional
    public ProductionStage updateProductionStage(
            Long stageId, ProductionStage updatedStage) {

        ProductionStage existing = findStage(stageId);

        if (updatedStage.getStageName() != null) {
            if (updatedStage.getStageName().isBlank()) {
                throw new IllegalArgumentException(
                        "Stage name cannot be blank");
            }
            existing.setStageName(updatedStage.getStageName());
        }

        if (updatedStage.getStartDate() != null) {
            existing.setStartDate(updatedStage.getStartDate());
        }

        if (updatedStage.getEndDate() != null) {
            existing.setEndDate(updatedStage.getEndDate());
        }

        if (updatedStage.getStatus() != null) {
            validateStatus(updatedStage.getStatus());
            existing.setStatus(updatedStage.getStatus());
        }

        if (updatedStage.getRemarks() != null) {
            existing.setRemarks(updatedStage.getRemarks());
        }

        validateDates(
                existing.getStartDate(), existing.getEndDate());

        return stageRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteProductionStage(Long stageId) {
        ProductionStage existing = findStage(stageId);
        stageRepository.delete(existing);
    }
}
