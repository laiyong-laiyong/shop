package com.sobey.module.product.service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sobey.module.product.model.response.Product;



@FeignClient(name = "${address.product.name}", url = "${address.product.url}/${api.v1}")
public interface ProductFeignService {

	@GetMapping(value = "/products/{uuid}")
	List<Product> list(@PathVariable(value = "uuid") String uuid);


}
