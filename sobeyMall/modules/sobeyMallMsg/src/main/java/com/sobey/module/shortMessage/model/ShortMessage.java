package com.sobey.module.shortMessage.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.sobey.framework.mybatisPlus.SuperModel;

/**
 */
@TableName("mall_msg_cloud_short_message")
public class ShortMessage extends SuperModel<ShortMessage> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@TableField(value = "cloudProvider")
	private String cloudProvider;

	@TableField(value = "signName")
	private String signName;

	@TableField(value = "templateCode")
	private String templateCode;

	@TableField(value = "notifyType")
	private String notifyType;

	

	

	/**
	 * @return the cloudProvider
	 */
	public String getCloudProvider() {
		return cloudProvider;
	}

	/**
	 * @param cloudProvider the cloudProvider to set
	 */
	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}

	/**
	 * @return the signName
	 */
	public String getSignName() {
		return signName;
	}

	/**
	 * @param signName
	 *            the signName to set
	 */
	public void setSignName(String signName) {
		this.signName = signName;
	}

	/**
	 * @return the templateCode
	 */
	public String getTemplateCode() {
		return templateCode;
	}

	/**
	 * @param templateCode
	 *            the templateCode to set
	 */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	

	

	/**
	 * @return the notifyType
	 */
	public String getNotifyType() {
		return notifyType;
	}

	/**
	 * @param notifyType the notifyType to set
	 */
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
