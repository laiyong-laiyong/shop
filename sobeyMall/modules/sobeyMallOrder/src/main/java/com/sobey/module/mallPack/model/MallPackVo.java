package com.sobey.module.mallPack.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/5/21 17:10
 */
@ApiModel
public class MallPackVo {

    @ApiModelProperty(notes = "用户userCode")
    private String accountId;//购买用户id

    @ApiModelProperty(notes = "用户名")
    private String account;//购买用户

    @ApiModelProperty(notes = "套餐包id")
    private String packUuid;

    @ApiModelProperty(notes = "套餐包编码")
    private String packCode;

    @ApiModelProperty(notes = "套餐包名称")
    private String packName;

    @ApiModelProperty(notes = "商品id")
    private String productId;

    @ApiModelProperty(notes = "商品名称")
    private String productName;

    @ApiModelProperty(notes = "生效时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date effectiveDate;//生效时间

    @ApiModelProperty(notes = "失效时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expireDate;//失效时间

    @ApiModelProperty(notes = "是否生效,前端不传该参数")
    private String isEffective;//是否生效

    @ApiModelProperty(notes = "资源id,必传")
    private String resourceId;//资源id

    @ApiModelProperty(notes = "资源名称,存在则传")
    private String resourceName;//资源名称

    @ApiModelProperty(notes = "对应套餐包主键,前端不传该参数")
    private String mallPackId;//对应mall_pack套餐包主键

    @ApiModelProperty(notes = "资源下选项的")
    private String optionId;//选项id

    @ApiModelProperty(notes = "资源下选项的名称,保留字段")
    private String optionName;

    @ApiModelProperty(notes = "单价id,必传")
    private String metricId;//单价id

    @ApiModelProperty(notes = "容量大小,必传")
    private BigDecimal size; //容量大小

    @ApiModelProperty(notes = "余量,前端不传该参数")
    private BigDecimal remainingSize;

    @ApiModelProperty(notes = "容量单位,必传")
    private String sizeUnit;//容量单位

    @ApiModelProperty(notes = "资源价格")
    private BigDecimal price;//资源价格


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

    public String getPackUuid() {
        return packUuid;
    }

    public void setPackUuid(String packUuid) {
        this.packUuid = packUuid;
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

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(String isEffective) {
        this.isEffective = isEffective;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getMallPackId() {
        return mallPackId;
    }

    public void setMallPackId(String mallPackId) {
        this.mallPackId = mallPackId;
    }

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public BigDecimal getRemainingSize() {
        return remainingSize;
    }

    public void setRemainingSize(BigDecimal remainingSize) {
        this.remainingSize = remainingSize;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
