package com.sobey.module.evaluate.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_work_order_evaluate")
public class Evaluate extends SuperModel<Evaluate> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;


	@TableField(value = "workOrderId")
	@ApiModelProperty(example="工单编号")
	private String workOrderId;

	@TableField(value = "evaluateCategoryId")
	@ApiModelProperty(example="评价类别编号，不区分星级评价和标签评价")
	private String evaluateCategoryId;


	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;
	



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
	 * @return the workOrderId
	 */
	public String getWorkOrderId() {
		return workOrderId;
	}

	/**
	 * @param workOrderId the workOrderId to set
	 */
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}

	/**
	 * @return the evaluateCategoryId
	 */
	public String getEvaluateCategoryId() {
		return evaluateCategoryId;
	}

	/**
	 * @param evaluateCategoryId the evaluateCategoryId to set
	 */
	public void setEvaluateCategoryId(String evaluateCategoryId) {
		this.evaluateCategoryId = evaluateCategoryId;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
