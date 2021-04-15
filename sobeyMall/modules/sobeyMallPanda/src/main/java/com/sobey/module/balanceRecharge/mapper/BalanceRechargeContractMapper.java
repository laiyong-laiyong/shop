package com.sobey.module.balanceRecharge.mapper;


import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.balanceRecharge.model.BalanceRechargeContract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BalanceRechargeContractMapper extends SupperMapper<BalanceRechargeContract> {
        List<BalanceRechargeContract> list(@Param("projectId") Long projectId);
}
