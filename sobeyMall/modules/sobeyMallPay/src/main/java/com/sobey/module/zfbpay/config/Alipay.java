package com.sobey.module.zfbpay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author lgc
 * @date 2020年3月23日 下午6:23:41
 */
@Component
@ConfigurationProperties(prefix = "zfb-pay")
public class Alipay {

	private String app_id;

	private String notify_url;

	private String sign_type;

	private String url;

	private String private_key;

	private String public_key;

	private String format;
	/**
	 * 应用公钥
	 */
	private String app_cert_path;
	/**
	 * 
	 * 支付宝公钥证书
	 */
	private String alipay_cert_path;
	/**
	 * 
	 * 支付宝根证书
	 */
	private String alipay_root_cert_path;
	
	private String address;
	
	private String return_url;
	
	

	/**
	 * @return the app_id
	 */
	public String getApp_id() {
		return app_id;
	}

	/**
	 * @param app_id
	 *            the app_id to set
	 */
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	/**
	 * @return the notify_url
	 */
	public String getNotify_url() {
		return notify_url;
	}

	/**
	 * @param notify_url
	 *            the notify_url to set
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	/**
	 * @return the sign_type
	 */
	public String getSign_type() {
		return sign_type;
	}

	/**
	 * @param sign_type
	 *            the sign_type to set
	 */
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the private_key
	 */
	public String getPrivate_key() {
		return private_key;
	}

	/**
	 * @param private_key
	 *            the private_key to set
	 */
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	/**
	 * @return the public_key
	 */
	public String getPublic_key() {
		return public_key;
	}

	/**
	 * @param public_key
	 *            the public_key to set
	 */
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the app_cert_path
	 */
	public String getApp_cert_path() {
		return app_cert_path;
	}

	/**
	 * @param app_cert_path the app_cert_path to set
	 */
	public void setApp_cert_path(String app_cert_path) {
		this.app_cert_path = app_cert_path;
	}

	/**
	 * @return the alipay_cert_path
	 */
	public String getAlipay_cert_path() {
		return alipay_cert_path;
	}

	/**
	 * @param alipay_cert_path the alipay_cert_path to set
	 */
	public void setAlipay_cert_path(String alipay_cert_path) {
		this.alipay_cert_path = alipay_cert_path;
	}

	/**
	 * @return the alipay_root_cert_path
	 */
	public String getAlipay_root_cert_path() {
		return alipay_root_cert_path;
	}

	/**
	 * @param alipay_root_cert_path the alipay_root_cert_path to set
	 */
	public void setAlipay_root_cert_path(String alipay_root_cert_path) {
		this.alipay_root_cert_path = alipay_root_cert_path;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the return_url
	 */
	public String getReturn_url() {
		return return_url;
	}

	/**
	 * @param return_url the return_url to set
	 */
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

}
