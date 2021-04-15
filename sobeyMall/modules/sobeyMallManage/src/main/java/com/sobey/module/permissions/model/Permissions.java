package com.sobey.module.permissions.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 */
@TableName("mall_init_data_permissions")
@ApiModel
public class Permissions extends SuperModel<Permissions> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example = "角色code")
	@TableField(value = "roleCode")
	private String roleCode;

	@ApiModelProperty(example = "资源code")
	@TableField(value = "resourceCode")
	private String resourceCode;

	@ApiModelProperty(example = "权限")
	@TableField(value = "permissions")
	private String permissions;
	

	@ApiModelProperty(example = "站点code")
	@TableField(value = "siteCode")
	private String siteCode;
	
	

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
	 * @return the roleCode
	 */
	public String getRoleCode() {
		return roleCode;
	}


	/**
	 * @param roleCode the roleCode to set
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}


	


	/**
	 * @return the resourceCode
	 */
	public String getResourceCode() {
		return resourceCode;
	}


	/**
	 * @param resourceCode the resourceCode to set
	 */
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}


	/**
	 * @return the permissions
	 */
	public String getPermissions() {
		return permissions;
	}


	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(String permissions) {
		this.permissions = permissions;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	
	
	

}
