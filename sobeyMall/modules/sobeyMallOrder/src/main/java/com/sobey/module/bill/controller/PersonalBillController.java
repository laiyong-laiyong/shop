package com.sobey.module.bill.controller;

import com.sobey.exception.AppException;
import com.sobey.module.bill.model.PersonalBill;
import com.sobey.module.bill.service.PersonalBillService;
import com.sobey.module.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/30 14:33
 * 个人账单相关接口 需要结合前端页面需求去查看该部分接口，不然容易混淆
 */
@RestController
@RequestMapping("/${api.v1}/personalBill")
@Api(description = "个人账单相关接口")
public class PersonalBillController {

    private static final Logger log = LoggerFactory.getLogger(PersonalBillController.class);

    @Autowired
    private PersonalBillService pbs;
    /**
     * 个人账单查询
     * @param billDate
     * @return
     */
    @GetMapping
    @ApiOperation(value = "个人账单查询",httpMethod = "GET",notes = "该接口同时会返回产品消费汇总数据")
    public PersonalBill personalBill(@RequestParam String accountId,
                                     @RequestParam String siteCode,
                                     @RequestParam @ApiParam(value = "账期 yyyy/MM") String billDate){
        //校验日期格式
        try {
            DateUtil.parse(billDate,"yyyy/MM");
        } catch (ParseException e) {
            throw new AppException("日期格式错误",e);
        }
        return pbs.personalBill(accountId,siteCode,billDate);
    }

    /**
     * 查询近n个月的消费趋势
     * @param n
     * @return
     */
    @GetMapping("/consumeTrend")
    @ApiOperation(value = "查询近n个月的消费趋势",httpMethod = "GET")
    public List<PersonalBill> consumeTrend(@RequestParam String accountId,
                                           @RequestParam String siteCode,
                                           @RequestParam int n){
        if (n<=0){
            n = 6;
        }
        return pbs.consumeTrend(accountId,siteCode,n);
    }

    /**
     * 查询产品消费分布
     * @param accountId
     * @param billDate
     * @return
     */
    @GetMapping("/consumeDistribution")
    @ApiOperation(value = "查询产品消费分布",httpMethod = "GET")
    public PersonalBill consumeDistribution(@RequestParam String accountId,
                                            @RequestParam String siteCode,
                                            @RequestParam @ApiParam(value = "账期 yyyy/MM") String billDate){
        //校验日期格式
        try {
            DateUtil.parse(billDate,"yyyy/MM");
        } catch (ParseException e) {
            throw new AppException("日期格式错误",e);
        }
        return pbs.consumeDistribution(accountId,siteCode,billDate);
    }

}
