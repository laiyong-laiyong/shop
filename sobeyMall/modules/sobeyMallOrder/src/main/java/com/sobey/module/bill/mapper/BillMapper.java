package com.sobey.module.bill.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.bill.model.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author WCY
 * @createTime 2020/7/29 16:41
 */
@Mapper
@Repository
public interface BillMapper extends SupperMapper<Bill> {

    Bill findByBillDate(@Param("billDate") String billDate);
}
