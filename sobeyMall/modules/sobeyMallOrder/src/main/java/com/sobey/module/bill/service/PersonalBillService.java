package com.sobey.module.bill.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.bill.mapper.BillDetailMapper;
import com.sobey.module.bill.mapper.NewBillDetailMapper;
import com.sobey.module.bill.mapper.PersonalBillMapper;
import com.sobey.module.bill.model.BillDetail;
import com.sobey.module.bill.model.NewBillDetail;
import com.sobey.module.bill.model.PersonalBill;
import com.sobey.module.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/30 11:55
 */
@Service
public class PersonalBillService extends ServiceImpl<PersonalBillMapper, PersonalBill> {

    @Autowired
    private PersonalBillMapper pbm;
    @Autowired
    private BillDetailMapper bdm;
    @Autowired
    private NewBillDetailMapper nbdm;

    public PersonalBill personalBill(String accountId, String siteCode, String billDate) {
        PersonalBill personalBill = pbm.personalBill(accountId, siteCode, billDate);
        //查询相关明细
        if (null != personalBill) {
            personalBill.setBillDetails(bdm.personalBillDetails(personalBill.getUuid()));
        }
        return personalBill;
    }

    /**
     * 查询消费趋势
     *
     * @param n
     * @return
     */
    public List<PersonalBill> consumeTrend(String accountId, String siteCode, int n) {

        Date now = new Date();
        String start = DateUtil.format(DateUtils.addMonths(now, -n), "yyyy/MM");
        String end = DateUtil.format(DateUtils.addMonths(now, -1), "yyyy/MM");
        List<PersonalBill> billList = pbm.consumeTrend(accountId, siteCode, start, end);
        if (null != billList && billList.size() > 0) {
            for (PersonalBill personalBill : billList) {
                String uuid = personalBill.getUuid();
                List<BillDetail> bds = bdm.consumeDistribution(uuid);
                personalBill.setBillDetails(bds);
            }
        }

        return billList;
    }

    /**
     * 查询产品消费分布
     *
     * @param userCode
     * @param billDate
     * @return
     */
    public PersonalBill consumeDistribution(String userCode, String siteCode, String billDate) {
        PersonalBill personalBill = pbm.personalBill(userCode, siteCode, billDate);
        if (null != personalBill) {
            List<BillDetail> bds = bdm.consumeDistribution(personalBill.getUuid());
            personalBill.setBillDetails(bds);
        }
        return personalBill;
    }

    /**
     * 总账单(管理后台账单页)的消费概览与产品消费汇总查询
     *
     * @param accountId
     * @param productId
     * @param billDate
     * @return
     */
    public PersonalBill bill(String accountId, String siteCode, String productId, String billDate) {

        PersonalBill personalBill = pbm.consumeOverview(accountId, siteCode, billDate);
        //存放个人账单表的主键集合,用于后续查询账单细节时使用
        HashSet<String> perBillUuids = new HashSet<>();
        if (null != personalBill) {
            perBillUuids.add(personalBill.getUuid());
            //这里判断是否有accountId参数，如果没有只根据账期去查询个人账单表
            if (StringUtils.isBlank(accountId)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("billDate", billDate);
                List<PersonalBill> list = pbm.selectByMap(map);
                for (PersonalBill pb : list) {
                    perBillUuids.add(pb.getUuid());
                }
            }
            //查询产品消费汇总
            List<BillDetail> bds = bdm.consumeSum(productId, perBillUuids);

            if (!StringUtils.isBlank(productId)) {
                //如果有条件查询从新计算总值
                personalBill.setBalAmount(BigDecimal.ZERO);
                personalBill.setBalRefundTotalAmount(BigDecimal.ZERO);
                personalBill.setWxAmount(BigDecimal.ZERO);
                personalBill.setWxRefundAmount(BigDecimal.ZERO);
                personalBill.setZfbAmount(BigDecimal.ZERO);
                personalBill.setZfbRefundAmount(BigDecimal.ZERO);
                personalBill.setVoucherAmount(BigDecimal.ZERO);
                personalBill.setTotalAmount(BigDecimal.ZERO);
                personalBill.setRefundTotalAmount(BigDecimal.ZERO);
                personalBill.setLimPriAmount(BigDecimal.ZERO);
                if (null != bds && bds.size() > 0) {
                    for (BillDetail billDetail : bds) {
                        personalBill.setBalAmount(personalBill.getBalAmount().add(billDetail.getBalAmount()));
                        personalBill.setBalRefundTotalAmount(personalBill.getBalRefundTotalAmount().add(billDetail.getBalRefundTotalAmount()));
                        personalBill.setWxAmount(personalBill.getWxAmount().add(billDetail.getWxAmount()));
                        personalBill.setWxRefundAmount(personalBill.getWxRefundAmount().add(billDetail.getWxRefundAmount()));
                        personalBill.setZfbAmount(personalBill.getZfbAmount().add(billDetail.getZfbAmount()));
                        personalBill.setZfbRefundAmount(personalBill.getZfbRefundAmount().add(billDetail.getZfbRefundAmount()));
                        personalBill.setVoucherAmount(personalBill.getVoucherAmount().add(billDetail.getVoucherAmount()));
                        personalBill.setTotalAmount(personalBill.getTotalAmount().add(billDetail.getTotalPayAmount()));
                        personalBill.setRefundTotalAmount(personalBill.getRefundTotalAmount().add(billDetail.getTotalRefundAmount()));
                        personalBill.setLimPriAmount(personalBill.getLimPriAmount().add(billDetail.getTotalLmiPri()));
                    }

                }
            }
            personalBill.setBillDetails(bds);
        }

        return personalBill;
    }

    /**
     * 月度统计
     *
     * @param accountId
     * @param productId
     * @param billDate
     * @return
     */
    public PersonalBill monthStatistic(String accountId, String siteCode, String productId, String billDate) {
        PersonalBill personalBill = bill(accountId, siteCode, productId, billDate);
        if (null != personalBill) {
            personalBill.setBillDetails(null);
            List<NewBillDetail> newBillDetails = nbdm.monthStatistic(accountId, siteCode, productId, billDate);
            personalBill.setNewBillDetails(newBillDetails);
        }
        return personalBill;
    }
}
