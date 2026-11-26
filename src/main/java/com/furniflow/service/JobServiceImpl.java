
package com.furniflow.service;

import com.furniflow.entity.Job;
import com.furniflow.entity.WorkOrderItem;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.JobRepository;
import com.furniflow.repository.WorkOrderItemRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class JobServiceImpl implements JobService {

    private static final Set<String> VALID_STATUSES =
            Set.of("PENDING", "IN_PROGRESS", "COMPLETED");

    private final JobRepository jobRepository;
    private final WorkOrderItemRepository itemRepository;

    public JobServiceImpl(
            JobRepository jobRepository,
            WorkOrderItemRepository itemRepository) {

        this.jobRepository = jobRepository;
        this.itemRepository = itemRepository;
    }

    private Job findJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job not found with ID: " + id));
    }

    private void validateStatus(String status) {
        if (status == null || !VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException(
                    "Status must be PENDING, IN_PROGRESS or COMPLETED");
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException(
                    "Start date is required");
        }

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date");
        }
    }

    @Override
    @Transactional
    public Job createJob(Long workOrderItemId, Job job) {

        WorkOrderItem item = itemRepository.findById(workOrderItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Work order item not found with ID: "
                                + workOrderItemId));

        if (job.getJobName() == null ||
                job.getJobName().isBlank()) {
            throw new IllegalArgumentException(
                    "Job name is required");
        }

        if (job.getStartDate() == null) {
            job.setStartDate(LocalDate.now());
        }

        validateDates(job.getStartDate(), job.getEndDate());

        if (job.getStatus() == null || job.getStatus().isBlank()) {
            job.setStatus("PENDING");
        }

        validateStatus(job.getStatus());

        job.setJobId(null);
        job.setWorkOrderItem(item);

        return jobRepository.save(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Job getJobById(Long jobId) {
        return findJob(jobId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getJobsByWorkOrderItem(Long workOrderItemId) {

        if (!itemRepository.existsById(workOrderItemId)) {
            throw new ResourceNotFoundException(
                    "Work order item not found with ID: "
                            + workOrderItemId);
        }

        return jobRepository
                .findByWorkOrderItem_WorkOrderItemId(workOrderItemId);
    }

    @Override
    @Transactional
    public Job updateJob(Long jobId, Job updatedJob) {

        Job existing = findJob(jobId);

        if (updatedJob.getJobName() != null) {
            if (updatedJob.getJobName().isBlank()) {
                throw new IllegalArgumentException(
                        "Job name cannot be blank");
            }
            existing.setJobName(updatedJob.getJobName());
        }

        if (updatedJob.getStartDate() != null) {
            existing.setStartDate(updatedJob.getStartDate());
        }

        if (updatedJob.getEndDate() != null) {
            existing.setEndDate(updatedJob.getEndDate());
        }

        if (updatedJob.getStatus() != null) {
            validateStatus(updatedJob.getStatus());
            existing.setStatus(updatedJob.getStatus());
        }

        validateDates(existing.getStartDate(), existing.getEndDate());

        return jobRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId) {
        Job existing = findJob(jobId);
        jobRepository.delete(existing);
    }
}
