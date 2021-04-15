package com.sobey.module.fegin.product.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 */
public class VersionCustomOption {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String name;

	private String desc;

	private String customId;


	private BigDecimal priceMonth;
	
	private BigDecimal priceYear;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date createDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date updateDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date endDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	/**
	 * @return the priceMonth
	 */
	public BigDecimal getPriceMonth() {
		return priceMonth;
	}

	/**
	 * @param priceMonth the priceMonth to set
	 */
	public void setPriceMonth(BigDecimal priceMonth) {
		this.priceMonth = priceMonth;
	}

	/**
	 * @return the priceYear
	 */
	public BigDecimal getPriceYear() {
		return priceYear;
	}

	/**
	 * @param priceYear the priceYear to set
	 */
	public void setPriceYear(BigDecimal priceYear) {
		this.priceYear = priceYear;
	}

	

}
