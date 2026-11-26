package com.furniflow.repository;

import com.furniflow.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByWorkOrderItem_WorkOrderItemId(Long workOrderItemId);

}