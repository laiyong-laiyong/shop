package com.sobey.module.balance.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.balance.entity.QueryRechargeBalanceParam;
import com.sobey.module.balance.model.RechargeBalanceRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/13 15:50
 */
@Mapper
@Repository
public interface RechargeBalanceRecordsMapper extends SupperMapper<RechargeBalanceRecords> {
    List<RechargeBalanceRecords> pages(@Param("page") Page<RechargeBalanceRecords> page,
                                       @Param("param") QueryRechargeBalanceParam param);
}
