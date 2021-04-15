package com.sobey.module.fegin.msg.request.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author WCY
 * @createTime 2020/5/19 15:16
 * 发送短信
 */
@FeignClient(name = "${address.msg.name}", url = "${address.msg.url}")
public interface ShortMsgService {

    /**
     * 创建代金券给下发人员发送短信通知
     *
     * @param phoneNumber
     * @param name
     * @param voucherCode
     * @param notifyType
     */
    @PostMapping("/${api.v1}/short-message/voucher")
    void sendNormalShortMessage(@RequestHeader(value = "Authorization") String token,
                                @RequestParam String phoneNumber,
                                @RequestParam String name,
                                @RequestParam String voucherCode,
                                @RequestParam String notifyType);

}
