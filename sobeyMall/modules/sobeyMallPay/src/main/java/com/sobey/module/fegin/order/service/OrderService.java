package com.sobey.module.fegin.order.service;

import com.sobey.module.fegin.order.entity.response.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by PengJK on 2018/8/29.
 */
@FeignClient(name = "${address.order.name}",url = "${address.order.url}")
public interface OrderService{

	/**
	 * 更新订单
	 * @param id
	 * @param param
	 * @return
	 */
	@PutMapping(value = "/${api.v1}/order/update/{id}")
	Order update(@RequestHeader(value = "Authorization") String auth,@PathVariable(value = "id") String id, @RequestBody Map<String,Object> param);

	@GetMapping("/${api.v1}/order/findByOrderNo/{orderNo}")
	Order findByOrderNo(@RequestHeader(value = "Authorization") String auth,@PathVariable("orderNo") String orderNo);

}
