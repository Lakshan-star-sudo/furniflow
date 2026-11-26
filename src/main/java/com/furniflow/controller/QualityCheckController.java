
package com.furniflow.controller;

import com.furniflow.dto.QualityCheckRequest;
import com.furniflow.dto.QualityCheckResponse;
import com.furniflow.entity.QualityCheck;
import com.furniflow.service.QualityCheckService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quality-checks")
public class QualityCheckController {

    private final QualityCheckService qualityCheckService;

    public QualityCheckController(
            QualityCheckService qualityCheckService) {
        this.qualityCheckService = qualityCheckService;
    }

    private QualityCheck toEntity(QualityCheckRequest request) {
        QualityCheck check = new QualityCheck();
        check.setCheckDate(request.checkDate());
        check.setResult(request.result());
        check.setRemarks(request.remarks());
        return check;
    }

    private QualityCheckResponse toResponse(QualityCheck check) {
        return new QualityCheckResponse(
                check.getQualityCheckId(),
                check.getProductionStage().getStageId(),
                check.getCheckDate(),
                check.getResult(),
                check.getRemarks()
        );
    }

    @PostMapping
    public ResponseEntity<QualityCheckResponse> create(
            @RequestParam Long stageId,
            @RequestBody QualityCheckRequest request) {

        QualityCheck saved = qualityCheckService.createQualityCheck(
                stageId, toEntity(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<QualityCheckResponse>> getAll() {

        List<QualityCheckResponse> responses = qualityCheckService
                .getAllQualityChecks()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QualityCheckResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                toResponse(qualityCheckService.getQualityCheckById(id)));
    }

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<List<QualityCheckResponse>> getByStage(
            @PathVariable Long stageId) {

        List<QualityCheckResponse> responses = qualityCheckService
                .getChecksByProductionStage(stageId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QualityCheckResponse> update(
            @PathVariable Long id,
            @RequestBody QualityCheckRequest request) {

        QualityCheck updated = qualityCheckService.updateQualityCheck(
                id, toEntity(request));

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        qualityCheckService.deleteQualityCheck(id);
        return ResponseEntity.noContent().build();
    }
}
