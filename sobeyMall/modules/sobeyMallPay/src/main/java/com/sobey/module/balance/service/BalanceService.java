package com.sobey.module.balance.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.balance.mapper.BalanceMapper;
import com.sobey.module.balance.model.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/6 10:59
 */
@Service
public class BalanceService extends ServiceImpl<BalanceMapper, Balance> {

    private static final Logger log = LoggerFactory.getLogger(BalanceService.class);

    @Autowired
    private BalanceMapper bm;


    public Balance findByAccountId(String accountId) {
        return bm.findByAccountId(accountId);
    }


    public int updateCredits(String accountId, BigDecimal credits) {
        return bm.updateCredits(accountId,credits);
    }

    public Page<Balance> findAccounts(Page<Balance> page,String accountId, String account){
        List<Balance> accounts = bm.findAccounts(page, accountId, account);
        page.setRecords(accounts);
        return page;
    }

}
