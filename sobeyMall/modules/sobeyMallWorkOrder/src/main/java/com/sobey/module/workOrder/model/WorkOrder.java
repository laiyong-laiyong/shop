package com.sobey.module.workOrder.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sobey.framework.mybatisPlus.SuperModel;
import com.sobey.module.workOrdercategory.model.Category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_work_order")
@ApiModel
public class WorkOrder extends SuperModel<WorkOrder> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@TableField(value = "name")
	private String name;

	@TableField(value = "categoryId")
	@ApiModelProperty(example="类别编号")
	private String categoryId;

	@TableField(value = "desc")
	@ApiModelProperty(example="描述")
	private String desc;

	@TableField(value = "state")
	@ApiModelProperty(example="工单状态")
	private String state;

	@TableField(value = "telephone")
	private String telephone;

	@TableField(value = "email")
	private String email;

	@NotBlank(message="mediaDir不能为空")
	@TableField(value = "mediaDir")
	@ApiModelProperty(example="素材主目录")
	private String mediaDir;

	@TableField(value = "level")
	@ApiModelProperty(example="工单级别")
	private String level;
	
	@NotBlank(message="handlerCode不能为空")
	@ApiModelProperty(example="处理者编码")
	@TableField(value = "handlerCode")
	private String handlerCode;
	
	
	@ApiModelProperty(example="商品编号")
	@TableField(value = "productId")
	private String productId;
	
	
	
	@ApiModelProperty(example="评价内容")
	@TableField(value = "evaluate")
	private String evaluate;

	/**
	 * 这里只是为了和dialogue表统一,其值等于uuid
	 * 
	 */
	@NotBlank(message="mediaId不能为空")
	@TableField(value = "mediaId")
	@ApiModelProperty(example="素材关联编号")
	private String mediaId;
	
	@NotBlank(message="createUserCode不能为空")
	@TableField(value = "createUserCode")
	@ApiModelProperty(example="创建者编码")
	private String createUserCode;
	
	@TableField(value = "deleteFlag")
	@TableLogic
	@ApiModelProperty(example="值是数字。逻辑删除(0是删除，1是未删除)")
	private Integer deleteFlag;

	@TableField(value = "createDate", fill = FieldFill.INSERT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@TableField(value = "updateDate", fill = FieldFill.UPDATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;
	
	@ApiModelProperty(example="联系开始时间")
	@TableField(value = "contactStartTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date contactStartTime;
	
	@ApiModelProperty(example="联系结束时间")
	@TableField(value = "contactEndTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date contactEndTime;
	
	
	@TableField(value = "closeDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date closeDate;
	

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
	private Category category;
	
	/**
	 * 处理时长
	 * 
	 */
	@TableField(exist = false)
	@ApiModelProperty(example="处理时长")
	private String handleDate;
	
	
	@TableField(exist = false)
	@ApiModelProperty(example="创建者名称")
	private String createUserName;
	
	@TableField(exist = false)
	@ApiModelProperty(example="处理者名称")
	private String handlerName;
	
	@ApiModelProperty(example="商品名称")
	@TableField(exist = false)
	private String productName;
	
	
	@NotBlank(message="siteCode不能为空")
	@TableField(value = "siteCode")
	@ApiModelProperty(example="创建者的站点code")
	private String siteCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public String getMediaDir() {
		return mediaDir;
	}

	public void setMediaDir(String mediaDir) {
		this.mediaDir = mediaDir;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
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
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the contactStartTime
	 */
	public Date getContactStartTime() {
		return contactStartTime;
	}

	/**
	 * @param contactStartTime the contactStartTime to set
	 */
	public void setContactStartTime(Date contactStartTime) {
		this.contactStartTime = contactStartTime;
	}

	/**
	 * @return the contactEndTime
	 */
	public Date getContactEndTime() {
		return contactEndTime;
	}

	/**
	 * @param contactEndTime the contactEndTime to set
	 */
	public void setContactEndTime(Date contactEndTime) {
		this.contactEndTime = contactEndTime;
	}


	/**
	 * @return the handlerCode
	 */
	public String getHandlerCode() {
		return handlerCode;
	}

	/**
	 * @param handlerCode the handlerCode to set
	 */
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	/**
	 * @return the handlerName
	 */
	public String getHandlerName() {
		return handlerName;
	}

	/**
	 * @param handlerName the handlerName to set
	 */
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	/**
	 * @return the evaluate
	 */
	public String getEvaluate() {
		return evaluate;
	}

	/**
	 * @param evaluate the evaluate to set
	 */
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	/**
	 * @return the closeDate
	 */
	public Date getCloseDate() {
		return closeDate;
	}

	/**
	 * @param closeDate the closeDate to set
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the handleDate
	 */
	public String getHandleDate() {
		return handleDate;
	}

	/**
	 * @param handleDate the handleDate to set
	 */
	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}

	/**
	 * @return the createUserCode
	 */
	public String getCreateUserCode() {
		return createUserCode;
	}

	/**
	 * @param createUserCode the createUserCode to set
	 */
	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

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

	/**
	 * @return the deleteFlag
	 */
	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * @return the siteCode
	 */
	public String getSiteCode() {
		return siteCode;
	}

	/**
	 * @param siteCode the siteCode to set
	 */
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	
	
	
	
	

}
