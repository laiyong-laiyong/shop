package com.sobey.module.mallPack.service;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.mallPack.model.MallPackOrder;
import com.sobey.module.mallPack.model.MallPackResource;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.utils.DateUtil;
import com.sobey.module.utils.QueryParamUtil;
import com.sobey.util.common.page.PageModel;
import com.sobey.util.common.page.SobeyPageable;
import com.sobey.util.common.page.SortUtil;
import com.sobey.util.common.regex.PatternUtil;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author WCY
 * @createTime 2020/5/20 11:53
 * 套餐包订单相关
 */
@Service
public class MallPackOrderService {

    @Value("${mongo.collection.mall_pack_order}")
    private String mallPackOrder;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    public MallPackOrder createOrder(MallPackOrder order) {

        String id = IdWorker.get32UUID();
        String orderNo = IdWorker.getIdStr();
        Date now = new Date();
        order.setId(id);
        order.setOrderAmount(order.getOrderAmount().setScale(2, BigDecimal.ROUND_DOWN));
        order.setOrderNo(orderNo);
        order.setCreateDate(now);
        order.setPayStatus(PayStatus.ToBePaid.getCode());
        List<MallPackResource> resources = order.getResources();
        for (MallPackResource resource : resources) {
            resource.setCreateDate(now);
            resource.setRemainingSize(resource.getSize());
        }
        return mongoTemplate.insert(order, mallPackOrder);
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    public MallPackOrder findById(String id) {
        return mongoTemplate.findById(id, MallPackOrder.class, mallPackOrder);
    }

    /**
     * 通过订单号查询
     *
     * @param orderNo
     * @return
     */
    public MallPackOrder findByOrderNo(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return null;
        }
        Query query = new Query();
        Criteria criteria = new Criteria("orderNo");
        criteria.is(orderNo);
        query.addCriteria(criteria);
        List<MallPackOrder> list = mongoTemplate.find(query, MallPackOrder.class, mallPackOrder);
        if (list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new AppException(ExceptionType.SYS_RUNTIME, "查询到重复的订单号", new RuntimeException());
        }
        return list.get(0);
    }

    /**
     * 分页查询
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param param    查询参数: 除日期字段外支持其他全部参数,日期支持支付时间段的查询 - startDate(开始时间) endDate(结束时间)
     * @return
     */
    public Page<MallPackOrder> pages(int pageNum, int pageSize, Map<String, Object> param) {
        Query query = new Query();
        query.addCriteria(handleQueryParam(param));
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
        long total = mongoTemplate.count(query, MallPackOrder.class, mallPackOrder);
        //查询
        List<MallPackOrder> list = mongoTemplate.find(query.with(pageable), MallPackOrder.class, mallPackOrder);
        //封装
//        Page<MallPackOrder> pages = new PageImpl<>(list, pageable, total);

        return new PageImpl<>(list, pageable, total);
    }

    /**
     * 查询订单列表不分页
     *
     * @param param 查询参数: 除日期字段外支持其他全部参数,日期支持支付时间段的查询 - startDate(开始时间) endDate(结束时间)
     * @return
     */
    public List<MallPackOrder> list(Map<String, Object> param) {
        Query query = new Query();
        query.addCriteria(handleQueryParam(param));
        return mongoTemplate.find(query, MallPackOrder.class, mallPackOrder);
    }

