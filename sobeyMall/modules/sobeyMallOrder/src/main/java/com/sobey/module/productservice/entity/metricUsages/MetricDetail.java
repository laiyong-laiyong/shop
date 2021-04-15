package com.sobey.module.productservice.entity.metricUsages;

/**
 * @author WCY
 * @createTime 2020/6/3 11:10
 *
 */
public class MetricDetail {

    private String priceId;
    private String type;
    private String typeCode;
    private String unit;
    private double value;

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
