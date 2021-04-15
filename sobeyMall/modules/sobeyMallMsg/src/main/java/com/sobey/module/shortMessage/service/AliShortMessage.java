package com.sobey.module.shortMessage.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionKit;
import com.sobey.module.cloudProvider.enumeration.CloudProviderType;
import com.sobey.module.cloudProvider.util.ClientUtil;
import com.sobey.module.shortMessage.enumeration.notifyType;
import com.sobey.module.shortMessage.model.ShortMessage;
import com.sobey.module.shortMessage.model.response.AliMessageResponse;
import com.sobey.util.common.json.JsonKit;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

@Service
public class AliShortMessage {

	@Autowired
	private ClientUtil clientUtil;
	@Autowired
	private ShortMessageService sms;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 * 
	 * @param phoneNumber
	 * @param placeholder 
	 *            占位符的json串：{"name":"lgc"}
	 * @param signName
	 * @param templateCode
	 */
	public void send(String phoneNumber, String placeholder, String signName, String templateCode) {
		/**
		 * 
		 * 
		 */
		if (StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(placeholder)
				|| StringUtils.isEmpty(signName) || StringUtils.isEmpty(templateCode)) {
			throw new AppException("phoneNumber,placeholder,signName,templateCode不能为空");
		}

		DefaultAcsClient client = clientUtil.getAliAcsClient();
		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("SignName", signName);
		request.putQueryParameter("PhoneNumbers", phoneNumber);
		request.putQueryParameter("TemplateCode", templateCode);
		request.putQueryParameter("TemplateParam", placeholder);
		StringBuffer reqStr = new StringBuffer();
		log.info(reqStr.append("发送短信请求数据：").append("SignName=").append(signName).append(",PhoneNumbers=").append(phoneNumber)
				.append(",TemplateCode=").append(templateCode).append(",TemplateParam=").append(placeholder).toString());
		try {
			CommonResponse response = client.getCommonResponse(request);
			String data = response.getData();
			log.info("发送短信收到的反馈：" + data);
			AliMessageResponse bean = JsonKit.jsonToBean(data, AliMessageResponse.class);
			if (!(bean.getMessage().equalsIgnoreCase("OK"))) {
				throw new AppException("发送短信失败"+bean.getMessage());
			}
		} catch (ServerException e) {
			throw new AppException("发送短信失败",e);
		} catch (com.aliyuncs.exceptions.ClientException e) {
			throw new AppException("发送短信失败",e);
		}
	}

	/**
	 * 
	 * 
	 * @param phoneNumber
	 * @param placeholder  占位符的json串：{"name":"lgc"}
	 * @param receiver 短信接收者
	 */
	public void send(String phoneNumber, String placeholder,String receiver) {

		ShortMessage sm = new ShortMessage();
		if (notifyType.reminder_operator.getName().equalsIgnoreCase(receiver)) {
			sm.setNotifyType(notifyType.reminder_operator.getName());
		}else if (notifyType.reminder_customer.getName().equalsIgnoreCase(receiver)) {
			sm.setNotifyType(notifyType.reminder_customer.getName());
		}else if (notifyType.distributed_customer.getName().equalsIgnoreCase(receiver)) {
			sm.setNotifyType(notifyType.distributed_customer.getName());
		}else if (notifyType.distributed_operator.getName().equalsIgnoreCase(receiver)) {
			sm.setNotifyType(notifyType.distributed_operator.getName());
		}else if (notifyType.new_work_order.getName().equalsIgnoreCase(receiver)) {
			sm.setNotifyType(notifyType.new_work_order.getName());
		}else if (notifyType.voucher_short_message.getName().equalsIgnoreCase(receiver)) {
			sm.setNotifyType(notifyType.voucher_short_message.getName());
		}else if (notifyType.arrears_short_message.getName().equalsIgnoreCase(receiver)) {
			sm.setNotifyType(notifyType.arrears_short_message.getName());
		}else {
			log.info("接收者不匹配:"+receiver);
		}
		sm.setCloudProvider(CloudProviderType.ali.getName());
		Wrapper<ShortMessage> wp = new EntityWrapper<ShortMessage>(sm);
		List<ShortMessage> list = this.sms.selectList(wp);
		if (CollectionUtils.isNotEmpty(list)) {
			for (ShortMessage item : list) {
				this.send(phoneNumber, placeholder, item.getSignName(), item.getTemplateCode());
			}
		}

	}
	
	

}
