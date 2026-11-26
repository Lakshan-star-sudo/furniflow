
package com.furniflow.service;

import com.furniflow.entity.QualityCheck;
import java.util.List;

public interface QualityCheckService {

    QualityCheck createQualityCheck(
            Long stageId, QualityCheck qualityCheck);

    List<QualityCheck> getAllQualityChecks();

    QualityCheck getQualityCheckById(Long qualityCheckId);

    List<QualityCheck> getChecksByProductionStage(Long stageId);

    QualityCheck updateQualityCheck(
            Long qualityCheckId, QualityCheck qualityCheck);

    void deleteQualityCheck(Long qualityCheckId);
}
