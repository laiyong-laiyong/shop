package com.sobey.module.discount.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 折扣表
 * 
 */
@TableName("mall_product_discount_request_info")
public class DiscountRequestInfo extends SuperModel<DiscountRequestInfo> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	
	@ApiModelProperty(example="协议内容")
	@TableField(value = "protocal")
	private String protocal;
	
	
	@ApiModelProperty(example="协议编码")
	@TableField(value = "protocalCode")
	private String protocalCode;
	
	
	@ApiModelProperty(example="协议状态")
	@TableField(value = "status")
	private String status;
	
	@ApiModelProperty(example="操作类型")
	@TableField(value = "operationType")
	private String operationType;

	

	@ApiModelProperty(example="创建时间,不用填",hidden=true)
	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@ApiModelProperty(example="修改时间,不用填",hidden=true)
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the protocal
	 */
	public String getProtocal() {
		return protocal;
	}

	/**
	 * @param protocal the protocal to set
	 */
	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}

	/**
	 * @return the protocalCode
	 */
	public String getProtocalCode() {
		return protocalCode;
	}

	/**
	 * @param protocalCode the protocalCode to set
	 */
	public void setProtocalCode(String protocalCode) {
		this.protocalCode = protocalCode;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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

	
	
	
	

}
