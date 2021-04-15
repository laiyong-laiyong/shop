package com.sobey.module.bill.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.bill.model.PersonalBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/29 16:41
 */
@Mapper
@Repository
public interface PersonalBillMapper extends SupperMapper<PersonalBill> {


    PersonalBill personalBill(@Param("accountId") String accountId,
                              @Param("siteCode") String siteCode,
                              @Param("billDate") String billDate);

    void batchSave(@Param("list") List<PersonalBill> personalBills);

    /**
     * 查询消费趋势，(用户个人查询不查出限价字段)
     *
     * @param accountId
     * @param start
     * @param end
     * @return
     */
    List<PersonalBill> consumeTrend(@Param("accountId") String accountId,
                                    @Param("siteCode") String siteCode,
                                    @Param("start") String start,
                                    @Param("end") String end);

    /**
     * 总账单(管理后台账单页)的消费概览查询
     *
     * @param accountId
     * @param billDate
     * @return
     */
    PersonalBill consumeOverview(@Param("accountId") String accountId,
                                 @Param("siteCode") String siteCode,
                                 @Param("billDate") String billDate);

    /**
     * 账单表mall_bill的uuid
     *
     * @param uuid
     * @return
     */
    List<PersonalBill> consumeRank(@Param("uuid") String uuid);

    /**
     * 统计销售中心月度销售情况
     *
     * @param date
     * @param userCode 销售人员userCode
     * @return
     */
    PersonalBill monthSales(@Param("saleUserCode") String userCode,
                            @Param("date") String date);

    /**
     * 我的每月销售情况
     *
     * @param userCode
     * @param start
     * @param end
     * @return
     */
    List<PersonalBill> perMonthSales(@Param("saleUserCode") String userCode,
                                     @Param("start") String start,
                                     @Param("end") String end);

    /**
     * 销售中心的客户消费top
     *
     * @param userCode
     * @return
     */
    List<PersonalBill> customerConsumeRank(@Param("saleUserCode") String userCode);

    List<String> findUserCodes(@Param("saleUserCode") String userCode,
                               @Param("billDate") String billDate);
}
