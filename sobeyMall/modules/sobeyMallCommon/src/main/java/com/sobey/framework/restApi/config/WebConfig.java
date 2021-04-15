package com.sobey.framework.restApi.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.sobey.framework.restApi.CustomRequestMappingHandlerMapping;

/**
 * 此种方式预留，暂不启用
 * 
 * @author lgc
 * @date 2020年1月14日 下午6:53:28
 */
//@Configuration
public class WebConfig implements WebMvcRegistrations{
 
	@Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new CustomRequestMappingHandlerMapping();
    }
	
    
}