package com.sobey.module.packagesCustom.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import com.sobey.module.packagesCustomOption.model.PackagesCustomOption;

import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_product_packages_custom")
public class PackagesCustom extends SuperModel<PackagesCustom> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example = "自定义套餐包资源名称")
	@TableField(value = "name")
	private String name;

	@ApiModelProperty(example = "描述")
	@TableField(value = "desc")
	private String desc;

	@ApiModelProperty(example = "所属套餐包编号")
	@TableField(value = "packagesId")
	private String packagesId;

	@ApiModelProperty(example = "是否启用")
	@TableField(value = "enable")
	private String enable;
	
	@ApiModelProperty(example = "资源的页面顺序")
	@TableField(value = "order")
	private Integer order;
	
	@ApiModelProperty(example = "是否隐藏")
	@TableField(value = "hide")
	private String hide;
	
	@ApiModelProperty(example = "关联按量计费的uuid")
	@TableField(value = "metricId")
	private String metricId;

	@ApiModelProperty(hidden=true)
	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@ApiModelProperty(hidden=true)
	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;

	

	@TableField(exist = false)
	private List<PackagesCustomOption> options;
	
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

	

	/**
	 * @return the packagesId
	 */
	public String getPackagesId() {
		return packagesId;
	}

	/**
	 * @param packagesId the packagesId to set
	 */
	public void setPackagesId(String packagesId) {
		this.packagesId = packagesId;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	

	/**
	 * @return the options
	 */
	public List<PackagesCustomOption> getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(List<PackagesCustomOption> options) {
		this.options = options;
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
	 * @return the metricId
	 */
	public String getMetricId() {
		return metricId;
	}

	/**
	 * @param metricId the metricId to set
	 */
	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