    /**
     * 统计账单
     * @param param
     * @return
     */
    public List<MallPackOrder> statisticBill(Map<String, Object> param){
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria
                        .where("payStatus").is(param.get("payStatus"))
                        .and("payDate").gte(param.get("start")).lte(param.get("end"))
                ),
                Aggregation.group("siteCode","accountId", "payMethod", "productId")
                        .sum("payAmount").as("payAmount")
                        .sum("limPri").as("limPri")
                        .sum("orderAmount").as("orderAmount")
        );
        AggregationResults<MallPackOrder> results = mongoTemplate.aggregate(aggregation, mallPackOrder, MallPackOrder.class);
        return results.getMappedResults();
    }

    /**
     * 更新订单
     *
     * @param id
     * @param param
     */
    public MallPackOrder update(String id, Map<String, Object> param) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        Field[] fields = MallPackOrder.class.getDeclaredFields();
        //将日期与BigDecimal类型的字段过滤出来
        List<Field> dateFields = new ArrayList<>();
        Set<String> decimalFields = new HashSet<>();
        for (Field field : fields) {
            if (field.getType().getTypeName().equals(Date.class.getTypeName())){
                dateFields.add(field);
            }
            if (field.getType().getTypeName().equals(BigDecimal.class.getTypeName())){
                decimalFields.add(field.getName());
            }
        }
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (entry.getValue() == null || StringUtils.isEmpty(entry.getValue().toString())) {
                continue;
            }
            if (dateFields.stream().anyMatch(field -> field.getName().equals(entry.getKey()))) {
                Object value = entry.getValue();
                if (null == value || StringUtils.isBlank(value.toString())) {
                    continue;
                }
                try {
                    update.set(entry.getKey(), DateUtil.parse(value.toString(), DateUtil.FORMAT_1));
                } catch (ParseException e) {
                    throw new AppException(ExceptionType.SYS_PARAMETER, "日期格式错误", e);
                }
                continue;
            }
            if (decimalFields.contains(entry.getKey())){
                update.set(entry.getKey(),new BigDecimal(entry.getValue().toString()));
                continue;
            }
            update.set(entry.getKey(), entry.getValue());
        }
        update.set("updateDate", new Date());
        return mongoTemplate.findAndModify(query, update, MallPackOrder.class, mallPackOrder);
    }


    /**
     * 处理查询参数
     *
     * @param param
     * @return
     */
    private Criteria handleQueryParam(Map<String, Object> param) {
        if (null == param || param.size() == 0) {
            return new Criteria();
        }
        Object startDate = param.remove("startDate");
        Object endDate = param.remove("endDate");
        //套餐包名称和商品名称做模糊查询
        Object packName = param.remove("packName");
        Object productName = param.remove("productName");
        Criteria criteria = QueryParamUtil.getCriteria(param);
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("packName", packName);
            put("productName", productName);
        }};
        criteria = handleBlurryQuery(criteria, map);
        handleQueryDate(criteria, startDate, endDate);
        return criteria;
    }

    /**
     * 处理日期查询
     *
     * @param criteria
     * @param startDate
     * @param endDate
     * @return
     */
    private Criteria handleQueryDate(Criteria criteria, Object startDate, Object endDate) {
        try {
            if (!QueryParamUtil.isBlank(startDate) && !QueryParamUtil.isBlank(endDate)) {
                criteria.andOperator(Criteria.where("payDate").gte(DateUtil.parse(startDate.toString(), DateUtil.FORMAT_1)),
                        Criteria.where("payDate").lt(DateUtil.parse(endDate.toString(), DateUtil.FORMAT_1)));
                return criteria;
            }
            if (!QueryParamUtil.isBlank(startDate)) {
                criteria.andOperator(Criteria.where("payDate").gte(DateUtil.parse(startDate.toString(), DateUtil.FORMAT_1)));
                return criteria;
            }
            if (!QueryParamUtil.isBlank(endDate)) {
                criteria.andOperator(Criteria.where("payDate").lt(DateUtil.parse(endDate.toString(), DateUtil.FORMAT_1)));
                return criteria;
            }
            return criteria;
        } catch (ParseException e) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "日期格式错误", e);
        }
    }

    /**
     * 处理模糊查询的参数
     *
     * @param criteria
     * @param params
     * @return
     */
    private Criteria handleBlurryQuery(Criteria criteria, Map<String, Object> params) {

        if (null == params || params.size() == 0) {
            return criteria;
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (QueryParamUtil.isBlank(entry.getValue())) {
                continue;
            }
            criteria.and(entry.getKey()).regex(PatternUtil.obscure(entry.getValue().toString()));
        }
        return criteria;
    }

    /**
     * 通过日期与accountId统计销售额
     * @param accountIds
     * @param start
     * @param end
     * @return
     */
    public List<MallPackOrder> statisticByAccountId(List<String> accountIds, Date start, Date end) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria
                        .where("payStatus").is(PayStatus.Paid.getCode())
                        .and("payDate").gte(start).lte(end)
                        .and("payMethod").ne(PayConstant.PayMethod.Voucher.getCode())
                        .and("accountId").in(accountIds)
                ),
                Aggregation.group("payStatus")
                        .sum("payAmount").as("payAmount")
                        .sum("limPri").as("limPri")
                        .sum("orderAmount").as("orderAmount")
        );
        AggregationResults<MallPackOrder> results = mongoTemplate.aggregate(aggregation, mallPackOrder, MallPackOrder.class);
        return results.getMappedResults();
    }

    /**
     * 统计当月销售额
     * @param param
     * @return
     */
    public List<MallPackOrder> statistic(Map<String, Object> param) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("payStatus").is(param.get("payStatus")).and("createDate").gte(param.get("startDate")).lt(param.get("endDate"))),
                Aggregation.group("payStatus").sum("payAmount").as("payAmount"));
        AggregationResults<MallPackOrder> aggregate = mongoTemplate.aggregate(aggregation, mallPackOrder, MallPackOrder.class);
        return aggregate.getMappedResults();
    }

    /**
     * 统计代金券支付的订单的限价
     * @param userCodes
     * @param start
     * @param end
     * @return
     */
    public List<MallPackOrder> statisticVoucherLimPri(List<String> userCodes, Date start, Date end) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria
                        .where("payStatus").is(PayStatus.Paid.getCode())
                        .and("payDate").gte(start).lte(end)
                        .and("payMethod").is(PayConstant.PayMethod.Voucher.getCode())
                        .and("accountId").in(userCodes)
                ),
                Aggregation.group("payStatus")
                        .sum("payAmount").as("payAmount")
                        .sum("limPri").as("limPri")
                        .sum("orderAmount").as("orderAmount")
        );
        AggregationResults<MallPackOrder> results = mongoTemplate.aggregate(aggregation, mallPackOrder, MallPackOrder.class);
        return results.getMappedResults();
    }
}
