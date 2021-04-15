package com.sobey.module.discount.model.request;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 */
public class ProductDiscount{

	@NotEmpty(message="productIds不能为空")
	private List<String> productIds;
	
	@NotNull(message="productDiscounts的discount必须传递")
	@Digits(integer=10,fraction=4,message="折扣小数位太多")
	@ApiModelProperty(example="折扣,值是小数")
	private BigDecimal discount;

	
	

	/**
	 * @return the productIds
	 */
	public List<String> getProductIds() {
		return productIds;
	}

	/**
	 * @param productIds the productIds to set
	 */
	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}

	/**
	 * @return the discount
	 */
	public BigDecimal getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	

	
	

}
