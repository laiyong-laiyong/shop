package com.sobey.module.zfbpay.entity.tradePage;

import java.math.BigDecimal;

public class TradePagePay {
	
	private String out_trade_no;
	private String product_code;
	private BigDecimal total_amount;
	private String subject;
	private String passback_params;//附加数据,支付宝回调时原样返回

	public String getPassback_params() {
		return passback_params;
	}

	public void setPassback_params(String passback_params) {
		this.passback_params = passback_params;
	}

	/**
	 * @return the out_trade_no
	 */
	public String getOut_trade_no() {
		return out_trade_no;
	}
	/**
	 * @param out_trade_no the out_trade_no to set
	 */
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	/**
	 * @return the product_code
	 */
	public String getProduct_code() {
		return product_code;
	}
	/**
	 * @param product_code the product_code to set
	 */
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	/**
	 * @return the total_amount
	 */
	public BigDecimal getTotal_amount() {
		return total_amount;
	}
	/**
	 * @param total_amount the total_amount to set
	 */
	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	
	
	
}
