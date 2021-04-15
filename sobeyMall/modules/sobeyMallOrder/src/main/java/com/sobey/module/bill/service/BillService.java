package com.sobey.module.bill.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.bill.mapper.BillDetailMapper;
import com.sobey.module.bill.mapper.BillMapper;
import com.sobey.module.bill.mapper.NewBillDetailMapper;
import com.sobey.module.bill.mapper.PersonalBillMapper;
import com.sobey.module.bill.model.Bill;
import com.sobey.module.bill.model.BillDetail;
import com.sobey.module.bill.model.NewBillDetail;
import com.sobey.module.bill.model.PersonalBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/29 16:40
 */
@Service
public class BillService extends ServiceImpl<BillMapper, Bill> {

    @Autowired
    private BillMapper bm;
    @Autowired
    private BillDetailMapper bdm;
    @Autowired
    private PersonalBillMapper pbm;
    @Autowired
    private NewBillDetailMapper nbdm;

    @Transactional(rollbackFor = Exception.class)
    public void save(Bill bill, List<PersonalBill> personalBills, List<BillDetail> billDetails, List<NewBillDetail> newBillDetails) {
        bm.insert(bill);
        if (null != billDetails && billDetails.size() > 0) {
            bdm.batchSave(billDetails);
        }
        if (null != personalBills && personalBills.size() > 0) {
            pbm.batchSave(personalBills);
        }
        if (null != newBillDetails && newBillDetails.size() > 0){
            nbdm.batchSave(newBillDetails);
        }
    }


    /**
     * 用户消费top与产品销售top
     *
     * @param billDate
     * @return
     */
    public Bill consumeRank(String billDate) {
        Bill bill = bm.findByBillDate(billDate);

        if (null != bill) {
            String uuid = bill.getUuid();
            //首先统计用户消费top前10名
            List<PersonalBill> pbs = pbm.consumeRank(uuid);
            //统计前十名用户消费的商品
            for (PersonalBill pb : pbs) {
                List<BillDetail> bds = bdm.statisticConsume(pb.getUuid());
                pb.setBillDetails(bds);
            }
            bill.setPersonalBills(pbs);
            //统计产品销售前5名
            List<BillDetail> bds = bdm.consumeRank(uuid);
            bill.setBillDetails(bds);
        }

        return bill;
    }
}
