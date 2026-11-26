
package com.furniflow.controller;

import com.furniflow.dto.JobRequest;
import com.furniflow.dto.JobResponse;
import com.furniflow.entity.Job;
import com.furniflow.service.JobService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    private Job toEntity(JobRequest request) {
        Job job = new Job();
        job.setJobName(request.jobName());
        job.setStartDate(request.startDate());
        job.setEndDate(request.endDate());
        job.setStatus(request.status());
        return job;
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getJobId(),
                job.getWorkOrderItem().getWorkOrderItemId(),
                job.getJobName(),
                job.getStartDate(),
                job.getEndDate(),
                job.getStatus()
        );
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @RequestParam Long workOrderItemId,
            @RequestBody JobRequest request) {

        Job saved = jobService.createJob(
                workOrderItemId, toEntity(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {

        List<JobResponse> responses = jobService
                .getAllJobs()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                toResponse(jobService.getJobById(id)));
    }

    @GetMapping("/work-order-item/{workOrderItemId}")
    public ResponseEntity<List<JobResponse>> getJobsByWorkOrderItem(
            @PathVariable Long workOrderItemId) {

        List<JobResponse> responses = jobService
                .getJobsByWorkOrderItem(workOrderItemId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestBody JobRequest request) {

        Job updated = jobService.updateJob(
                id, toEntity(request));

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id) {

        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
