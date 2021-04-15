package com.sobey.module.mallPack.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.fegin.product.request.service.DiscountService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.mallPack.enumeration.IsEffective;
import com.sobey.module.mallPack.model.*;
import com.sobey.module.mallPack.service.MallPackOrderService;
import com.sobey.module.mallPack.service.MallPackResourceService;
import com.sobey.module.mallPack.service.MallPackService;
import com.sobey.module.mallPack.service.MallPackUseRecordService;
import com.sobey.module.mq.constant.OrderExpireConstant;
import com.sobey.module.mq.producer.OrderExpireProducer;
import com.sobey.module.order.OrderType;
import com.sobey.module.order.entity.ResultInfo;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.utils.DateUtil;
import com.sobey.module.utils.ValidateUtil;
import com.sobey.util.business.header.HeaderUtil;
import io.swagger.annotations.Api;
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
import org.springframework.beans.BeanUtils;
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
 * @author WCY
 * @createTime 2020/5/20 11:51
 * 套餐包相关接口
 */
@Api(description = "套餐包相关接口")
@RestController
@RequestMapping("/${api.v1}/mallPack")
public class MallPackController {

    private static final Logger log = LoggerFactory.getLogger(MallPackController.class);

    @Autowired
    private MallPackOrderService mpos;
    @Autowired
    private MallPackService mps;
    @Autowired
    private MallPackResourceService mprs;
    @Autowired
    private MallPackUseRecordService mpurs;
    @Autowired
    private DiscountService ds;
    @Autowired
    private OrderExpireProducer oep;
    @Autowired
    private ProductService ps;

    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    @PostMapping("/order")
    @ApiOperation(value = "创建订单", httpMethod = "POST")
    public ResultInfo createOrder(@RequestBody MallPackOrder order) {
        ResultInfo rt = null;
        try {
            rt = ValidateUtil.validateBeanParam(order);
            if ("FAIL".equalsIgnoreCase(rt.getCode())) {
                return rt;
            }
            List<MallPackResource> resources = order.getResources();
            if (null == resources || resources.size() == 0) {
                return ResultInfo.withFail("资源信息为空");
            }
            for (MallPackResource resource : resources) {
                rt = ValidateUtil.validateBeanParam(resource);
                if ("FAIL".equalsIgnoreCase(rt.getCode())) {
                    return rt;
                }
            }
            //查询折扣
            String accountId = order.getAccountId();
            String productId = order.getProductId();
            BigDecimal discount = ds.queryDiscount(HeaderUtil.getAuth(), accountId, productId);
            if (null == discount){
                discount = BigDecimal.valueOf(1.0);
            }
            BigDecimal discountPrice = discount.multiply(order.getOrderAmount());
            order.setDiscount(discount.abs());
            order.setDiscountPrice(discountPrice.abs());
            MallPackOrder packOrder = mpos.createOrder(order);
            oep.produce(OrderExpireConstant.PACK_ORDER_NO+order.getOrderNo());
            rt = ResultInfo.withSuccess("创建成功");
            rt.setBody(packOrder);
        } catch (Exception e) {
            log.error("订单创建异常", e);
            rt = ResultInfo.withFail("订单创建异常");
        }
        return rt;
    }

    /**
     * 订单分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param param
     * @return
     */
    @PostMapping("/order/pages")
    @ApiOperation(value = "订单分页查询")
    public Page<MallPackOrder> orderPages(@RequestParam @ApiParam(value = "页码") int pageNum,
                                          @RequestParam @ApiParam(value = "每页条数") int pageSize,
                                          @RequestBody(required = false) @ApiParam(value = "查询参数,以支付时间为参数时请传 startDate(开始时间) endDate(结束时间)") Map<String, Object> param) {
        return mpos.pages(pageNum, pageSize, param);
    }

