package com.sobey.module.zfbpay.entity.tradePage.response;

import java.math.BigDecimal;

public class TradePagePayCallBack {
	
	private String charset;
	private String out_trade_no;
	private String method;
	private BigDecimal total_amount;
	private String sign;
	private String passback_params;
	
	private String trade_no;
	private String auth_app_id;
	private String version;
	private String app_id;
	private String sign_type;
	private String seller_id;

	public String getPassback_params() {
		return passback_params;
	}

	public void setPassback_params(String passback_params) {
		this.passback_params = passback_params;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
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
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
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
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the trade_no
	 */
	public String getTrade_no() {
		return trade_no;
	}
	/**
	 * @param trade_no the trade_no to set
	 */
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	/**
	 * @return the auth_app_id
	 */
	public String getAuth_app_id() {
		return auth_app_id;
	}
	/**
	 * @param auth_app_id the auth_app_id to set
	 */
	public void setAuth_app_id(String auth_app_id) {
		this.auth_app_id = auth_app_id;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the app_id
	 */
	public String getApp_id() {
		return app_id;
	}
	/**
	 * @param app_id the app_id to set
	 */
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	/**
	 * @return the sign_type
	 */
	public String getSign_type() {
		return sign_type;
	}
	/**
	 * @param sign_type the sign_type to set
	 */
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	/**
	 * @return the seller_id
	 */
	public String getSeller_id() {
		return seller_id;
	}
	/**
	 * @param seller_id the seller_id to set
	 */
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	
	
	
	
}
