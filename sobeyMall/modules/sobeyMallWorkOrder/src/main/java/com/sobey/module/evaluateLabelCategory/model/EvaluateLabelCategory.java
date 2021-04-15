package com.sobey.module.evaluateLabelCategory.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_work_order_evaluate_label_category")
public class EvaluateLabelCategory extends SuperModel<EvaluateLabelCategory> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example="名字")
	@TableField(value = "name")
	private String name;
	
	@ApiModelProperty(example="星级编号")
	@TableField(value = "starId")
	private String starId;

	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;

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

	/**
	 * @return the starId
	 */
	public String getStarId() {
		return starId;
	}

	/**
	 * @param starId the starId to set
	 */
	public void setStarId(String starId) {
		this.starId = starId;
	}

	

}
