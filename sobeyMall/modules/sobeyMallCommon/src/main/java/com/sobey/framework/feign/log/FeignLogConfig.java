package com.sobey.framework.feign.log;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

@Configuration
public class FeignLogConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        //打印请求的最详细的信息
        return Logger.Level.FULL;
    }
    
    @Bean
    Logger infoFeignLoggerFactory() {
    	org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
        return new FeignCustomLogger(logger);
    }
    
    
    
    
}
