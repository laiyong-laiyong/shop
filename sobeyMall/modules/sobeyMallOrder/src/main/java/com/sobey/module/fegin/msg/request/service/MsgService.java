package com.sobey.module.fegin.msg.request.service;

import com.sobey.module.fegin.msg.request.entity.MsgEntity;
import com.sobey.module.fegin.msg.request.entity.MsgTemplate;
import com.sobey.module.fegin.msg.request.entity.OperationsPersonnel;
import com.sobey.module.order.entity.ResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author WCY
 * @CreateTime 2020/4/16 15:24 ${address.msg.url}
 */
@FeignClient(name = "${address.msg.name}", url = "${address.msg.url}")
public interface MsgService {

    @PostMapping("/${api.v1}/message/system")
    Map<String, String> sendMsg(@RequestHeader(value = "Authorization") String token,
                                @RequestParam String templateUuid,
                                @RequestBody MsgEntity msgEntity);

    @PostMapping("/${api.v1}/message/pushStatistics")
    void pushStatistics(@RequestHeader(value = "Authorization") String token,
                        @RequestBody Map<String,Object> map);

    @PostMapping("/${api.v1}/template/list")
    List<MsgTemplate> queryTemplate(@RequestHeader(value = "Authorization") String token,
                                    @RequestBody Map<String,Object> map);

    @PostMapping("/${api.v1}/message/sendSaasMsg")
    ResultInfo sendSaasMsg(@RequestHeader(value = "Authorization") String token,
                           @RequestBody Map<String,String> msg);

    @GetMapping("/${api.v1}/operaPersonnel")
    List<OperationsPersonnel> listOnDuty(@RequestHeader(value = "Authorization") String token,
                                         @RequestParam(required = false) String isOnDuty);

    @PostMapping("/${api.v1}/short-message/arrears")
    void sendArrearsMsgToAdmin(@RequestHeader(value = "Authorization") String token,
                               @RequestParam String phoneNumber,
                               @RequestParam String customer);

}