    /**
     * 订单查询不分页
     *
     * @param param
     * @return
     */
    @PostMapping("/order/list")
    @ApiOperation(value = "订单列表查询")
    public List<MallPackOrder> orderList(@RequestBody(required = false) @ApiParam(value = "查询参数,以支付时间为参数时请传 startDate(开始时间) endDate(结束时间)") Map<String, Object> param) {
        return mpos.list(param);
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/order/id/{id}")
    @ApiOperation(value = "根据主键查询,rest风格", httpMethod = "GET")
    public MallPackOrder findById(@PathVariable("id") String id) {
        return mpos.findById(id);
    }

    /**
     * 根据订单号查询
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/order/orderNo/{orderNo}")
    @ApiOperation(value = "根据订单号查询,rest风格", httpMethod = "GET")
    public MallPackOrder findByOrderNo(@PathVariable("orderNo") String orderNo) {
        return mpos.findByOrderNo(orderNo);
    }

    /**
     * 更新订单
     *
     * @param id
     * @param param
     * @return
     */
    @PutMapping("/order")
    @ApiOperation(value = "更新订单", httpMethod = "PUT")
    public ResultInfo update(@RequestParam String id,
                             @RequestBody Map<String, Object> param) {
        if (param.size() == 0) {
            return ResultInfo.withFail("更新参数为空");
        }
        MallPackOrder oldVal = mpos.update(id, param);
        ResultInfo success = ResultInfo.withSuccess("");
        success.setBody(oldVal);
        return success;
    }

    /**
     * 创建套餐包
     *
     * @param mallPack
     * @return
     */
    @PostMapping("/pack")
    @ApiOperation(value = "创建套餐包", httpMethod = "POST")
    public ResultInfo createMallPack(@RequestBody MallPack mallPack) {
        mps.createMallPack(mallPack);
        return ResultInfo.withSuccess("");
    }

    /**
     * 套餐包查询
     *
     * @param pageNum
     * @param pageSize
     * @param mallPack
     * @param resourceName
     * @return
     */
    @PostMapping("/pack/pages")
    @ApiOperation(value = "套餐包分页查询", httpMethod = "POST")
    public com.baomidou.mybatisplus.plugins.Page<MallPackVo> mallPackPages(@RequestParam @ApiParam("页码") int pageNum,
                                                                           @RequestParam @ApiParam("每页条数") int pageSize,
                                                                           @RequestBody(required = false) MallPack mallPack,
                                                                           @RequestParam(required = false) @ApiParam("通过资源名称查询时传此参数") String resourceName) {
        if (pageNum < 0) {
            pageNum = 1;
        }
        if (pageSize < 0) {
            pageSize = 10;
        }
        com.baomidou.mybatisplus.plugins.Page<MallPackVo> pages = new com.baomidou.mybatisplus.plugins.Page<>(pageNum, pageSize);
        List<MallPack> list = mps.list(mallPack);
        if (null == list || list.size() == 0) {
            return pages;
        }
        List<String> mallPackIds = list.stream().map(MallPack::getUuid).collect(Collectors.toList());
        int count = mprs.count(resourceName, mallPackIds);
        List<MallPackResource> resources = mprs.manualPages(pageNum, pageSize, resourceName, mallPackIds);
        pages.setTotal(count);
        pages.setCurrent(pageNum);
        List<MallPackVo> packVos = new ArrayList<>();

        for (MallPack pack : list) {
            for (MallPackResource resource : resources) {
                if (pack.getUuid().equals(resource.getMallPackId())) {
                    double remain = resource.getRemainingSize().doubleValue();
                    MallPackVo packVo = new MallPackVo();
                    BeanUtils.copyProperties(pack, packVo, "uuid", "duration", "unit", "createDate");
                    BeanUtils.copyProperties(resource, packVo, "createDate", "updateDate");
                    if (0 == remain){
                        //设置状态为已用完
                        packVo.setIsEffective(IsEffective.UseUp.getCode());
                    }
                    packVos.add(packVo);
                }
            }
        }
        pages.setRecords(packVos);
        return pages;
    }

    /**
     * 使用明细分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param param
     * @return
     */
    @PostMapping("/pack/useRecordPages")
    @ApiOperation(value = "使用明细分页查询", httpMethod = "POST")
    public Page<MallPackUseRecord> useRecordPages(@RequestParam @ApiParam(value = "页码,大于1的整数") int pageNum,
                                                  @RequestParam @ApiParam(value = "每页条数,大于0的整数") int pageSize,
                                                  @RequestBody(required = false) @ApiParam(value = "查询参数,以抵扣日期为参数查询时:consumptionDate-抵扣日期(yyyy-MM-dd)") Map<String, Object> param) {
        return mpurs.pages(pageNum, pageSize, param);
    }

    /**
     * 新增使用明细
     *
     * @param mallPackUseRecord
     * @return
     */
    @PostMapping("/pack/useRecord")
    @ApiOperation(value = "新增使用明细", httpMethod = "POST")
    public ResultInfo insertUseRecord(@RequestBody MallPackUseRecord mallPackUseRecord) {
        mpurs.insert(mallPackUseRecord);
        return ResultInfo.withSuccess("");
    }

    @GetMapping("/pack/packStatus")
    @ApiOperation(value = "获取套餐包状态码",httpMethod = "GET")
    @PassToken
    public Map<String,String> getEffectiveStatus(){
        Map<String,String> map = new HashMap<>();
        for (IsEffective value : IsEffective.values()) {
            map.put(value.getCode(),value.getDesc());
        }
        return map;
    }

    /**
     * 导出excel
     * @param response
     */
    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出excel",httpMethod = "POST")
    public void exportExcel(HttpServletResponse response,
                            @RequestBody(required = false) @ApiParam(value = "查询参数,支持订单实体中包含的全部字段(日期相关参数除外),若要根据创建日期查询则需要按以下格式传入{\"startDate\":\"2020-01-01 00:00:00\",\"endDate\":\"2020-02-01 00:00:00\",...(其他条件)}") HashMap<String, Object> param){
        List<MallPackOrder> list = mpos.list(param);
        try {
            if (null != list && list.size() > 0){
                //创建需要导出的字段
                LinkedHashMap<String, String> titles = getExportTitles();
                String filename = "PackOrderList-" + IdWorker.getId() + ".xls";
                response.setHeader("Content-Disposition", "attachment;filename=" + filename);
                response.setContentType("application/vnd.ms-excel;Charset=UTF-8");
                ServletOutputStream outputStream = response.getOutputStream();
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("sheet");
                int rowIndex = 0;
                int maxColumn = 0;//列数
                List<String> titleKey = new ArrayList<>();//标题对应的key，即字段名
                //创建标题行
                Field[] fields = MallPackOrder.class.getDeclaredFields();
                HSSFRow headRow = sheet.createRow(0);
                for (Map.Entry<String, String> entry : titles.entrySet()) {
                    sheet.setColumnWidth(maxColumn,20*256);
                    HSSFCell cell = headRow.createCell(maxColumn, CellType.STRING);
                    cell.setCellValue(entry.getValue());
                    titleKey.add(entry.getKey());
                    maxColumn++;
                }
                //由于查询商品名称涉及到网络请求，这里先对productId去重单独查出商品名
                ConcurrentHashMap<String, String> productMap = new ConcurrentHashMap<>();
                for (MallPackOrder packOrder : list) {
                    productMap.put(packOrder.getProductId(), "");
                }
                for (Map.Entry<String, String> entry : productMap.entrySet()) {
                    JSONArray jsonArray = ps.queryProduct(HeaderUtil.getAuth(), entry.getKey());
                    if (null != jsonArray && jsonArray.size() > 0) {
                        JSONObject product = jsonArray.getJSONObject(0);
                        productMap.put(entry.getKey(), product.getString("name"));
                    }
                }
                for (MallPackOrder packOrder : list) {
                    rowIndex++;
                    HSSFRow row = sheet.createRow(rowIndex);
                    //设置每列的值
                    for (int i = 0; i < maxColumn; i++) {
                        HSSFCell cell = row.createCell(i, CellType.STRING);
                        String key = titleKey.get(i);
                        if ("productName".equals(key)){
                            cell.setCellValue(productMap.get(packOrder.getProductId()));
                            continue;
                        }
                        if ("payMethod".equals(key)){
                            cell.setCellValue(PayConstant.PayMethod.getDesc(packOrder.getPayMethod()));
                            continue;
                        }
                        if ("orderType".equals(key)){
                            cell.setCellValue(OrderType.getDesc(packOrder.getOrderType()));
                            continue;
                        }
                        if ("payStatus".equals(key)){
                            cell.setCellValue(PayStatus.getDesc(packOrder.getPayStatus()));
                            continue;
                        }
                        for (Field field : fields) {
                            field.setAccessible(true);
                            if (field.getName().equals(key)){
                                Object val = field.get(packOrder);
                                if (null == val || StringUtils.isBlank(val.toString())){
                                    cell.setCellValue("");
                                    break;
                                }
                                if (val instanceof Date){
                                    cell.setCellValue(DateUtil.format((Date)val,DateUtil.FORMAT_1));
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
            log.info("导出订单excel异常:",e);
        }
    }

    public LinkedHashMap<String,String> getExportTitles(){
        LinkedHashMap<String,String> titles = new LinkedHashMap<>();
        titles.put("orderNo","订单号");
        titles.put("productName","产品");
        titles.put("packName","套餐包名称");
        titles.put("payDate","交易时间");
        titles.put("discount","折扣");
        titles.put("limPri","限价金额");
        titles.put("payAmount","交易金额");
        titles.put("payMethod","交易方式");
        titles.put("orderType","订单类型");
        titles.put("payStatus","支付状态");
        titles.put("accountId","用户ID");
        titles.put("account","用户名");
        return titles;
    }

}
