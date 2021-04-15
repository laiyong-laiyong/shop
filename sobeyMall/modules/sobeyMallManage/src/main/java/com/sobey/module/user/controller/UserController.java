package com.sobey.module.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 用户相关
 * @Author WuChenYang
 * @Since 2020/1/21 14:35
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/queryDetail/{userCode}")
    public void queryDetail(@PathVariable(name = "userCode") String userCode) {

    }

}
