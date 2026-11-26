
package com.furniflow.service;

import com.furniflow.entity.Job;
import java.util.List;

public interface JobService {

    Job createJob(Long workOrderItemId, Job job);

    List<Job> getAllJobs();

    Job getJobById(Long jobId);

    List<Job> getJobsByWorkOrderItem(Long workOrderItemId);

    Job updateJob(Long jobId, Job job);

    void deleteJob(Long jobId);
}
