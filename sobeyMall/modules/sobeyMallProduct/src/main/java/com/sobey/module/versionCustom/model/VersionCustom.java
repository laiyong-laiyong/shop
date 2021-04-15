package com.sobey.module.versionCustom.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import com.sobey.module.versionCustomOption.model.VersionCustomOption;

import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_product_version_custom")
public class VersionCustom extends SuperModel<VersionCustom> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@TableField(value = "name")
	private String name;

	@TableField(value = "desc")
	private String desc;

	@ApiModelProperty(example="版本的编号")
	@TableField(value = "versionId")
	private String versionId;

	@ApiModelProperty(example="是否启用")
	@TableField(value = "enable")
	private String enable;
	
	@ApiModelProperty(example="页面排序")
	@TableField(value = "order")
	private Integer order;
	
	@ApiModelProperty(example="是否隐藏,1:隐藏。2：不隐藏")
	@TableField(value = "hide")
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

	@TableField(exist = false)
	private List<VersionCustomOption> options;
	
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

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public List<VersionCustomOption> getOptions() {
		return options;
	}

	public void setOptions(List<VersionCustomOption> options) {
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

}
