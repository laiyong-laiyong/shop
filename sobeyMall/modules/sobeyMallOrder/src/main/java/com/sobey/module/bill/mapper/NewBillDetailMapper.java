package com.sobey.module.bill.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.bill.model.NewBillDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WCY
 * @create 2020/10/9 15:05
 */
@Mapper
@Repository
public interface NewBillDetailMapper extends SupperMapper<NewBillDetail> {
    void batchSave(@Param("list") List<NewBillDetail> newBillDetails);

    List<NewBillDetail> monthStatistic(@Param("accountId") String accountId,
                                       @Param("siteCode") String siteCode,
                                       @Param("productId") String productId,
                                       @Param("billDate") String billDate);
}
