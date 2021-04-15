package com.sobey.module.fegin.mallPack.pack.service;

import com.sobey.module.fegin.mallPack.pack.entity.MallPack;
import com.sobey.module.fegin.mallPack.packOrder.entity.response.ResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author WCY
 * @createTime 2020/5/21 14:24
 */
@FeignClient(name = "${address.order.name}", url = "${address.order.url}")
public interface MallPackService {

    @PostMapping("/${api.v1}/mallPack/pack")
    ResultInfo createPack(@RequestHeader(value = "Authorization") String token,
                          @RequestBody MallPack mallPack);

}
