package com.sobey.framework.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Swagger2 {
	@Bean
	public Docket createRestApi() {

    	ParameterBuilder tokenPar = new ParameterBuilder();
    	tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
    	ParameterBuilder gray = new ParameterBuilder();
    	gray.name("X-Sc-Canary").description("灰度发布(启用:true,不启用:false)").modelRef(new ModelRef("boolean")).parameterType("header").required(false).build();
    	
    	List<Parameter> pars = new ArrayList<Parameter>();
    	pars.add(tokenPar.build());
    	pars.add(gray.build());

	return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(apiInfo())
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.sobey"))
			.paths(PathSelectors.any())
			.build()
			.globalOperationParameters(pars);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("api文档")
				.description("http://www.sobey.com")
				.termsOfServiceUrl("http://www.sobey.com")
				.version("1.0")
				.build();
	}

}
