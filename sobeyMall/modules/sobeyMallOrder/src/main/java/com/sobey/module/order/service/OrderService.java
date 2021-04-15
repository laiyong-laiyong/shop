package com.sobey.module.order.service;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.order.common.OpenType;
import com.sobey.module.order.model.Order;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.productservice.mapper.MetricUsageMapper;
import com.sobey.module.productservice.model.MetricUsage;
import com.sobey.module.utils.DateUtil;
import com.sobey.module.utils.QueryParamUtil;
import com.sobey.util.common.page.PageModel;
import com.sobey.util.common.page.SobeyPageable;
import com.sobey.util.common.page.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by PengJK on 2018/8/29.
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${mongo.collection.mall_order}")
    private String mallOrder;
    @Autowired
    private MetricUsageMapper mum;

    /**
     * 分页查询
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param param    查询参数:
     *                 orderNo orderName(模糊) _id productType
     *                 paymentStatus accountId startDate(开始时间) endDate(结束时间)
     * @return
     */
    public Page<Order> pages(int pageNum, int pageSize, Map<String, Object> param) {
        Query query = new Query();
        Criteria criteria = QueryParamUtil.getCriteria(param);
        query.addCriteria(criteria);
        PageModel page = new PageModel();
        SobeyPageable pageable = new SobeyPageable();
        //对时间降序排序
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Sort sort = SortUtil.getSort(orders);

        page.setPagenumber(pageNum);
        page.setPagesize(pageSize);
        page.setSort(sort);
        pageable.setPage(page);
        //查询出一共多少条
        long total = mongoTemplate.count(query, Order.class, mallOrder);
        //查询
        List<Order> list = mongoTemplate.find(query.with(pageable), Order.class, mallOrder);
        if (list.size() > 0){
            //查询出所有按需订单对应的计费信息
            List<String> orderNos = list.stream()
                    .filter(order -> OpenType.Demand.getCode().equals(order.getOpenType()))
                    .map(Order::getOrderNo).collect(Collectors.toList());
            List<MetricUsage> usages = null;
            if (orderNos.size() > 0){
                usages = mum.getUsagesByOrderNos(orderNos);
            }
            for (Order order : list) {
                if (null != usages && usages.size() > 0) {
                    List<MetricUsage> usageList = usages.stream()
                            .filter(metricUsage -> order.getOrderNo().equals(metricUsage.getOrderNo()))
                            .collect(Collectors.toList());
                    order.setMetricUsages(usageList);
                }
            }
        }
        return new PageImpl<>(list, pageable, total);
    }

    /**
     * 查询订单列表不分页
     *
     * @param param
     * @return
     */
    public List<Order> list(Map<String, Object> param) {

        Query query = new Query();
        Criteria criteria = QueryParamUtil.getCriteria(param);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Order.class, mallOrder);

    }

    /**
     * 使用聚合函数统计当月销售额
     *
     * @param param
     * @return
     */
    public List<Order> statistic(Map<String, Object> param) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("paymentStatus").is(param.get("paymentStatus")).and("createDate").gte(param.get("startDate")).lt(param.get("endDate"))),
                Aggregation.group("paymentStatus").sum("payAmount").as("payAmount"));
        AggregationResults<Order> aggregate = mongoTemplate.aggregate(aggregation, mallOrder, Order.class);
        return aggregate.getMappedResults();
    }

    /**
     * 账单统计
     *
     * @param param
     */
    public List<Order> statisticBill(Map<String, Object> param) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria
                        .where("paymentStatus").is(param.get("paymentStatus"))
                        .and("transType").is(param.get("transType"))
                        .and("payDate").gte(param.get("start")).lte(param.get("end"))
                ),
                Aggregation.group("siteCode","accountId", "payMethod", "productId")
                        .sum("payAmount").as("payAmount")
                        .sum("limPri").as("limPri")
                        .sum("orderAmount").as("orderAmount")
        );
        AggregationResults<Order> results = mongoTemplate.aggregate(aggregation, mallOrder, Order.class);
        return results.getMappedResults();
    }

    /**
     * 通过日期与accountId统计销售额
     * @param accountIds
     * @param start
     * @param end
     * @return
     */
    public List<Order> statisticByAccountId(List<String> accountIds,Date start,Date end){
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria
                        .where("paymentStatus").is(PayStatus.Paid.getCode())
                        .and("transType").is(PayConstant.TransType.CONSUMPTION.getCode())
                        .and("payDate").gte(start).lte(end)
                        .and("payMethod").ne(PayConstant.PayMethod.Voucher.getCode())
                        .and("accountId").in(accountIds)
                ),
                Aggregation.group("paymentStatus")
                        .sum("payAmount").as("payAmount")
                        .sum("limPri").as("limPri")
                        .sum("orderAmount").as("orderAmount")
        );
        AggregationResults<Order> results = mongoTemplate.aggregate(aggregation, mallOrder, Order.class);
        return results.getMappedResults();
    }


    /**
     * 通过服务号查询订单列表
     *
     * @param serviceNo
     * @return
     */
    @Deprecated
    public List<Order> getOrdersByServiceNo(String serviceNo) {
        Query query = new Query(Criteria.where("serviceNo").is(serviceNo));
        return mongoTemplate.find(query, Order.class, mallOrder);
    }

    /**
     * 根据主键查询详情
     *
     * @param id
     * @return
     */
    public Order findById(String id) {

        return mongoTemplate.findById(id, Order.class, mallOrder);

    }

    /**
     * 通过订单号查询
     *
     * @param orderNo
     * @return
     */
    public List<Order> findByOrderNo(String orderNo) {
        Query query = new Query();
        Criteria criteria = new Criteria("orderNo");
        criteria.is(orderNo);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Order.class, mallOrder);
    }

    /**
     * 新增订单 新增成功则返回对应的实体
     *
     * @param order
     */
    public Order insert(Order order) {
        return mongoTemplate.insert(order, mallOrder);
    }

    /**
     * 更新订单
     *
     * @param id
     * @param param
     * @return
     */
    public Order update(String id, Map<String, Object> param) {

        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        //获取order中bigDecimal类型的字段
        Set<String> decimalFields = new HashSet<>();
        Field[] fields = Order.class.getDeclaredFields();
        for (Field field : fields) {
            if (BigDecimal.class.getTypeName().equals(field.getType().getTypeName())) {
                decimalFields.add(field.getName());
            }
        }

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (entry.getValue() == null || StringUtils.isEmpty(entry.getValue().toString())) {
                continue;
            }
            if (StringUtils.containsIgnoreCase(entry.getKey(), "date")) {
                Object value = entry.getValue();
                if (null == value || StringUtils.isBlank(value.toString())) {
                    continue;
                }
                try {
                    Date parse = DateUtil.parse(value.toString(), DateUtil.FORMAT_1);
                    update.set(entry.getKey(), parse);
                } catch (ParseException e) {
                    throw new AppException(ExceptionType.SYS_PARAMETER, "日期格式错误", e);
                }
                continue;
            }
            if (decimalFields.contains(entry.getKey())) {
                update.set(entry.getKey(), new BigDecimal(entry.getValue().toString()));
                continue;
            }
            update.set(entry.getKey(), entry.getValue());
        }
        update.set("updateDate", new Date());
        return mongoTemplate.findAndModify(query, update, Order.class, mallOrder);
    }

    /**
     * 统计代金券支付的订单的限价
     * @param customerUserCodes
     * @param start
     * @param end
     * @return
     */
    public List<Order> statisticVoucherLimPri(List<String> customerUserCodes, Date start, Date end) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria
                        .where("paymentStatus").is(PayStatus.Paid.getCode())
                        .and("transType").is(PayConstant.TransType.CONSUMPTION.getCode())
                        .and("payDate").gte(start).lte(end)
                        .and("payMethod").is(PayConstant.PayMethod.Voucher.getCode())
                        .and("accountId").in(customerUserCodes)
                ),
                Aggregation.group("paymentStatus")
                        .sum("payAmount").as("payAmount")
                        .sum("limPri").as("limPri")
                        .sum("orderAmount").as("orderAmount")
        );
        AggregationResults<Order> results = mongoTemplate.aggregate(aggregation, mallOrder, Order.class);
        return results.getMappedResults();
    }
}
