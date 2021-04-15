package com.sobey.module.balance.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.balance.entity.QueryRechargeBalanceParam;
import com.sobey.module.balance.mapper.RechargeBalanceRecordsMapper;
import com.sobey.module.balance.model.RechargeBalanceRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/13 15:52
 */
@Service
public class RechargeBalanceRecordsService extends ServiceImpl<RechargeBalanceRecordsMapper, RechargeBalanceRecords> {

    @Autowired
    private RechargeBalanceRecordsMapper rbrm;

    public Page<RechargeBalanceRecords> pages(Page<RechargeBalanceRecords> page, QueryRechargeBalanceParam param) {

        List<RechargeBalanceRecords> list = rbrm.pages(page,param);

        page.setRecords(list);
        return page;
    }
}
