package com.sobey.module.common.request;

import java.math.BigDecimal;

/**
 * @author WCY
 * @createTime 2020/8/13 10:52
 */
public class NoticePackResource {

    private String resourceName;
    private BigDecimal size;
    private String sizeUnit;
    private String metricId;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }
}
