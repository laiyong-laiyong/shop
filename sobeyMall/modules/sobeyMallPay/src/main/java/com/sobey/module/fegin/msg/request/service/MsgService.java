package com.sobey.module.fegin.msg.request.service;

import com.sobey.module.fegin.msg.request.entity.MsgEntity;
import com.sobey.module.fegin.msg.request.entity.MsgTemplate;
import com.sobey.module.fegin.msg.request.entity.OperationsPersonnel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author WCY
 * @CreateTime 2020/4/16 15:24
 */
@FeignClient(name = "${address.msg.name}", url = "${address.msg.url}")
public interface MsgService {

    @PostMapping("/${api.v1}/message/system")
    Map<String, String> sendMsg(@RequestHeader(value = "Authorization") String token,
                                @RequestParam String templateUuid,
                                @RequestBody MsgEntity msgEntity);

    @PostMapping("/${api.v1}/template/list")
    List<MsgTemplate> queryTemplate(@RequestHeader(value = "Authorization") String token,
                                    @RequestBody Map<String, Object> map);

    @GetMapping("/${api.v1}/operaPersonnel")
    List<OperationsPersonnel> listOnDuty(@RequestHeader(value = "Authorization") String token,
                                   @RequestParam(required = false) String isOnDuty);

    @PostMapping("/${api.v1}/short-message/arrears")
    void sendArrearsMsgToAdmin(@RequestHeader(value = "Authorization") String token,
                               @RequestParam String phoneNumber,
                               @RequestParam String customer);

}
