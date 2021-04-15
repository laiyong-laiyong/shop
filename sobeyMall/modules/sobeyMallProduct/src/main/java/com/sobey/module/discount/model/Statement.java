package com.sobey.module.discount.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.sobey.framework.mybatisPlus.SuperModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 折扣的合同声明
 * 
 */
@TableName("mall_product_discount_statement")
public class Statement extends SuperModel<Statement> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example="声明")
	@TableField(value = "text")
	private String text;

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

	
	
	

}
