package com.sobey.module.productservice.controller;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.mallPack.enumeration.MetricType;
import com.sobey.module.mallPack.model.MallPackResource;
import com.sobey.module.mallPack.service.MallPackResourceService;
import com.sobey.module.productservice.entity.metricUsages.MetricDetail;
import com.sobey.module.productservice.entity.metricUsages.MetricUsageView;
import com.sobey.module.productservice.model.MetricUsage;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.productservice.model.UsageStatistics;
import com.sobey.module.productservice.service.MetricUsageService;
import com.sobey.module.productservice.service.ServiceInfoService;
import com.sobey.module.productservice.service.UsageStatisticsService;
import com.sobey.module.utils.DateUtil;
import com.sobey.util.business.header.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/25 15:37
 */
@RestController
@RequestMapping("/${api.v1}/metric")
@Api(description = "按需订单用量相关接口")
public class MetricUsageController {

    @Autowired
    private MetricUsageService mus;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private MallPackResourceService mprs;
    @Autowired
    private UsageStatisticsService uss;

    @GetMapping("/list")
    @ApiOperation(value = "获取按需类型订单相关的消费信息", httpMethod = "GET")
    public List<MetricUsage> list(@RequestParam String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "orderNo参数不能为空", new RuntimeException());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", orderNo);
        List<MetricUsage> usages = mus.selectByMap(map);
        if (null == usages) {
            usages = new ArrayList<>();
        }
        return usages;
    }

    /**
     * 获取某个服务的资源使用情况概览
     *
     * @param appId
     * @return
     */
    @GetMapping("/usages")
    @ApiOperation(value = "查询服务用量概览(每天用量的折线图)", httpMethod = "GET")
    public List<MetricUsageView> getUsages(@RequestParam @ApiParam(value = "appId") String appId,
                                           @RequestParam(required = false) @ApiParam(value = "近一周或近一月显示折线图,week 或 month", example = "week") String weekOrMonth,
                                           @RequestParam(required = false) @ApiParam(value = "开始时间 yyyy-MM-dd") String start,
                                           @RequestParam(required = false) @ApiParam(value = "结束时间 yyyy-MM-dd") String end) {
        if (StringUtils.isBlank(appId)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "appId参数不能为空", new RuntimeException());
        }
        if (StringUtils.isBlank(weekOrMonth) && StringUtils.isBlank(start) && StringUtils.isBlank(end)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "weekOrMonth和日期查询条件不能同时为空", new RuntimeException());
        }
        if (!StringUtils.isBlank(weekOrMonth) && !StringUtils.isBlank(start) && !StringUtils.isBlank(end)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "weekOrMonth和日期查询条件不能同时存在", new RuntimeException());
        }

        Date first = null;
        Date last = null;
        List<UsageStatistics> usages = null;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        if (StringUtils.isNotBlank(weekOrMonth)) {
            last = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
            if ("week".equalsIgnoreCase(weekOrMonth)) {
                calendar.add(Calendar.DAY_OF_MONTH, -6);
                first = calendar.getTime();
            }
            if ("month".equalsIgnoreCase(weekOrMonth)) {
                calendar.add(Calendar.DAY_OF_MONTH, -29);
                first = calendar.getTime();
            }
            if (null == first) {
                calendar.add(Calendar.DAY_OF_MONTH, -6);
                first = calendar.getTime();
            }
            usages = uss.getUsages(appId, DateUtils.formatDate(first, "yyyy-MM-dd"), DateUtils.formatDate(last, "yyyy-MM-dd"));
        } else {
            if (StringUtils.isBlank(start) || StringUtils.isBlank(end)) {
                throw new AppException(ExceptionType.SYS_PARAMETER, "日期查询条件必须同时有开始和结束时间", new RuntimeException());
            }
            //校验日期格式
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            try {
                format.parse(start);
                format.parse(end);
            } catch (ParseException e) {
                throw new AppException(ExceptionType.SYS_PARAMETER, "日期格式错误", e);
            }
            usages = uss.getUsages(appId, start, end);
        }
        String token = HeaderUtil.getAuth();
        //最外层map的key是日期，对应的value为某一天所有的计费类型的使用量,第二层map的key是metricId,对应的最内层的map中的key为type value为使用量
        List<MetricUsageView> result = new ArrayList<>();
        if (usages != null && usages.size() > 0) {
            for (UsageStatistics usage : usages) {
                String metricId = usage.getMetricId();
                String date = DateUtil.format(usage.getCreateDate(), DateUtil.FORMAT_4);
                String typeCode = usage.getTypeCode();
                String unit = MetricType.getName(typeCode);
                String type = usage.getType();
                double value = usage.getValue().doubleValue();
                if (result.size() == 0) {
                    MetricUsageView usageView = new MetricUsageView();
                    usageView.setDateTime(date);
                    MetricDetail detail = new MetricDetail();
                    detail.setPriceId(metricId);
                    detail.setType(type);
                    detail.setTypeCode(typeCode);
                    detail.setUnit(unit);
                    detail.setValue(value);
                    List<MetricDetail> details = new ArrayList<>();
                    details.add(detail);
                    usageView.setUsages(details);
                    result.add(usageView);
                    continue;
                }
                boolean flag = false;//用来标记是否存在相同日期
                for (MetricUsageView usageView : result) {
                    String dateTime = usageView.getDateTime();
                    if (dateTime.equals(date)) {
                        List<MetricDetail> details = usageView.getUsages();
                        MetricDetail detail = new MetricDetail();
                        detail.setValue(value);
                        detail.setTypeCode(typeCode);
                        detail.setType(type);
                        detail.setPriceId(metricId);
                        detail.setUnit(unit);
                        details.add(detail);
                        usageView.setUsages(details);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    MetricUsageView usageView = new MetricUsageView();
                    usageView.setDateTime(date);
                    MetricDetail detail = new MetricDetail();
                    detail.setPriceId(metricId);
                    detail.setType(type);
                    detail.setTypeCode(typeCode);
                    detail.setUnit(unit);
                    detail.setValue(value);
                    List<MetricDetail> details = new ArrayList<>();
                    details.add(detail);
                    usageView.setUsages(details);
                    result.add(usageView);
                }
            }
        }

        return result;
    }

    /**
     * 获取当月使用量
     *
     * @param appId
     * @return
     */
    @GetMapping("/currentMonthUsage")
    @ApiOperation(value = "获取当月使用量", httpMethod = "GET")
    public List<MetricDetail> getCurrentMonthUsage(@RequestParam String appId) {
        if (StringUtils.isBlank(appId)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "appId参数不能为空", new RuntimeException());
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date first = calendar.getTime();
        Date last = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);

        //统计使用量
        List<UsageStatistics> usages = uss.getCurrentMonthUsage(DateUtil.format(first, DateUtil.FORMAT_4), DateUtil.format(last, DateUtil.FORMAT_4), appId);
        if (null == usages) {
            usages = new ArrayList<>();
        }
        List<MetricDetail> usageViews = new ArrayList<>();
        if (usages.size() > 0) {
            for (UsageStatistics usage : usages) {
                MetricDetail detail = new MetricDetail();
                detail.setPriceId(usage.getMetricId());
                detail.setType(usage.getType());
                detail.setTypeCode(usage.getTypeCode());
                detail.setValue(usage.getValue().doubleValue());
                detail.setUnit(MetricType.getName(usage.getTypeCode()));
                usageViews.add(detail);
            }
        }
        return usageViews;
    }

    /**
     * 获取服务的所有使用量
     *
     * @param appId
     * @return
     */
    @GetMapping("/totalUsage")
    @ApiOperation(value = "统计服务的使用量", httpMethod = "GET")
    public List<MetricDetail> getTotalUsages(@RequestParam String appId) {
        if (StringUtils.isBlank(appId)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "appId参数不能为空", new RuntimeException());
        }
        List<UsageStatistics> usages = uss.getTotalUsages(appId);
        if (null == usages) {
            usages = new ArrayList<>();
        }
        List<MetricDetail> usageViews = new ArrayList<>();
        if (usages.size() > 0) {
            for (UsageStatistics usage : usages) {
                MetricDetail detail = new MetricDetail();
                detail.setPriceId(usage.getMetricId());
                detail.setType(usage.getType());
                detail.setTypeCode(usage.getTypeCode());
                detail.setValue(usage.getValue().doubleValue());
                detail.setUnit(MetricType.getName(usage.getTypeCode()));
                usageViews.add(detail);
            }
        }
        return usageViews;
    }

    /**
     * 获取套餐包使用量
     *
     * @param appId
     */
    @GetMapping("/getPackUsage")
    @ApiOperation(value = "统计服务的套餐总量与使用量", httpMethod = "GET", hidden = true)
    public List<MallPackResource> getPackUsage(@RequestParam String appId) {

        if (StringUtils.isBlank(appId)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "appId参数不能为空", new RuntimeException());
        }
        ServiceInfo service = sis.findByAppId(appId);
        if (null == service) {
            return new ArrayList<>();
        }
        String accountId = service.getAccountId();
        String productId = service.getProductId();
        List<MallPackResource> packResources = mprs.statisticEffective(accountId, productId);
        if (null == packResources) {
            return new ArrayList<>();
        }
        return packResources;

    }

}
