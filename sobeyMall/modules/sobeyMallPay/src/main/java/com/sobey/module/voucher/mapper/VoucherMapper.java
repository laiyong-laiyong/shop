package com.sobey.module.voucher.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.voucher.model.Voucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wcy
 */
@Mapper
@Repository
public interface VoucherMapper extends SupperMapper<Voucher> {

    List<Voucher> pages(Page<Voucher> page,Voucher voucher);

    void batchSave(@Param("vouchers") List<Voucher> vouchers);
}
