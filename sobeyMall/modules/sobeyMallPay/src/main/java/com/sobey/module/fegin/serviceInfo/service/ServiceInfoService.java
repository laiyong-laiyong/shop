package com.sobey.module.fegin.serviceInfo.service;

import com.sobey.module.fegin.serviceInfo.entity.response.ResultInfo;
import com.sobey.module.fegin.serviceInfo.entity.response.ServiceInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description 商品服务相关接口
 * @Author WuChenYang
 * @CreateTime 2020/3/16 18:20
 */
@FeignClient(name = "${address.order.name}",url = "${address.order.url}")
public interface ServiceInfoService {

    @PostMapping(value = "/${api.v1}/service")
    ResultInfo createService(@RequestHeader(value = "Authorization") String auth,
                             @RequestBody ServiceInfo serviceInfo);

    @PutMapping(value = "/${api.v1}/service")
    boolean update(@RequestHeader(value = "Authorization") String auth,
                   @RequestBody ServiceInfo serviceInfo);

    @PutMapping(value = "/${api.v1}/service/renew")
    ResultInfo renew(@RequestHeader(value = "Authorization") String auth,
                     @RequestBody ServiceInfo serviceInfo);

    @GetMapping(value = "/${api.v1}/service")
    ServiceInfo select(@RequestHeader(value = "Authorization") String auth,
                       @RequestParam String serviceNo);

    @PostMapping(value = "/${api.v1}/service/list")
    List<ServiceInfo> list(@RequestHeader(value = "Authorization") String auth,
                           @RequestBody Map<String,Object> map);

    @GetMapping(value = "/${api.v1}/service/isOpened")
    List<ServiceInfo> isOpened(@RequestHeader(value = "Authorization") String auth,
                               @RequestParam String accountId,
                               @RequestParam String productId);

    /**
     * 充值成功后通知扫描已经关闭的按量服务然后重新开通
     * @param auth
     * @param flag
     */
    @PostMapping(value = "/${api.v1}/service/finishRecharge")
    void finishRecharge(@RequestHeader(value = "Authorization") String auth,
                        @RequestParam boolean flag,
                        @RequestParam String accountId);

    @PostMapping(value = "/${api.v1}/saas/open")
    ResultInfo openSaas(@RequestHeader(value = "Authorization") String token,
                        @RequestBody ServiceInfo serviceInfo);
}
