package com.sobey.module.packages.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import com.sobey.module.packagesCustom.model.PackagesCustom;

import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_product_packages")
public class Packages extends SuperModel<Packages> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@TableField(value = "name")
	@ApiModelProperty(example = "套餐包名称")
	private String name;

	@TableField(value = "code")
	@ApiModelProperty(example = "套餐包编码")
	private String code;

	@ApiModelProperty(example = "有效时长")
	@TableField(value = "effectiveDuration")
	private Integer effectiveDuration;

	@ApiModelProperty(example = "时长单位")
	@TableField(value = "unit")
	private String unit;

	@ApiModelProperty(example = "套餐包描述")
	@TableField(value = "desc")
	private String desc;

	@ApiModelProperty(example = "所属商品编号")
	@TableField(value = "productId")
	private String productId;

	@ApiModelProperty(example = "是否隐藏")
	@TableField(value = "hide")
	private String hide;

	@ApiModelProperty(example = "套餐包页面顺序")
	@TableField(value = "order")
	private Integer order;

	@ApiModelProperty(hidden=true)
	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@ApiModelProperty(hidden=true)
	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;

	@TableField(exist = false)
	@ApiModelProperty(example = "操作类型")
	private String operationType;

	@TableField(exist = false)
	private List<PackagesCustom> customs;

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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	

	/**
	 * @return the customs
	 */
	public List<PackagesCustom> getCustoms() {
		return customs;
	}

	/**
	 * @param customs the customs to set
	 */
	public void setCustoms(List<PackagesCustom> customs) {
		this.customs = customs;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	 * @return the effectiveDuration
	 */
	public Integer getEffectiveDuration() {
		return effectiveDuration;
	}

	/**
	 * @param effectiveDuration the effectiveDuration to set
	 */
	public void setEffectiveDuration(Integer effectiveDuration) {
		this.effectiveDuration = effectiveDuration;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
