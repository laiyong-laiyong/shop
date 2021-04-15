package com.sobey.framework.jwt.Interceptor;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.framework.jwt.annotation.JwtToken;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.framework.jwt.model.Token;
import com.sobey.util.common.date.DateKit;
import com.sobey.util.common.json.JsonKit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Configuration
public class AuthenticationInterceptor implements HandlerInterceptor {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private  SignatureVerifier DEFAULT_VERIFIER = new MacSigner("sobeyhive");

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object)
            throws Exception {
    		

        // C如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // C检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }


        String token = null;
        // C检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(JwtToken.class)) {
            JwtToken userLoginToken = method.getAnnotation(JwtToken.class);
            if (userLoginToken.required()) {
                token = httpServletRequest.getHeader("Authorization");
                // 执行认证
                if (token == null) {
                	throw new AppException(ExceptionType.SYS_TOKEN_NOT_EXIST);
                }
            }
        }

        //C默认需要穿token
        token = httpServletRequest.getHeader("Authorization");
        this.validate(token);

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                Exception e) throws Exception {
    }

    /**
     * C校验token是否过期
     *
     * @param token
     */
    public void validate(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new AppException(ExceptionType.SYS_TOKEN_NOT_EXIST);
        }
        
        Token bean = null;
        try {
			
        	Jwt jwt = JwtHelper.decodeAndVerify(token,DEFAULT_VERIFIER);
        	bean = JsonKit.jsonToBean(jwt.getClaims(), Token.class);
		} catch (InvalidSignatureException e) {
			throw new AppException(ExceptionType.SYS_TOKEN_NOT_SIGN_IN_AUTH_CENTER);
		}catch (Exception e) {
			log.info("token = "+ token);
			throw new AppException(ExceptionType.SYS_TOKEN_NOT_BEEN_PARSE);
		}
        Long exp = null;
        if (bean != null) {
            exp = bean.getExp();
        }
        /**
         * 这里不再做判断，暂时先注释掉
         * 如果token还有5分钟就过期
         */
//        Long minuts = DateKit.getDiffMinute(exp * 1000);
//        if (minuts <= 5) {
//            throw new AppException(ExceptionType.SYS_TOKEN);
//        }
    }
}