package com.sobey.module.balanceRecharge.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.exception.AppException;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.balanceRecharge.common.ResultInfo;
import com.sobey.module.balanceRecharge.model.BalanceRecharge;
import com.sobey.module.balanceRecharge.service.BalanceRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("${api.v1}/balance-recharge")
@Api(value = "余额充值", description = "余额充值相关接口")
public class BalanceRechargeController {

    @Autowired
    private BalanceRechargeService brs;

    @PassToken
    @PostMapping
    @ApiOperation(value = "充值提交",httpMethod = "POST")
    public ResultInfo update(@RequestBody BalanceRecharge balanceRecharge,@RequestHeader("token") String token ){
        if(balanceRecharge == null){
           throw new AppException("参数为空");
        }
        return brs.insertMs(balanceRecharge,token);
    }

    @PostMapping(value = "/page")
    @ApiOperation(value = "查询订单")
    public Page<BalanceRecharge> page(@RequestParam(name = "page",defaultValue = "1") int page, @RequestParam(name = "size",defaultValue = "20") int size,@RequestBody(required=false) BalanceRecharge balanceRecharge){
        Page<BalanceRecharge> page1 = new Page<BalanceRecharge>(page,size);
        page1 = brs.page(page1,balanceRecharge);
        return page1;
    }

    @PostMapping(value = "/check")
    @ApiOperation(value = "充值核查",httpMethod = "POST")
    public ResultInfo check(@RequestBody BalanceRecharge br){
        if(br == null){
            throw new AppException("参数为空");
        }
        return brs.getOne(br);
    }

    @PostMapping(value = "/download")
    @ApiOperation(value = "下载数据")
    public void download(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "language",defaultValue = "cn") String language, @RequestBody(required=false) BalanceRecharge balanceRecharge) throws IOException {
        brs.download(request,response,language,balanceRecharge);
    }

    @PassToken
    @GetMapping
    @ApiOperation(value = "总额统计",httpMethod = "GET")
    public ResultInfo getTotal() {
        return new ResultInfo();
    }
}
