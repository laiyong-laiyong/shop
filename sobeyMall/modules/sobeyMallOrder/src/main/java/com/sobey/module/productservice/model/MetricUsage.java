package com.sobey.module.productservice.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 保存用量信息
 * @Author WuChenYang
 * @CreateTime 2020/3/25 15:04
 */
@TableName("metric_usage")
@ApiModel
public class MetricUsage extends SuperModel<MetricUsage> {

    @TableField("orderNo")
    @ApiModelProperty(notes = "订单号",example = "123")
    private String orderNo;

    @TableField("requestId")
    @ApiModelProperty(notes = "请求Id",example = "123")
    private String requestId;

    @TableField("priceId")
    @ApiModelProperty(notes = "单价id",example = "123")
    private String priceId;//单价id

    @TableField("price")
    @ApiModelProperty(notes = "单价",example = "1")
    private BigDecimal price;

    @TableField("value")
    @ApiModelProperty(notes = "使用量",example = "50")
    private Double value;//用量

    @TableField("typeCode")
    @ApiModelProperty(notes = "用量类型编码 1-流量/KB 2-流量/MB 3-流量/GB 4-时长/秒 5-时长/分钟 6-时长/小时 7-次 8-其他/默认单位",example = "4")
    private String typeCode;

    @TableField("type")
    @ApiModelProperty(notes = "用量类型名称",example = "时长")
    private String type;

    @TableField("createTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    @ApiModelProperty(notes = "创建时间",example = "2020-03-25 10:00:00")
    private Date createTime;

    public BigDecimal getPrice() {
        return price;
    }

    public MetricUsage setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public MetricUsage setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public MetricUsage setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public String getPriceId() {
        return priceId;
    }

    public MetricUsage setPriceId(String priceId) {
        this.priceId = priceId;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public MetricUsage setValue(Double value) {
        this.value = value;
        return this;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public MetricUsage setTypeCode(String typeCode) {
        this.typeCode = typeCode;
        return this;
    }

    public String getType() {
        return type;
    }

    public MetricUsage setType(String type) {
        this.type = type;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public MetricUsage setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}
