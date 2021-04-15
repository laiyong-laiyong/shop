/**
 * 
 */
package com.sobey.module.zfbpay.entity.precreate.request;

import java.math.BigDecimal;

/**
 * @author lgc
 * @date 2020年3月23日 下午5:41:32
 */
public class SettleDetailInfo {

	private String trans_in_type;
	private String trans_in;
	private BigDecimal amount;

	/**
	 * @return the trans_in_type
	 */
	public String getTrans_in_type() {
		return trans_in_type;
	}

	/**
	 * @param trans_in_type
	 *            the trans_in_type to set
	 */
	public void setTrans_in_type(String trans_in_type) {
		this.trans_in_type = trans_in_type;
	}

	/**
	 * @return the trans_in
	 */
	public String getTrans_in() {
		return trans_in;
	}

	/**
	 * @param trans_in
	 *            the trans_in to set
	 */
	public void setTrans_in(String trans_in) {
		this.trans_in = trans_in;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
