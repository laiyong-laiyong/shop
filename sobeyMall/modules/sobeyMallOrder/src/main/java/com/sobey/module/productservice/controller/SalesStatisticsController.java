package com.sobey.module.productservice.controller;

import com.sobey.module.mallPack.model.MallPackOrder;
import com.sobey.module.mallPack.service.MallPackOrderService;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.order.model.Order;
import com.sobey.module.order.service.OrderService;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.productservice.service.ServiceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WCY
 * @createTime 2020/5/9 17:24
 * 商品与交易额统计相关
 */
@RestController
@RequestMapping("/${api.v1}/salesStatistics")
@Api(description = "商品与交易额统计相关接口")
public class SalesStatisticsController {

    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private OrderService os;
    @Autowired
    private MallPackOrderService mpos;

    @ApiOperation(value = "当月交易额与出售商品数统计接口")
    @GetMapping("/currentMonth")
    public Map<String, Object> currentMonth(){
        //首先查看这个月售出的商品数
        Date date = new Date();
        int serviceNum = sis.collectServiceNum(date, ServiceStatus.OpenFail.getCode());

        //统计这个月交易额
        //查询这个月1号到当前时间并且是已支付的订单
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        Date start = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        Map<String, Object> map = new HashMap<>();
        map.put("startDate", start);
        map.put("endDate", date);
        map.put("paymentStatus", PayStatus.Paid.getCode());
        map.put("payStatus",PayStatus.Paid.getCode());

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
        return param;
    }


}
