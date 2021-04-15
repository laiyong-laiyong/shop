/**
 * 
 */
package com.sobey.module.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.sobey.exception.AppException;
import com.sobey.module.zfbpay.config.Alipay;

/**
 * @author lgc
 * @date 2020年3月23日 下午6:09:56
 */

@Component
public class ZfbUtil {

	@Autowired
	private Alipay ali;

	public AlipayClient getAlipayClient() {


		CertAlipayRequest cert = new CertAlipayRequest();
		cert.setServerUrl(ali.getUrl());
		cert.setAppId(ali.getApp_id());
		cert.setPrivateKey(ali.getPrivate_key());
		cert.setFormat(ali.getFormat());
		cert.setCharset("UTF-8");
		cert.setSignType(ali.getSign_type());
		cert.setCertPath(ali.getAddress() + ali.getApp_cert_path());
		cert.setAlipayPublicCertPath(ali.getAddress() + ali.getAlipay_cert_path());
		cert.setRootCertPath(ali.getAddress() + ali.getAlipay_root_cert_path());
		DefaultAlipayClient ct = null;
		try {
			ct = new DefaultAlipayClient(cert);
		} catch (AlipayApiException e) {
			throw new AppException("初始化阿里支付客户端失败",e);
		}

		return ct;
	}

}
