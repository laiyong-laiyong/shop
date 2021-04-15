package com.sobey.module.balance.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.balance.model.Balance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/6 11:00
 */
@Mapper
@Repository
public interface BalanceMapper extends SupperMapper<Balance> {


    Balance findByAccountId(@Param("accountId") String accountId);

    int updateCredits(@Param("accountId") String accountId,
                      @Param("credits") BigDecimal credits);

    List<Balance> findAccounts(@Param("page") Page<Balance> page,
                               @Param("accountId") String accountId,
                               @Param("account") String account);
}
