package com.sobey.module.productservice.entity.metricUsages;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/6/3 11:08
 * 封装服务使用概览(每天用量的折线图)结果
 */
public class MetricUsageView {

    private String dateTime;
    private List<MetricDetail> usages;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<MetricDetail> getUsages() {
        return usages;
    }

    public void setUsages(List<MetricDetail> usages) {
        this.usages = usages;
    }
}
