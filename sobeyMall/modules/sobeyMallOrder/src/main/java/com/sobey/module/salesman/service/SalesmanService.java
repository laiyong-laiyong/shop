package com.sobey.module.salesman.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.exception.AppException;
import com.sobey.module.bill.mapper.PersonalBillMapper;
import com.sobey.module.bill.model.PersonalBill;
import com.sobey.module.mallPack.model.MallPackOrder;
import com.sobey.module.mallPack.service.MallPackOrderService;
import com.sobey.module.order.model.Order;
import com.sobey.module.order.service.OrderService;
import com.sobey.module.salescustomer.mapper.SalescustomerMapper;
import com.sobey.module.salesman.mapper.SalesmanMapper;
import com.sobey.module.salesman.model.Salesman;
import com.sobey.module.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class SalesmanService extends ServiceImpl<SalesmanMapper, Salesman> {

    private static final Logger log = LoggerFactory.getLogger(SalesmanService.class);

    @Autowired
    private PersonalBillMapper pbm;
    @Autowired
    private SalesmanMapper sm;
    @Autowired
    private SalescustomerMapper scm;
    @Autowired
    private OrderService os;
    @Autowired
    private MallPackOrderService mpos;

    public Page<Salesman> page(Page<Salesman> page, Salesman entity) {

        List<Salesman> cts = this.baseMapper.page(page, entity);
        page.setRecords(cts);
        return page;
    }

    /**
     * 月度销售情况查询
     *
     * @param date
     * @return
     */
    public Page<Salesman> monthSales(Page<Salesman> page, String date, String loginName, String name) {
        //查询所有的销售人员
        List<Salesman> salesmen = sm.selectByNames(page, loginName, name);
        if (salesmen != null && salesmen.size() > 0) {
            Map<String, Salesman> saleMap = new HashMap<>();
            salesmen.forEach(salesman -> saleMap.put(salesman.getUserCode(), salesman));
            salesmen = new ArrayList<>(saleMap.values());
            for (Salesman salesman : salesmen) {
                PersonalBill pb = pbm.monthSales(salesman.getUserCode(), date);
                salesman.setSaleAmount(BigDecimal.ZERO);
                salesman.setSaleProfit(BigDecimal.ZERO);
                if (null != pb) {
                    //减去代金券的限价
                    subtractVouLimPri(salesman.getUserCode(),pb);
                    //总销售额不包括代金券
                    salesman.setSaleAmount(pb.getTotalAmount().subtract(pb.getVoucherAmount()));
                    salesman.setSaleProfit(pb.getTotalAmount().subtract(pb.getLimPriAmount()).subtract(pb.getVoucherAmount()));
                }
            }
        }
        page.setRecords(salesmen);
        return page;
    }

    public void check(String userCode) {

        EntityWrapper<Salesman> wp = new EntityWrapper<Salesman>();
        wp.eq("userCode", userCode);
        List<Salesman> list = this.baseMapper.selectList(wp);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new AppException("此人员已经存在，请检查");
        }

    }

    /**
     * 查询我的每月销售情况
     *
     * @param userCode
     * @param start
     * @param end
     * @return
     */
    public List<Salesman> perMonthSales(String userCode, String start, String end) {
        //查询销售对应的用户userCodes
        List<PersonalBill> personalBills = pbm.perMonthSales(userCode, start, end);
        List<Salesman> salesmen = new ArrayList<>();
        if (null == personalBills || personalBills.size() == 0) {
            return salesmen;
        }
        //由于账单中的限价包含了代金券订单，所以需要出去每月代金券订单的限价
        for (PersonalBill personalBill : personalBills) {
            subtractVouLimPri(userCode,personalBill);
        }
        EntityWrapper<Salesman> wp = new EntityWrapper<>();
        wp.eq("userCode", userCode);
        List<Salesman> list = this.baseMapper.selectList(wp);
        if (null != list && list.size() > 0) {
            Salesman salesman = list.get(0);
            for (PersonalBill bill : personalBills) {
                Salesman sman = new Salesman();
                BeanUtils.copyProperties(salesman, sman);
                sman.setBillDate(bill.getBillDate());
                sman.setSaleAmount(bill.getTotalAmount().subtract(bill.getVoucherAmount()));
                sman.setSaleProfit(bill.getTotalAmount().subtract(bill.getLimPriAmount()).subtract(bill.getVoucherAmount()));
                salesmen.add(sman);
            }
            salesmen.sort((o1, o2) -> {
                String date1 = o1.getBillDate();
                String date2 = o2.getBillDate();
                long time1 = 0;
                long time2 = 0;
                try {
                    time1 = DateUtil.parse(date1, "yyyy/MM").getTime();
                    time2 = DateUtil.parse(date2, "yyyy/MM").getTime();
                } catch (ParseException e) {
                    log.info("时间格式错误,{}",e.getMessage());
                }
                return Long.compare(time1,time2);
            });
        }
        return salesmen;
    }

    /**
     * 客户消费top
     *
     * @param userCode
     * @return
     */
    public List<PersonalBill> customerConsumeRank(String userCode) {
        List<PersonalBill> personalBills = pbm.customerConsumeRank(userCode);
        if (null != personalBills && personalBills.size() > 0){
            //排序
            personalBills.sort((o1,o2)->{
                BigDecimal consume2 = o2.getTotalAmount().subtract(o2.getVoucherAmount());
                BigDecimal consume1 = o1.getTotalAmount().subtract(o1.getVoucherAmount());
                return consume2.compareTo(consume1);
            });
        }
        return personalBills;
    }

    /**
     * 我的当月销售额与销售利润
     *
     * @param saleUserCode
     * @return
     */
    public Salesman currentMonthSale(String saleUserCode) {
        //查询该销售的客户userCode
        List<String> customerUserCodes = scm.customerUserCodes(saleUserCode);
        EntityWrapper<Salesman> wp = new EntityWrapper<Salesman>();
        wp.eq("userCode", saleUserCode);
        List<Salesman> list = this.baseMapper.selectList(wp);
        Salesman salesman = null;
        if (null != list && list.size() > 0) {
            salesman = list.get(0);
            salesman.setSaleAmount(BigDecimal.ZERO);
            salesman.setSaleProfit(BigDecimal.ZERO);
            if (null != customerUserCodes && customerUserCodes.size() > 0) {
                Date now = new Date();
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
                calendar.setTime(now);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date start = calendar.getTime();
                List<Order> orders = os.statisticByAccountId(customerUserCodes, start, now);
                List<MallPackOrder> packOrders = mpos.statisticByAccountId(customerUserCodes, start, now);
                if (null != orders && orders.size() > 0){
                    for (Order order : orders) {
                        salesman.setSaleAmount(salesman.getSaleAmount().add(order.getPayAmount()));
                        salesman.setSaleProfit(salesman.getSaleProfit().add(order.getPayAmount().subtract(order.getLimPri())));
                    }
                }
                if (null != packOrders && packOrders.size() > 0){
                    for (MallPackOrder packOrder : packOrders) {
                        salesman.setSaleAmount(salesman.getSaleAmount().add(packOrder.getPayAmount()));
                        salesman.setSaleProfit(salesman.getSaleProfit().add(packOrder.getPayAmount().subtract(packOrder.getLimPri())));
                    }
                }
            }
        }

        return salesman;
    }

    /**
     * 减去指定条件的代金券订单的限价
     * @param saleUserCode
     * @param personalBill
     */
    private void subtractVouLimPri(String saleUserCode,PersonalBill personalBill){
        String billDate = personalBill.getBillDate();
        //查询该账期下的所有用户ID
        List<String> userCodes = pbm.findUserCodes(saleUserCode,billDate);
        try {
            //统计该账期下对应的用户的代金券订单的限价金额
            Date date = DateUtil.parse(billDate,"yyyy/MM");
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            //设置时间条件
            calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date st = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.SECOND,59);
            calendar.set(Calendar.MILLISECOND,999);
            Date ed = calendar.getTime();
            List<Order> orders = os.statisticVoucherLimPri(userCodes, st, ed);
            if (null != orders && orders.size() > 0){
                for (Order order : orders) {
                    if (null == order.getLimPri()){
                        continue;
                    }
                    personalBill.setLimPriAmount(personalBill.getLimPriAmount().subtract(order.getLimPri()));
                }
            }
            //套餐包订单
            List<MallPackOrder> packOrders = mpos.statisticVoucherLimPri(userCodes, st, ed);
            if (null != packOrders && packOrders.size() > 0){
                for (MallPackOrder packOrder : packOrders) {
                    if (null == packOrder.getLimPri()){
                        continue;
                    }
                    personalBill.setLimPriAmount(personalBill.getLimPriAmount().subtract(packOrder.getLimPri()));
                }
            }
        } catch (ParseException e) {

        }
    }

}
