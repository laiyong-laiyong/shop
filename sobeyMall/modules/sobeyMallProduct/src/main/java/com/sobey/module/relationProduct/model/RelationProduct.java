package com.sobey.module.relationProduct.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_product_relation")
public class RelationProduct extends SuperModel<RelationProduct> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;


	
	@ApiModelProperty(example="商品编号,这个编号相当于工具编号")
	@TableField(value = "productId")
	private String productId;
	
	
	@ApiModelProperty(example="关联商品编号,这个是主商品编号")
	@TableField(value = "relationProductId")
	private String relationProductId;
	
	
	
	
	

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
	@ApiModelProperty(hidden=true)
	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date startDate;

	@ApiModelProperty(hidden=true)
	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date endDate;
	
	@ApiModelProperty(example="关联商品名称")
	@TableField(exist = false)
	private String relationProductName;
	
	@ApiModelProperty(example="操作类型")
	@TableField(exist = false)
	private String operationType;
	
	@ApiModelProperty(example="商品名称，也就是工具名称")
	@TableField(exist = false)
	private String productName;
	

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the relationProductId
	 */
	public String getRelationProductId() {
		return relationProductId;
	}

	/**
	 * @param relationProductId the relationProductId to set
	 */
	public void setRelationProductId(String relationProductId) {
		this.relationProductId = relationProductId;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the relationProductName
	 */
	public String getRelationProductName() {
		return relationProductName;
	}

	/**
	 * @param relationProductName the relationProductName to set
	 */
	public void setRelationProductName(String relationProductName) {
		this.relationProductName = relationProductName;
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
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	
	
	

}
