package com.sobey.module.mallPack.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
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
@TableName(value = "mall_pack_resource")
public class MallPackResource extends SuperModel<MallPackResource> {

    @TableField(value = "resourceId")
    @ApiModelProperty(notes = "资源id,必传",required = true)
    private String resourceId;//资源id

    @TableField(value = "resourceName")
    @ApiModelProperty(notes = "资源名称,存在则传")
    private String resourceName;//资源名称

    @TableField(value = "optionId")
    @ApiModelProperty(notes = "资源下选项的id,必传",required = true)
    private String optionId;//选项id

    @TableField(value = "optionName")
    @ApiModelProperty(notes = "资源下选项的名称,保留字段")
    private String optionName;

    @TableField(value = "mallPackId")
    @ApiModelProperty(notes = "对应套餐包主键,前端不传该参数")
    private String mallPackId;//对应mall_pack套餐包主键

    @TableField(value = "metricId")
    @ApiModelProperty(notes = "单价id,必传",required = true)
    private String metricId;//单价id

    @TableField(value = "size")
    @ApiModelProperty(notes = "容量大小,必传",required = true)
    private BigDecimal size; //容量大小

    @TableField(value = "remainingSize")
    @ApiModelProperty(notes = "余量,前端不传该参数")
    private BigDecimal remainingSize;

    @TableField(value = "sizeUnit")
    @ApiModelProperty(notes = "容量单位,必传",required = true)
    private String sizeUnit;//容量单位

    @TableField(value = "price")
    @ApiModelProperty(notes = "资源价格")
    private BigDecimal price;//资源价格

    @TableField(value = "createDate",fill = FieldFill.INSERT)
    @ApiModelProperty(notes = "创建时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @TableField(value = "updateDate",fill = FieldFill.UPDATE)
    @ApiModelProperty(notes = "更新时间,前端不传该参数")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    @TableField(value = "expireDate")
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
