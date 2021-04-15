package com.sobey.module.fegin.mallPack.packOrder.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/5/20 9:58
 * 套餐包含的资源信息
 */
@ApiModel
public class MallPackResource{

    private String uuid;

    @ApiModelProperty(notes = "资源id,必传",required = true)
    private String resourceId;//资源id

    @ApiModelProperty(notes = "资源名称,存在则传")
    private String resourceName;//资源名称

    @ApiModelProperty(notes = "对应套餐包主键,前端不传该参数")
    private String mallPackId;//对应mall_pack套餐包主键

    @ApiModelProperty(notes = "单价id,必传",required = true)
    private String metricId;//单价id

    @ApiModelProperty(notes = "资源下选项的id,必传",required = true)
    private String optionId;//选项id

    @ApiModelProperty(notes = "资源下选项的名称,保留字段")
    private String optionName;

    @ApiModelProperty(notes = "容量大小,必传",required = true)
    private BigDecimal size; //容量大小

    @ApiModelProperty(notes = "余量,前端不传该参数")
    private BigDecimal remainingSize;

    @ApiModelProperty(notes = "容量单位,必传",required = true)
    private String sizeUnit;//容量单位

    @ApiModelProperty(notes = "资源价格")
    private BigDecimal price;//资源价格

    @ApiModelProperty(notes = "创建时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(notes = "更新时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    @ApiModelProperty(notes = "套餐包失效时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expireDate;

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
