package com.sobey.module.token.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sobey.framework.jwt.annotation.PassToken;

import cn.hutool.core.text.StrBuilder;

@RestController
@RequestMapping("/config")
public class ConfigController {

	@Value("${address.manage.url}")
	private String manager;
	@Value("${address.product.url}")
	private String product;
	@Value("${address.order.url}")
	private String order;
	@Value("${address.pay.url}")
	private String pay;
	@Value("${address.msg.url}")
	private String msg;
	@Value("${address.auth.url}")
	private String auth;
	
	
	
	@PassToken
	@GetMapping("/address")
	public String address(){
		StrBuilder db = StrBuilder.create();
		db.append("manage地址="+manager).append("\n");
		db.append("product地址="+product).append("\n");
		db.append("order地址="+order).append("\n");
		db.append("msg地址="+msg).append("\n");
		db.append("pay地址="+pay).append("\n");
		db.append("auth地址="+auth).append("\n");
		return db.toString();
	}
}