
package com.furniflow.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "quality_checks")
public class QualityCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qualityCheckId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private ProductionStage productionStage;

    @Column(nullable = false)
    private LocalDate checkDate;

    @Column(nullable = false)
    private String result;

    private String remarks;

    public QualityCheck() {
    }

    public Long getQualityCheckId() {
        return qualityCheckId;
    }

    public void setQualityCheckId(Long qualityCheckId) {
        this.qualityCheckId = qualityCheckId;
    }

    public ProductionStage getProductionStage() {
        return productionStage;
    }

    public void setProductionStage(ProductionStage productionStage) {
        this.productionStage = productionStage;
    }

    public LocalDate getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
