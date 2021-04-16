package com.sobey.module.balanceRecharge.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.balanceRecharge.model.BalanceRecharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BalanceRechargeMapper extends SupperMapper<BalanceRecharge> {

    BalanceRecharge getOne(@Param("id") Long id);
    List<BalanceRecharge> page(Page<BalanceRecharge> page,BalanceRecharge balanceRecharge);
    List<BalanceRecharge> list(BalanceRecharge balanceRecharge);
    List getTotal();
}
