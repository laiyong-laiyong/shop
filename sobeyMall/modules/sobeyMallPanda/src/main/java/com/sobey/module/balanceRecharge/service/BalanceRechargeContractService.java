package com.sobey.module.balanceRecharge.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.balanceRecharge.mapper.BalanceRechargeContractMapper;
import com.sobey.module.balanceRecharge.model.BalanceRechargeContract;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceRechargeContractService extends ServiceImpl<BalanceRechargeContractMapper, BalanceRechargeContract> {

    @Autowired
    private BalanceRechargeContractMapper brcm;

    //添加记录

    //获取
    public List<BalanceRechargeContract> list(Long projectId){
        return brcm.list(projectId);
    }
}
