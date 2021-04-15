package com.sobey.module.bill.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.bill.model.BillDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author WCY
 * @createTime 2020/7/29 16:41
 */
@Mapper
@Repository
public interface BillDetailMapper extends SupperMapper<BillDetail> {
    void batchSave(@Param("list") List<BillDetail> list);

    /**
     * 查询个人消费汇总
     * @param personalBillUuid
     * @return
     */
    List<BillDetail> personalBillDetails(String personalBillUuid);

    /**
     * 产品消费分布
     * @param uuid
     * @return
     */
    List<BillDetail> consumeDistribution(String uuid);

    List<BillDetail> consumeSum(@Param("productId") String productId,
                                @Param("perBillUuids") Set<String> perBillUuids);

    /**
     * 分别统计前十名用户消费top所消费的商品
     * @param pbUuid
     * @return
     */
    List<BillDetail> statisticConsume(@Param("pbUuid") String pbUuid);

    /**
     * 统计产品销售top排行
     * @param billUuid
     * @return
     */
    List<BillDetail> consumeRank(@Param("billUuid") String billUuid);
}
