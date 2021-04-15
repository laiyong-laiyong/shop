package com.sobey.module.protocal.model;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 开通协议
 * 
 */
@TableName("mall_product_protocal")
public class Protocal extends SuperModel<Protocal> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@NotBlank(message="协议内容不能为空")
	@ApiModelProperty(example="协议内容")
	@TableField(value = "text")
	private String text;
	
	@NotBlank(message="协议类型不能为空")
	@ApiModelProperty(example="协议类型")
	@TableField(value = "type")
	private String type;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	
	
	

}
