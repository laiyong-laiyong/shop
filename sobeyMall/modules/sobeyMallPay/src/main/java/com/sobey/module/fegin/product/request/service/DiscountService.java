package com.sobey.module.fegin.product.request.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author WCY
 * @createTime 2020/6/28 10:40
 * 查询折扣信息
 */
@FeignClient(name = "${address.product.name}",url = "${address.product.url}")
public interface DiscountService {

    @GetMapping("/${api.v1}/discount")
    BigDecimal queryDiscount(@RequestHeader(value = "Authorization") String token,
                             @RequestParam String usercode,
                             @RequestParam String productId);

}
