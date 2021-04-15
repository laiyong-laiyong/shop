package com.sobey.module.account.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.module.balance.model.Balance;
import com.sobey.module.balance.service.BalanceService;
import com.sobey.module.voucher.model.VoucherAccount;
import com.sobey.module.voucher.service.VoucherAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户管理相关接口，查询用户的金额包括余额代金券等信息
 * @author WCY
 * @createTime 2020/9/3 9:57
 */
@RestController
@RequestMapping("/${api.v1}/account")
@Api(description = "账户管理相关接口")
public class AccountInfosController {

    @Autowired
    private BalanceService bs;
    @Autowired
    private VoucherAccountService vas;

    @PostMapping("/accountAmounts")
    @ApiOperation(value = "账户金额信息",httpMethod = "POST")
    public Page<Balance> accountAmounts(@RequestParam int pageNum,
                                        @RequestParam int pageSize,
                                        @RequestParam(required = false) String accountId,
                                        @RequestParam(required = false) String account){
        Page<Balance> page = new Page<>(pageNum, pageSize);

        page = bs.findAccounts(page,accountId, account);
        List<Balance> accounts = page.getRecords();
        List<VoucherAccount> list = vas.selectByMap(null);
        if (null == accounts || accounts.size() == 0){
            return page;
        }
        for (Balance balance : accounts) {
            if (null == balance.getCredits()){
                balance.setCredits(BigDecimal.ZERO);
            }
            for (VoucherAccount voucherAccount : list) {
                if (balance.getAccountId().equals(voucherAccount.getAccountId())){
                    balance.setVoucherAmount(voucherAccount.getVouAmount());
                    break;
                }
            }
        }
        page.setRecords(accounts);
        return page;
    }

}
