package com.sobey.module.zfbpay.entity.precreate.request;

import java.math.BigDecimal;

/**
 * C统一收单线下交易预创建,
 * C收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付
 * 
 * C这里是请求参数
 * 
 * @author lgc
 * @date 2020年3月23日 下午5:32:12
 */
public class PrecreateRequest {
	private String out_trade_no;
	private BigDecimal total_amount;
	private String subject;
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
