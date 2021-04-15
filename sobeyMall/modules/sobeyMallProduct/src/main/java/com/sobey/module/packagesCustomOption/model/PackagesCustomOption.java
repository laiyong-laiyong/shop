package com.sobey.module.packagesCustomOption.model;

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
@TableName("mall_product_packages_custom_option")
public class PackagesCustomOption extends SuperModel<PackagesCustomOption> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example = "套餐包选项名称")
	@TableField(value = "name")
	private String name;

	@ApiModelProperty(example = "描述")
	@TableField(value = "desc")
	private String desc;

	@ApiModelProperty(example = "自定义资源编号")
	@TableField(value = "customId")
	private String customId;

	@ApiModelProperty(example = "价格,内容是数字")
	@TableField(value = "price")
	private BigDecimal price;
	
	@TableField(value = "limitedPrice")
	@ApiModelProperty(example="限价")
	private BigDecimal limitedPrice;


	@ApiModelProperty(example = "页面顺序")
	@TableField(value = "order")
	private Integer order;

	@ApiModelProperty(example = "是否隐藏")
	@TableField(value = "hide")
	private String hide;

	@ApiModelProperty(example = "容量大小,-1表示无限量")
	@TableField(value = "size")
	private BigDecimal size;

	@ApiModelProperty(example = "容量单位")
	@TableField(value = "sizeUnit")
	private String sizeUnit;

	@ApiModelProperty(hidden=true)
	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@ApiModelProperty(hidden=true)
	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;

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
	 * @return the operationType
	 */
	public String getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType
	 *            the operationType to set
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
	 * @param order
	 *            the order to set
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
	 * @param hide
	 *            the hide to set
	 */
	public void setHide(String hide) {
		this.hide = hide;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	

	

	/**
	 * @return the size
	 */
	public BigDecimal getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(BigDecimal size) {
		this.size = size;
	}

	/**
	 * @return the sizeUnit
	 */
	public String getSizeUnit() {
		return sizeUnit;
	}

	/**
	 * @param sizeUnit
	 *            the sizeUnit to set
	 */
	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the limitedPrice
	 */
	public BigDecimal getLimitedPrice() {
		return limitedPrice;
	}

	/**
	 * @param limitedPrice the limitedPrice to set
	 */
	public void setLimitedPrice(BigDecimal limitedPrice) {
		this.limitedPrice = limitedPrice;
	}

}
