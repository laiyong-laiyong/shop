package com.sobey.module.productservice.corn;

import com.sobey.framework.spring.SpringContextHolder;
import com.sobey.module.fegin.msg.request.service.MsgService;
import com.sobey.module.mallPack.model.MallPackOrder;
import com.sobey.module.mallPack.service.MallPackOrderService;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.order.model.Order;
import com.sobey.module.order.service.OrderService;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.productservice.service.ServiceInfoService;
import com.sobey.module.utils.CacheUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author WCY
 * @createTime 2020/5/9 15:17
 * 售出商品与金额的统计(实时) 初步定为5分钟统计一次
 */
public class SalesStatisticsJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(SalesStatisticsJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //首先查看这个月售出的商品数
        Date date = new Date();
        ServiceInfoService sis = SpringContextHolder.getBean(ServiceInfoService.class);
        int serviceNum = sis.collectServiceNum(date, ServiceStatus.OpenFail.getCode());
        //查询这个月1号到当前时间并且是已支付的订单
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        Date start = calendar.getTime();
//        String startDate = DateUtil.format(start, DateUtil.FORMAT_1);
//        String endDate = DateUtil.format(date, DateUtil.FORMAT_1);
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", start);
        map.put("endDate", date);
        map.put("paymentStatus", PayStatus.Paid.getCode());
        map.put("payStatus",PayStatus.Paid.getCode());
        OrderService os = SpringContextHolder.getBean(OrderService.class);
        MallPackOrderService mpos = SpringContextHolder.getBean(MallPackOrderService.class);
        List<Order> orders = os.statistic(map);
        List<MallPackOrder> packOrders =  mpos.statistic(map);

        Map<String, Object> param = new HashMap<>();
        param.put("num", serviceNum);
        BigDecimal amount = BigDecimal.ZERO;
        if (null != orders && orders.size() > 0){
            for (Order order : orders) {
                amount = amount.add(order.getPayAmount());
            }
        }
        if (null != packOrders && packOrders.size() > 0){
            for (MallPackOrder packOrder : packOrders) {
                amount = amount.add(packOrder.getPayAmount());
            }
        }
        param.put("amount", amount.toString());
        log.info("当月售出商品数:" + serviceNum + "--交易额:" + amount.toString());
        MsgService msgService = SpringContextHolder.getBean(MsgService.class);
        String token = CacheUtil.getToken();
        msgService.pushStatistics(token, param);
    }
}
