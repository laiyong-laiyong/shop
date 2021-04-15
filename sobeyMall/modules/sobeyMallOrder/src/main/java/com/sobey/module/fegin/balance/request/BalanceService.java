package com.sobey.module.fegin.balance.request;

import com.sobey.module.fegin.balance.response.Balance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/19 16:47
 */
@FeignClient(name = "${address.pay.name}",url = "${address.pay.url}")
public interface BalanceService {

    @GetMapping("/${api.v1}/balance/query")
    Balance query(@RequestHeader(value = "Authorization") String auth, @RequestParam String accountId);

    @PutMapping("/${api.v1}/balance")
    void update(@RequestHeader(value = "Authorization") String auth, @RequestBody Balance balance);

}
