package com.sobey.module.order.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.fegin.product.request.service.DiscountService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.mallPack.enumeration.MetricType;
import com.sobey.module.mq.constant.OrderExpireConstant;
import com.sobey.module.mq.producer.OrderExpireProducer;
import com.sobey.module.order.OrderType;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.order.common.OpenType;
import com.sobey.module.order.entity.ResultInfo;
import com.sobey.module.order.model.Order;
import com.sobey.module.order.service.OrderService;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.productservice.model.MetricUsage;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.productservice.service.MetricUsageService;
import com.sobey.module.productservice.service.ServiceInfoService;
import com.sobey.module.utils.DateUtil;
import com.sobey.util.business.header.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/${api.v1}/order")
@Api(description = "消费相关接口")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService os;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private DiscountService ds;
    @Autowired
    private OrderExpireProducer oep;
    @Autowired
    private ProductService ps;
    @Autowired
    private MetricUsageService mus;


    @PostMapping
    @ApiOperation(value = "创建订单", httpMethod = "POST")
    public Order createOrder(@RequestBody Order order) {

        try {
            //校验参数
            String result = validateParams(order);
            if (!"SUCCESS".equals(result)) {
                throw new AppException(ExceptionType.ORDER_CODE_PARAM, result, new RuntimeException());
            }

            Map<String, Object> serviceMap = new HashMap<>();
            String accountId = order.getAccountId();
            String productId = order.getProductId();
            serviceMap.put("accountId", accountId);
            serviceMap.put("productId", productId);
            List<ServiceInfo> serviceInfos = sis.selectByMap(serviceMap);
            if (PayConstant.TransType.CONSUMPTION.getCode().equals(order.getTransType())) {
                //如果是新购需要判断是否已经买过相同的服务
                if (OrderType.New.getCode().equals(order.getOrderType())) {
                    if (null != serviceInfos && serviceInfos.size() > 0) {
                        for (ServiceInfo serviceInfo : serviceInfos) {
                            if (ServiceStatus.isEverBought(serviceInfo.getServiceStatus())) {
                                throw new AppException(ExceptionType.ORDER_CODE_INSERT, "已经购买过相同服务,如果服务关闭请重新开通", new RuntimeException());
                            }
                        }
                    }
                }
                //判断订单类型是不是续费，如果是续费需要去服务里查找是否有相应的服务
                if (OrderType.Renew.getCode().equals(order.getOrderType())) {
                    String serviceNo = order.getServiceNo();
                    if (StringUtils.isBlank(serviceNo)) {
                        throw new AppException(ExceptionType.ORDER_CODE_PARAM, "续费订单serviceNo参数不能为空", new RuntimeException());
                    }
                    if (null == serviceInfos || serviceInfos.size() == 0){
                        throw new AppException(ExceptionType.ORDER_CODE_PARAM, "未能查询到相关服务", new RuntimeException());
                    }
                    ServiceInfo service = null;
                    //续费订单需要判断当前续费的服务是否是正常运行的服务，如果有其他正在运行的相同服务但是当前续费的订单是已关闭的同一种服务则抛出错误提示
                    for (ServiceInfo serviceInfo : serviceInfos) {
                        if (ServiceStatus.Normal.getCode().equals(serviceInfo.getServiceStatus())) {
                            if (!serviceNo.equals(serviceInfo.getServiceNo())) {
                                throw new AppException(ExceptionType.ORDER_CODE_INSERT, "已经有相同服务,请勿重复开通", new RuntimeException());
                            }
                        }
                        if (serviceNo.equals(serviceInfo.getServiceNo())) {
                            //判断是否是开通失败
                            if (ServiceStatus.OpenFail.getCode().equals(serviceInfo.getServiceStatus())) {
                                throw new AppException(ExceptionType.ORDER_CODE_INSERT, "请重新开通该开通失败的服务后再进行续费操作", new RuntimeException());
                            }
                            if (ServiceStatus.Destroy.getCode().equals(serviceInfo.getServiceStatus())){
                                throw new AppException(ExceptionType.ORDER_CODE_INSERT,"该服务已销毁,请重新购买",new RuntimeException());
                            }
                            service = serviceInfo;
                            break;
                        }
                    }
                    if (null == service) {
                        throw new AppException(ExceptionType.SYS_PARAMETER, "未能查询到相关服务", new RuntimeException());
                    }
                }
            }
            //创建订单号等信息
            order.setId(IdWorker.get32UUID());
            order.setOrderNo(IdWorker.getIdStr());
            order.setPaymentStatus(PayStatus.ToBePaid.getCode());
            order.setCreateDate(new Date());
            //查询折扣信息
            BigDecimal discount = ds.queryDiscount(HeaderUtil.getAuth(), accountId, productId);
            if (null == discount) {
                discount = BigDecimal.valueOf(1.0);
            }
            BigDecimal discountPrice = discount.multiply(order.getOrderAmount());
            order.setDiscount(discount.abs());
            order.setDiscountPrice(discountPrice.abs());
            os.insert(order);
            oep.produce(OrderExpireConstant.PRODUCT_ORDER_NO + order.getOrderNo());
            return order;
        } catch (Exception e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, e);
        }

    }

    /**
     * 返回信息按照创建时间降序排序(分页)
     *
     * @param pageNum
     * @param pageSize
     * @param param
     * @return
     */
    @PostMapping("/pages")
    @ApiOperation(value = "消费信息分页查询", httpMethod = "POST")
    public Page<Order> pages(@RequestParam @ApiParam("页码") int pageNum,
                             @RequestParam @ApiParam("每页条数") int pageSize,
                             @RequestBody(required = false) @ApiParam(value = "查询参数,支持订单实体中包含的全部字段(日期相关参数除外),若要根据创建日期查询则需要按以下格式传入{\"startDate\":\"2020-01-01 00:00:00\",\"endDate\":\"2020-02-01 00:00:00\",...(其他条件)}") HashMap<String, Object> param) {

        return os.pages(pageNum, pageSize, param);

    }


    /**
     * 根据主键查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/findById/{id}")
    @ApiOperation(value = "根据主键查询订单详情", httpMethod = "GET")
    public Order findById(@PathVariable("id") String id) {
        Order order = os.findById(id);
        List<MetricUsage> usages = mus.getUsagesByOrderNos(Collections.singletonList(order.getOrderNo()));
        order.setMetricUsages(usages);
        return order;
    }

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    @GetMapping("/findByOrderNo/{orderNo}")
    @ApiOperation(value = "根据订单号查询订单", httpMethod = "GET")
    public Order findByOrderNo(@PathVariable("orderNo") String orderNo) {
        List<Order> orders = os.findByOrderNo(orderNo);
        if (null == orders || orders.size() == 0) {
            throw new AppException(ExceptionType.ORDER_CODE_QUERY, "未查询到订单号为" + orderNo + "的订单", new RuntimeException());
        }
        if (orders.size() > 1) {
            throw new AppException(ExceptionType.ORDER_CODE_QUERY, "存在" + orders.size() + "个相同订单号的订单,请联系管理员", new RuntimeException());
        }
        Order order = orders.get(0);
        List<MetricUsage> usages = mus.getUsagesByOrderNos(Collections.singletonList(order.getOrderNo()));
        order.setMetricUsages(usages);
        return order;
    }

    /**
     * 更新 内部系统使用不暴露给前端
     *
     * @param param
     * @return
     */
    @PutMapping(value = "/update/{id}")
    @ApiOperation(value = "更新消费信息", httpMethod = "PUT", hidden = true)
    public ResultInfo update(@PathVariable("id") String id,
                             @RequestBody @ApiParam("更新参数") Map<String, Object> param) {

        ResultInfo result = new ResultInfo();
        if (null == param || param.size() == 0) {
            result.setCode("FAIL");
            result.setMsg("更新参数为空");
            return result;
        }
        os.update(id, param);
        result.setCode("SUCCESS");
        result.setMsg("更新成功");
        return result;

    }

    /**
     * 校验订单参数 通过返回SUCCESS否侧返回错误信息
     *
     * @param order
     * @return
     */
    public String validateParams(Order order) throws Exception {

        if (null == order) {
            return "参数错误不能为空";
        }
        if (PayConstant.TransType.RECHARGE.getCode().equalsIgnoreCase(order.getTransType())) {
            String accountId = order.getAccountId();
            BigDecimal orderAmount = order.getOrderAmount();
            if (StringUtils.isBlank(accountId)) {
                return "参数accountId不能为空";
            }
            if (null == orderAmount) {
                return "参数orderAmount不能为空";
            }
            return "SUCCESS";
        }
        //校验必传参数
        Field[] fields = order.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            ApiModelProperty apiModelProperty = field.getDeclaredAnnotation(ApiModelProperty.class);
            if (null != apiModelProperty) {
                if (apiModelProperty.required()) {
                    Object o = field.get(order);
                    if (null == o || StringUtils.isBlank(o.toString())) {
                        return "参数" + field.getName() + "不能为空";
                    }
                }
            }
        }
        //判断是否是正确的订单类型
        if (!OrderType.isContained(order.getOrderType())) {
            return "未知的订单类型";
        }
        //判断开通类型是否正确
        if (!OpenType.isContained(order.getOpenType())) {
            return "未知的开通类型";
        }
        return "SUCCESS";

    }

    /**
     * 导出excel
     *
     * @param response
     */
    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出excel", httpMethod = "POST")
    public void exportExcel(HttpServletResponse response,
                            @RequestBody(required = false) @ApiParam(value = "查询参数,支持订单实体中包含的全部字段(日期相关参数除外),若要根据创建日期查询则需要按以下格式传入{\"startDate\":\"2020-01-01 00:00:00\",\"endDate\":\"2020-02-01 00:00:00\",...(其他条件)}") HashMap<String, Object> param) {
        List<Order> list = os.list(param);
        try {
            if (null != list && list.size() > 0) {
                //创建需要导出的字段
                LinkedHashMap<String, String> titles = getExportTitles();
                String filename = "OrderList-" + IdWorker.getId() + ".xls";
                response.setHeader("Content-Disposition", "attachment;filename=" + filename);
                response.setContentType("application/vnd.ms-excel;Charset=UTF-8");
                ServletOutputStream outputStream = response.getOutputStream();
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("sheet");
                int rowIndex = 0;
                int maxColumn = 0;//列数
                List<String> titleKey = new ArrayList<>();//标题对应的key，即字段名
                //创建标题行
                Field[] fields = Order.class.getDeclaredFields();
                HSSFRow headRow = sheet.createRow(0);
                for (Map.Entry<String, String> entry : titles.entrySet()) {
                    sheet.setColumnWidth(maxColumn, 20 * 256);
                    HSSFCell cell = headRow.createCell(maxColumn, CellType.STRING);
                    cell.setCellValue(entry.getValue());
                    titleKey.add(entry.getKey());
                    maxColumn++;
                }
                //最后一列显示计费项
                sheet.setColumnWidth(maxColumn, 20 * 256 * 4);
                HSSFCell chargeCell = headRow.createCell(maxColumn, CellType.STRING);
                chargeCell.setCellValue("计费项");
                maxColumn++;
                //由于查询商品名称涉及到网络请求，这里先对productId去重单独查出商品名
                ConcurrentHashMap<String, String> productMap = new ConcurrentHashMap<>();
                for (Order order : list) {
                    productMap.put(order.getProductId(), "");
                }
                for (Map.Entry<String, String> entry : productMap.entrySet()) {
                    JSONArray jsonArray = ps.queryProduct(HeaderUtil.getAuth(), entry.getKey());
                    if (null != jsonArray && jsonArray.size() > 0) {
                        JSONObject product = jsonArray.getJSONObject(0);
                        productMap.put(entry.getKey(), product.getString("name"));
                    }
                }
                //查询出所有按需订单对应的计费信息
                List<String> orderNos = list.stream()
                        .filter(order -> OpenType.Demand.getCode().equals(order.getOpenType()))
                        .map(Order::getOrderNo).collect(Collectors.toList());
                List<MetricUsage> usages = null;
                if (orderNos.size() > 0){
                    usages = mus.getUsagesByOrderNos(orderNos);
                }
                for (Order order : list) {
                    rowIndex++;
                    HSSFRow row = sheet.createRow(rowIndex);
                    //设置每列的值
                    for (int i = 0; i < maxColumn; i++) {
                        HSSFCell cell = row.createCell(i, CellType.STRING);
                        //首先判断最后一列，填写计费项
                        if (i == (maxColumn - 1)) {
                            cell.setCellValue("");
                            //如果不是按需类型的订单可直接跳过
                            if (!OpenType.Demand.getCode().equals(order.getOpenType())){
                                break;
                            }
                            if (null != usages && usages.size() > 0) {
                                StringBuilder builder = new StringBuilder();
                                List<MetricUsage> usageList = usages.stream()
                                        .filter(metricUsage -> order.getOrderNo().equals(metricUsage.getOrderNo()))
                                        .collect(Collectors.toList());
                                if (usageList.size() > 0){
                                    for (MetricUsage usage : usageList) {
                                        builder
                                                .append("单价ID:").append(usage.getPriceId()).append("/")
                                                .append("类型:").append(usage.getType()).append("/")
                                                .append("单位:").append(MetricType.getName(usage.getTypeCode())).append("/")
                                                .append("用量:").append(usage.getValue().toString()).append(" ");
                                    }
                                    String val = builder.toString();
                                    cell.setCellValue(val.substring(0,val.length()-1));
                                }
                            }
                            break;
                        }
                        String key = titleKey.get(i);
                        if ("productName".equals(key)) {
                            cell.setCellValue(productMap.get(order.getProductId()));
                            continue;
                        }
                        if ("payMethod".equals(key)) {
                            cell.setCellValue(PayConstant.PayMethod.getDesc(order.getPayMethod()));
                            continue;
                        }
                        if ("orderType".equals(key)) {
                            cell.setCellValue(OrderType.getDesc(order.getOrderType()));
                            continue;
                        }
                        if ("paymentStatus".equals(key)) {
                            cell.setCellValue(PayStatus.getDesc(order.getPaymentStatus()));
                            continue;
                        }
                        for (Field field : fields) {
                            field.setAccessible(true);
                            if (field.getName().equals(key)) {
                                Object val = field.get(order);
                                if (null == val || StringUtils.isBlank(val.toString())) {
                                    cell.setCellValue("");
                                    break;
                                }
                                if (val instanceof Date) {
                                    cell.setCellValue(DateUtil.format((Date) val, DateUtil.FORMAT_1));
                                    break;
                                }
                                cell.setCellValue(val.toString());
                                break;
                            }
                        }
                    }
                }
                workbook.write(outputStream);
                workbook.close();
            }
        } catch (Exception e) {
            log.info("导出订单excel异常:", e);
        }
    }

    public LinkedHashMap<String, String> getExportTitles() {
        LinkedHashMap<String, String> titles = new LinkedHashMap<>();
        titles.put("orderNo", "订单号");
        titles.put("productName", "产品名称");
        titles.put("createDate", "创建时间");
        titles.put("payDate", "交易时间");
        titles.put("discount", "折扣");
        titles.put("limPri", "限价金额");
        titles.put("orderAmount", "订单金额");
        titles.put("payAmount", "交易金额");
        titles.put("payMethod", "交易方式");
        titles.put("orderType", "订单类型");
        titles.put("paymentStatus", "支付状态");
        titles.put("accountId", "用户ID");
        titles.put("account", "用户名");
        return titles;
    }

}
