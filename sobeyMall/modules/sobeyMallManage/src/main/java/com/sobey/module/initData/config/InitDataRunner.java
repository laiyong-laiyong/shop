package com.sobey.module.initData.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sobey.module.initData.service.InitDataService;

/**
 * 这里是为了商城的初始化数据
 * 
 * @author lgc
 * @date 2021年1月20日 下午4:48:52
 */
@Component
public class InitDataRunner implements ApplicationRunner {
	
	
	@Autowired
	private InitDataService ids;
	
    @Override
    public void run(ApplicationArguments args) throws Exception {
    	ids.run();
    }
    
    
    
    
    
}