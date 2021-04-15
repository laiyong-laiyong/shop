package com.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * C此项目目前只做quartz数据的初始化，其他功能暂时不用
 * 
 * @author lgc
 * @date 2021年2月23日 下午5:22:28
 */
//@EnableSwagger2
@SpringBootApplication(scanBasePackages = "com.sobey")
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}

}
