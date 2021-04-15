package com.sobey.module.fegin.mallPack.packOrder.service;

import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackOrder;
import com.sobey.module.fegin.mallPack.packOrder.entity.response.ResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/5/20 16:41
 */
@FeignClient(name = "${address.order.name}",url = "${address.order.url}")
public interface PackOrderService {

    /**
     * 更新订单
     * @param id
     * @param param
     * @return
     */
    @PutMapping(value = "/${api.v1}/mallPack/order")
    ResultInfo update(@RequestHeader(value = "Authorization") String auth,
                      @RequestParam String id,
                      @RequestBody Map<String, Object> param);

    @GetMapping("/${api.v1}/mallPack/order/orderNo/{orderNo}")
    MallPackOrder findByOrderNo(@RequestHeader(value = "Authorization") String auth,
                                @PathVariable("orderNo") String orderNo);


}
