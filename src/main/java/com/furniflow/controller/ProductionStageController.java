
package com.furniflow.controller;

import com.furniflow.dto.ProductionStageRequest;
import com.furniflow.dto.ProductionStageResponse;
import com.furniflow.entity.ProductionStage;
import com.furniflow.service.ProductionStageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production-stages")
public class ProductionStageController {

    private final ProductionStageService stageService;

    public ProductionStageController(
            ProductionStageService stageService) {
        this.stageService = stageService;
    }

    private ProductionStage toEntity(
            ProductionStageRequest request) {

        ProductionStage stage = new ProductionStage();
        stage.setStageName(request.stageName());
        stage.setStartDate(request.startDate());
        stage.setEndDate(request.endDate());
        stage.setStatus(request.status());
        stage.setRemarks(request.remarks());

        return stage;
    }

    private ProductionStageResponse toResponse(
            ProductionStage stage) {

        return new ProductionStageResponse(
                stage.getStageId(),
                stage.getJob().getJobId(),
                stage.getStageName(),
                stage.getStartDate(),
                stage.getEndDate(),
                stage.getStatus(),
                stage.getRemarks()
        );
    }

    @PostMapping
    public ResponseEntity<ProductionStageResponse> create(
            @RequestParam Long jobId,
            @RequestBody ProductionStageRequest request) {

        ProductionStage saved =
                stageService.createProductionStage(
                        jobId, toEntity(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ProductionStageResponse>> getAll() {

        List<ProductionStageResponse> responses = stageService
                .getAllProductionStages()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionStageResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                toResponse(stageService.getProductionStageById(id)));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ProductionStageResponse>> getByJob(
            @PathVariable Long jobId) {

        List<ProductionStageResponse> responses = stageService
                .getStagesByJob(jobId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionStageResponse> update(
            @PathVariable Long id,
            @RequestBody ProductionStageRequest request) {

        ProductionStage updated =
                stageService.updateProductionStage(
                        id, toEntity(request));

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        stageService.deleteProductionStage(id);
        return ResponseEntity.noContent().build();
    }
}
