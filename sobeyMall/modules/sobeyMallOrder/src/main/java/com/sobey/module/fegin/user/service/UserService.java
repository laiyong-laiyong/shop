package com.sobey.module.fegin.user.service;

import com.sobey.module.fegin.user.response.UserDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/2/24 15:03
 */
@FeignClient(name = "${address.auth.name}", url = "${address.auth.url}")
public interface UserService {

    @GetMapping("/v3.0/cross-tenant/{site_code}/user/{userCode}")
    UserDetail userDetail(@PathVariable(name = "site_code") String site_code,
                          @PathVariable(name = "userCode") String userCode,
                          @RequestHeader(value = "sobeyhive-http-token") String auth,
                          @RequestHeader(value = "sobeycloud-site",defaultValue = "sobeyLingYunMall") String site);//site只是临时的没有实质作用，按照认证中心的接口添加的

    @PutMapping("/v1.0/user")
    void update(@RequestBody UserDetail userDetail, @RequestHeader(value = "sobeyhive-http-token") String auth);

}
