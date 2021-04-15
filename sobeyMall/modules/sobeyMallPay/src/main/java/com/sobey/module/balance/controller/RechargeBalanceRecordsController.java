package com.sobey.module.balance.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.module.balance.entity.QueryRechargeBalanceParam;
import com.sobey.module.balance.model.RechargeBalanceRecords;
import com.sobey.module.balance.service.RechargeBalanceRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author WCY
 * @createTime 2020/7/13 16:16
 */
@RestController
@RequestMapping("/${api.v1}/rechargeRecords")
@Api(description = "余额充值记录相关接口")
public class RechargeBalanceRecordsController {

    @Autowired
    private RechargeBalanceRecordsService rbrs;

    @PostMapping
    @ApiOperation(value = "充值记录分页查询",httpMethod = "POST")
    public Page<RechargeBalanceRecords> pages(@RequestParam @ApiParam(value = "查询页码") int pageNum,
                                              @RequestParam @ApiParam(value = "每页条数") int pageSize,
                                              @RequestBody(required = false) QueryRechargeBalanceParam param){

        Page<RechargeBalanceRecords> page = new Page<>(pageNum,pageSize);
        return rbrs.pages(page,param);
    }

}
