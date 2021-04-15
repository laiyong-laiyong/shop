package com.sobey.module.fegin.product.request.service;

import com.alibaba.fastjson.JSONArray;
import com.sobey.module.fegin.product.response.entity.Metric;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 请求商品模块
 * @Author WuChenYang
 * @CreateTime 2020/3/19 16:11
 */
@FeignClient(name = "${address.product.name}",url = "${address.product.url}")
public interface ProductService {


    /**
     * 查询单价
     * @param auth
     * @param uuid
     * @return
     */
    @GetMapping("/${api.v1}/metric/list")
    List<Metric> queryPrice(@RequestHeader(value = "Authorization") String auth,
                            @RequestParam(required = false) String uuid,
                            @RequestParam(required = false) String productId);

    @GetMapping("/${api.v1}/products/{uuid}")
    JSONArray queryProduct(@RequestHeader(value = "Authorization") String auth, @PathVariable(value = "uuid") String uuid);

    @GetMapping("/${api.v1}/products/list")
    JSONArray queryProductByCode(@RequestHeader(value = "Authorization") String auth, @RequestParam String code);

}
