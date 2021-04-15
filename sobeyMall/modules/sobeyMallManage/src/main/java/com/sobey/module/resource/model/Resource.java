package com.sobey.module.resource.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_init_data_resource")
@ApiModel
public class Resource extends SuperModel<Resource> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example = "资源名称")
	@TableField(value = "name")
	private String name;

	@ApiModelProperty(example = "是否显示")
	@TableField(value = "shown")
	private Integer shown;

	@ApiModelProperty(example = "资源code")
	@TableField(value = "code")
	private String code;
	
	@ApiModelProperty(example = "父节点编号")
	@TableField(value = "parentId")
	private String parentId;

	@ApiModelProperty(example = "客户端code")
	@TableField(value = "clientCode")
	private String clientCode;

	@ApiModelProperty(example = "站点code")
	@TableField(value = "siteCode")
	private String siteCode;


	@ApiModelProperty(example = "认证中心资源id")
	@TableField(value = "resourceId")
	private Long resourceId;
	
	
	@ApiModelProperty(example = "描述")
	@TableField(value = "desc")
	private String desc;
	
	
	@ApiModelProperty(example = "是否成功执行(yes,no)")
	@TableField(value = "success")
	private String success;

	
	
	

	/**
	 * @return the success
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the shown
	 */
	public Integer getShown() {
		return shown;
	}

	/**
	 * @param shown the shown to set
	 */
	public void setShown(Integer shown) {
		this.shown = shown;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the clientCode
	 */
	public String getClientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
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

	/**
	 * @return the resourceId
	 */
	public Long getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
	
	

}
