package com.sobey.module.token.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.framework.config.Token;
import com.sobey.framework.jwt.annotation.JwtToken;
import com.sobey.framework.jwt.config.Login;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

@RestController
@RequestMapping("/test")
@ApiIgnore
public class TokenController {

	@Autowired
	private Login user;
	@Autowired
	private Token time;
	
	@GetMapping("/token")
	public String getToken(@RequestParam @ApiParam("username") String username, @RequestParam @ApiParam("password") String password) {

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new AppException(ExceptionType.SYS_PARAMETER);
		}
		
		if (!(user.getUsername().equals(username) && user.getPassword().equals(password))) {
			AppException a = new AppException(ExceptionType.SYS_PASSWORD);
			throw a;
		}
		//以后是一小时过期3600000
		int tm = time.getMethod() * 60 * 1000;
		String token = JWT.create().withAudience(username).withExpiresAt(new Date(System.currentTimeMillis()+ tm)).sign(Algorithm.HMAC256(password));
		return token;
	}
	
	@JwtToken
    @GetMapping("/getMessage")
    public String getMessage(@RequestParam @ApiParam("SK") String sk){
        return "你已通过验证";
    }
}