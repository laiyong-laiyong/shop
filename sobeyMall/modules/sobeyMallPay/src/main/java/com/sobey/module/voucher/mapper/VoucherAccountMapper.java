package com.sobey.module.voucher.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.voucher.model.VoucherAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VoucherAccountMapper extends SupperMapper<VoucherAccount> {

    VoucherAccount findByAccountId(@Param("accountId") String accountId);
}
