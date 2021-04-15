package com.sobey.framework.feign.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 如果在feign方法上写了@RequestHeader,会自动传递。
 * 这里暂时不启用,以后做公共配置
 * 
 * 
 * @Description fegin统一token拦截器
 * @Author WuChenYang
 * @Since 2020/1/22 18:13
 */
@Component
public class FeginTokenInterceptor implements RequestInterceptor {
	
    @Override
    public void apply(RequestTemplate template) {
        if (null == template){
            return;
        }

        HttpServletRequest request = getRequest();
        //如果是定时任务发起的请求，没有request。所以要判断。
        if (request != null) {
        	//灰度发布(启用:true,不启用:false)
        	String gray = request.getHeader("X-Sc-Canary");
        	if (StrUtil.isNotBlank(gray) && "true".equalsIgnoreCase(gray)) {
        		template.header("X-Sc-Canary",gray);
        	}
		}

    }
    private HttpServletRequest getRequest(){
    	
    	 //如果是定时任务发起的请求，没有request。所以要判断。
    	ServletRequestAttributes att =(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
    	if (att == null) {
			return null;
		}else {
			return att.getRequest();
		}
    }
}
