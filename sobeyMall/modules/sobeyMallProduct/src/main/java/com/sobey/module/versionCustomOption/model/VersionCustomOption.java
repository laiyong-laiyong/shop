package com.sobey.module.versionCustomOption.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_product_version_custom_option")
public class VersionCustomOption extends SuperModel<VersionCustomOption> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@TableField(value = "name")
	private String name;

	@TableField(value = "desc")
	private String desc;

	@TableField(value = "customId")
	@ApiModelProperty(example="自定义规格编号")
	private String customId;


	@TableField(value = "priceMonth")
	@ApiModelProperty(example="按月价格")
	private BigDecimal priceMonth;
	
	@TableField(value = "priceYear")
	@ApiModelProperty(example="按年价格")
	private BigDecimal priceYear;
	
	
	@TableField(value = "priceDay")
	@ApiModelProperty(example="按天价格")
	private BigDecimal priceDay;
	
	
	@TableField(value = "limitedPriceDay")
	@ApiModelProperty(example="按天限价")
	private BigDecimal limitedPriceDay;
	
	@TableField(value = "limitedPriceMonth")
	@ApiModelProperty(example="按月限价")
	private BigDecimal limitedPriceMonth;
	
	@TableField(value = "limitedPriceYear")
	@ApiModelProperty(example="按年限价")
	private BigDecimal limitedPriceYear;
	
	
	@TableField(value = "order")
	@ApiModelProperty(example="页面排序")
	private Integer order;
	
	@TableField(value = "hide")
	@ApiModelProperty(example="是否隐藏,1:隐藏。2：不隐藏")
	private String hide;

	@ApiModelProperty(hidden=true)
	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@ApiModelProperty(hidden=true)
	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;

	/**
	 * 以下字段在数据库中不存在，供页面查询用
	 * 
	 */
	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date startDate;

	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date endDate;
	
	/**
	 * C类型有：插入，修改，删除
	 * 
	 */
	@TableField(exist = false)
	private String operationType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	/**
	 * @return the priceMonth
	 */
	public BigDecimal getPriceMonth() {
		return priceMonth;
	}

	/**
	 * @param priceMonth the priceMonth to set
	 */
	public void setPriceMonth(BigDecimal priceMonth) {
		this.priceMonth = priceMonth;
	}

	/**
	 * @return the priceYear
	 */
	public BigDecimal getPriceYear() {
		return priceYear;
	}

	/**
	 * @param priceYear the priceYear to set
	 */
	public void setPriceYear(BigDecimal priceYear) {
		this.priceYear = priceYear;
	}

	/**
	 * @return the operationType
	 */
	public String getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType the operationType to set
	 */
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	/**
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * @return the hide
	 */
	public String getHide() {
		return hide;
	}

	/**
	 * @param hide the hide to set
	 */
	public void setHide(String hide) {
		this.hide = hide;
	}

	/**
	 * @return the priceDay
	 */
	public BigDecimal getPriceDay() {
		return priceDay;
	}

	/**
	 * @param priceDay the priceDay to set
	 */
	public void setPriceDay(BigDecimal priceDay) {
		this.priceDay = priceDay;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the limitedPriceDay
	 */
	public BigDecimal getLimitedPriceDay() {
		return limitedPriceDay;
	}

	/**
	 * @param limitedPriceDay the limitedPriceDay to set
	 */
	public void setLimitedPriceDay(BigDecimal limitedPriceDay) {
		this.limitedPriceDay = limitedPriceDay;
	}

	/**
	 * @return the limitedPriceMonth
	 */
	public BigDecimal getLimitedPriceMonth() {
		return limitedPriceMonth;
	}

	/**
	 * @param limitedPriceMonth the limitedPriceMonth to set
	 */
	public void setLimitedPriceMonth(BigDecimal limitedPriceMonth) {
		this.limitedPriceMonth = limitedPriceMonth;
	}

	/**
	 * @return the limitedPriceYear
	 */
	public BigDecimal getLimitedPriceYear() {
		return limitedPriceYear;
	}

	/**
	 * @param limitedPriceYear the limitedPriceYear to set
	 */
	public void setLimitedPriceYear(BigDecimal limitedPriceYear) {
		this.limitedPriceYear = limitedPriceYear;
	}

	

}
