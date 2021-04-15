package com.sobey.module.mallPack.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/5/20 10:38
 * 套餐包使用记录
 */
@ApiModel
public class MallPackUseRecord {

    private String id;

    @ApiModelProperty(notes = "站点")
    private String siteCode;

    @ApiModelProperty(notes = "用户userCode")
    private String accountId;

    @ApiModelProperty(notes = "用户")
    private String account;

    @ApiModelProperty(notes = "商品id")
    private String productId;

    @ApiModelProperty(notes = "商品名称")
    private String productName;

    @ApiModelProperty(notes = "")
    private String mallPackId;//套餐包表mall_pack主键

    @ApiModelProperty(notes = "套餐包编码,保留字段")
    private String packCode;

    @ApiModelProperty(notes = "套餐包名称")
    private String packName;

    @ApiModelProperty(notes = "")
    private String mallPackResourceId;//资源表mall_pack_resource的主键

    @ApiModelProperty(notes = "资源名称")
    private String resourceName;//资源名称

    @ApiModelProperty(notes = "单价id")
    private String metricId;

    @ApiModelProperty(notes = "选项id")
    private String optionId;

    @ApiModelProperty(notes = "选项名称保留字段")
    private String optionName;

    @ApiModelProperty(notes = "使用量")
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal usageAmount;//使用量

    @ApiModelProperty(notes = "使用前总量")
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal beforeUsageAmount;//使用前总量

    @ApiModelProperty(notes = "使用后余量")
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal afterUsageAmount;//使用后余量

    @ApiModelProperty(notes = "单位")
    private String sizeUnit;//单位

    @ApiModelProperty(notes = "抵扣时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date consumptionDate;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMallPackId() {
        return mallPackId;
    }

    public void setMallPackId(String mallPackId) {
        this.mallPackId = mallPackId;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getMallPackResourceId() {
        return mallPackResourceId;
    }

    public void setMallPackResourceId(String mallPackResourceId) {
        this.mallPackResourceId = mallPackResourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public BigDecimal getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(BigDecimal usageAmount) {
        this.usageAmount = usageAmount;
    }

    public BigDecimal getBeforeUsageAmount() {
        return beforeUsageAmount;
    }

    public void setBeforeUsageAmount(BigDecimal beforeUsageAmount) {
        this.beforeUsageAmount = beforeUsageAmount;
    }

    public BigDecimal getAfterUsageAmount() {
        return afterUsageAmount;
    }

    public void setAfterUsageAmount(BigDecimal afterUsageAmount) {
        this.afterUsageAmount = afterUsageAmount;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public Date getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(Date consumptionDate) {
        this.consumptionDate = consumptionDate;
    }
}
