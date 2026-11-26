
package com.furniflow.service;

import com.furniflow.entity.ProductionStage;
import java.util.List;

public interface ProductionStageService {

    ProductionStage createProductionStage(
            Long jobId, ProductionStage stage);

    List<ProductionStage> getAllProductionStages();

    ProductionStage getProductionStageById(Long stageId);

    List<ProductionStage> getStagesByJob(Long jobId);

    ProductionStage updateProductionStage(
            Long stageId, ProductionStage stage);

    void deleteProductionStage(Long stageId);
}
