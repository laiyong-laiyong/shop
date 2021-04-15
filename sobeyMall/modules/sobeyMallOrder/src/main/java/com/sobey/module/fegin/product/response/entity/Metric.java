package com.sobey.module.fegin.product.response.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
public class Metric {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String uuid;

    private String name;


    private String productId;


    private String type;

    private String operationType;

    private String order;
    private BigDecimal price;

    private BigDecimal limitedPrice;//限价

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public BigDecimal getLimitedPrice() {
        return limitedPrice;
    }

    public void setLimitedPrice(BigDecimal limitedPrice) {
        this.limitedPrice = limitedPrice;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private List<VersionCustom> customs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public List<VersionCustom> getCustoms() {
        return customs;
    }

    public void setCustoms(List<VersionCustom> customs) {
        this.customs = customs;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }


}
