package com.sobey.module.dialogue.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_work_order_dialogue")
public class Dialogue extends SuperModel<Dialogue> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@TableField(value = "from")
	@ApiModelProperty(example="讲话人的编码")
	private String from;
	
	@TableField(value = "to")
	@ApiModelProperty(example="听话人的编码")
	private String to;

	@TableField(value = "workOrderId")
	@ApiModelProperty(example="工单编号")
	private String workOrderId;

	@TableField(value = "context")
	@ApiModelProperty(example="谈话内容,仅文字")
	private String context;

	

	/**
	 * 这里只是为了和dialogue表统一,其值等于uuid
	 * 
	 */
	@TableField(value = "mediaId")
	@ApiModelProperty(example="素材关联编号")
	private String mediaId;

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
	 * @return the mediaId
	 */
	public String getMediaId() {
		return mediaId;
	}

	/**
	 * @param mediaId
	 *            the mediaId to set
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}


}
