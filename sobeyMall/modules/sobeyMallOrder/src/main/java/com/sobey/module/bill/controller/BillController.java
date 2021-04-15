package com.sobey.module.bill.controller;

import com.sobey.exception.AppException;
import com.sobey.module.bill.model.Bill;
import com.sobey.module.bill.model.PersonalBill;
import com.sobey.module.bill.service.BillService;
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

/**
 * @author WCY
 * @createTime 2020/5/18 9:52
 * 总账单相关接口  需要结合前端页面需求去查看该部分接口，不然容易混淆
 */
@RestController
@RequestMapping("/${api.v1}/bill")
@Api(description = "总账单相关接口")
public class BillController {

    private static final Logger log = LoggerFactory.getLogger(BillController.class);

    @Autowired
    private PersonalBillService pbs;
    @Autowired
    private BillService bs;

    /**
     * 消费概览与产品消费汇总查询
     * @param accountId
     * @param productId
     * @param billDate
     * @return
     */
    @GetMapping("/overview")
    @ApiOperation(value = "消费概览与产品消费汇总查询",httpMethod = "GET")
    public PersonalBill bill(@RequestParam(required = false) @ApiParam(value = "与siteCode同时传或不传") String accountId,
                             @RequestParam(required = false) @ApiParam(value = "与accountId同时传或不传") String siteCode,
                             @RequestParam(required = false) String productId,
                             @RequestParam @ApiParam("账期 yyyy/MM") String billDate){
        //校验日期格式
        try {
            DateUtil.parse(billDate,"yyyy/MM");
        } catch (ParseException e) {
            throw new AppException("日期格式错误",e);
        }
        return pbs.bill(accountId,siteCode,productId,billDate);
    }

    /**
     * 月度统计
     * @param accountId
     * @param productId
     * @param billDate
     * @return
     */
    @GetMapping("/monthStatistic")
    @ApiOperation(value = "月度统计",httpMethod = "GET")
    public PersonalBill monthStatistic(@RequestParam(required = false) @ApiParam(value = "与siteCode同时传或不传") String accountId,
                                       @RequestParam(required = false) @ApiParam(value = "与accountId同时传或不传") String siteCode,
                                       @RequestParam(required = false) String productId,
                                       @RequestParam @ApiParam("账期 yyyy/MM") String billDate){
        //校验日期格式
        try {
            DateUtil.parse(billDate,"yyyy/MM");
        } catch (ParseException e) {
            throw new AppException("日期格式错误",e);
        }
        return pbs.monthStatistic(accountId,siteCode,productId,billDate);
    }

    /**
     * 用户消费top与产品销售top
     * @param billDate
     * @return
     */
    @GetMapping("/consumeRank")
    @ApiOperation(value = "用户消费top与产品销售top",httpMethod = "GET")
    public Bill consumeRank(@RequestParam @ApiParam("账期 yyyy/MM") String billDate){
        //校验日期格式
        try {
            DateUtil.parse(billDate,"yyyy/MM");
        } catch (ParseException e) {
            throw new AppException("日期格式错误",e);
        }
        return bs.consumeRank(billDate);
    }

}
