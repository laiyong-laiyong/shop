package com.sobey.module.fegin.voucher.request.service;

import com.sobey.module.fegin.voucher.response.entity.VoucherAccount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author WCY
 * @createTime 2020/6/19 11:40
 */
@FeignClient(name = "${address.pay.name}",url = "${address.pay.url}")
public interface VouAccountService {

    @GetMapping("/${api.v1}/vouAccount/detail")
    VoucherAccount detail(@RequestHeader(value = "Authorization") String token,
                          @RequestParam String accountId);

    @PutMapping("/${api.v1}/vouAccount")
    void update(@RequestHeader(value = "Authorization") String token,
                @RequestBody VoucherAccount vouAccount);

}
