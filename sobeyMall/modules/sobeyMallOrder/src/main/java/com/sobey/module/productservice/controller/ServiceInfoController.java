package com.sobey.module.productservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionKit;
import com.sobey.exception.ExceptionType;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.order.common.OpenType;
import com.sobey.module.order.entity.Result;
import com.sobey.module.order.entity.ResultInfo;
import com.sobey.module.productservice.destroy.DestroyServicePublisher;
import com.sobey.module.productservice.entity.charge.*;
import com.sobey.module.productservice.entity.notice.NoticeOpen;
import com.sobey.module.fegin.balance.request.BalanceService;
import com.sobey.module.fegin.balance.response.Balance;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.fegin.product.response.entity.Metric;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.mq.producer.ChargeProducer;
import com.sobey.module.productservice.service.ServiceInfoService;
import com.sobey.module.utils.DateUtil;
import com.sobey.module.utils.MsgUtil;
import com.sobey.module.utils.RedisLock;
import com.sobey.module.utils.ReqUtil;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.appid.AppIdUtil;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description 服务相关操作
 * @Author WuChenYang
 * @CreateTime 2020/3/13 16:14
 */
@RestController
@RequestMapping("/${api.v1}/service")
@Api(description = "服务相关接口")
public class ServiceInfoController {
    private static final Logger log = LoggerFactory.getLogger(ServiceInfoController.class);

    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private ChargeProducer producer;
    @Autowired
    private BalanceService bs;
    @Autowired
    private AppIdUtil appIdUtil;
    @Autowired
    private MsgUtil msgUtil;
    @Autowired
    private ProductService ps;
    @Autowired
    private DestroyServicePublisher dsp;
    @Autowired
    private RedisLock redisLock;

    /**
     * 按量计费时会先调用此接口开通服务
     *
     * @param serviceInfo
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增服务", httpMethod = "POST")
    public ResultInfo creatService(@RequestBody ServiceInfo serviceInfo) {
        ResultInfo result = new ResultInfo();
        String token = HeaderUtil.getAuth();
        if (null == serviceInfo) {
            result.setCode("FAIL");
            result.setMsg("参数错误");
            return result;
        }
        String lockKey = "";
        try {
            //判断必传参数是否为空
            Field[] fields = serviceInfo.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                field.setAccessible(true);
                ApiModelProperty apiModelProperty = field.getDeclaredAnnotation(ApiModelProperty.class);
                if (null != apiModelProperty) {
                    if (apiModelProperty.required()) {
                        Object o = field.get(serviceInfo);
                        if (null == o || StringUtils.isBlank(o.toString())) {
                            result.setCode("FAIL");
                            result.setMsg("参数" + field.getName() + "不能为空");
                            return result;
                        }
                    }
                }
            }
            //判断是否是正确的开通类型
            if (!OpenType.isContained(serviceInfo.getOpenType())) {
                result.setCode("FAIL");
                result.setMsg("未知的开通类型:" + serviceInfo.getOpenType());
                return result;
            }
            lockKey = serviceInfo.getAccountId() + "_" + serviceInfo.getProductId();
            if (!redisLock.lock(lockKey, "openService", 30000)) {
                result.setCode("FAIL");
                result.setMsg("请勿重复操作");
                return result;
            }
            //判断是否已经购买过相同服务
            List<ServiceInfo> serviceInfos = isOpened(serviceInfo.getAccountId(), serviceInfo.getProductId());
            if (serviceInfos.size() > 0) {
                result.setCode("FAIL");
                result.setMsg("已购买过相同服务");
                return result;
            }
            //开通按需计费的商品时判断是否已经是欠费状态,如果已经欠费则不许开通
            if (OpenType.Demand.getCode().equalsIgnoreCase(serviceInfo.getOpenType())) {
                Balance balance = bs.query(HeaderUtil.getAuth(), serviceInfo.getAccountId());
                if (null == balance) {
                    result.setCode("FAIL");
                    result.setMsg("未查询到账户" + serviceInfo.getAccountId() + "的余额信息");
                    return result;
                }
                /**
                 * C这里原来的写法：balance.getBalance().add(balance.getCredits()).compareTo(BigDecimal.ZERO)
                 * C其中balance.getCredits()有可能是null，导致报错。
                 * 
                 */
                
                BigDecimal credits = balance.getCredits();
                BigDecimal balanceVal = balance.getBalance();
                if (NumberUtil.add(balanceVal,credits).compareTo(BigDecimal.ZERO) < 0) {
                	result.setCode("FAIL");
                	result.setMsg("该账户已欠费,请充值后购买");
                	return result;
                }
               
                //判断是否已经有关闭的服务，如果有的话重新开通这个服务
                Map<String, Object> map = new HashMap<>();
                map.put("productId", serviceInfo.getProductId());
                map.put("accountId", serviceInfo.getAccountId());
                map.put("serviceStatus", ServiceStatus.Closed.getCode());
                List<ServiceInfo> list = sis.selectByMap(map);
                if (null != list && list.size() == 1) {
                    ServiceInfo service = list.get(0);
                    try {
                        //开通商品,默认使用保存的开通接口,如果查询到商品则使用查询到的开通接口
                        String openUrl = service.getOpenUrl();
                        OpenParam body = new OpenParam();
                        List<Metric> metrics = ps.queryPrice(token, null, service.getProductId());
                        Charge[] charg = new Charge[metrics.size()];
                        for (int i = 0; i < metrics.size(); i++) {
                            Metric metric = metrics.get(i);
                            Charge charge = new Charge();
                            charge.setId(metric.getUuid());
                            charge.setTypeCode(metric.getType());
                            charge.setType(metric.getName());
                            charg[i] = charge;
                        }
                        service.setChargeCodes(JSON.toJSONString(charg));
                        body.setCharg(charg);
                        body.setOpenType(service.getOpenType());
                        //如果查不到商品code则默认传id
                        body.setProductCode(service.getProductId());
                        JSONArray jsonArray = ps.queryProduct(token, service.getProductId());
                        if (null != jsonArray && jsonArray.size() > 0) {
                            //获取商品code
                            Object o = jsonArray.get(0);
                            String str = JSON.toJSONString(o);
                            JSONObject object = JSON.parseObject(str, JSONObject.class);
                            String code = object.get("code").toString();
                            openUrl = object.get("openInterface").toString();//获取开通接口
                            body.setProductCode(code);
                        }
                        body.setUserCode(service.getAccountId());
                        body.setAppId(service.getAppId());
                        body.setVersionCode(service.getVersionId());
                        Result rt = ReqUtil.operaProd(token, body, openUrl);
                        if (null == rt) {
                            service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                            result.setCode("FAIL");
                            result.setMsg("商品开通失败");
                            service.setFailReason("开通失败--调用开通接口返回值为null");
                        } else {
                            //检查是否开通成功
                            if ("success".equalsIgnoreCase(rt.getState())) {
                                //删除服务销毁倒计时
                                service.setDestroyDate(null);
                                service.setServiceStatus(ServiceStatus.Normal.getCode());
                                result.setCode("SUCCESS");
                                result.setMsg("商品开通成功");
                            } else if ("failed".equalsIgnoreCase(rt.getState())) {
                                service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                                result.setCode("FAIL");
                                result.setMsg("商品开通失败");
                                service.setFailReason("开通接口返回失败:" + JSON.toJSONString(rt, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));
                            } else if ("opening".equalsIgnoreCase(rt.getState())) {
                                service.setServiceStatus(ServiceStatus.Opening.getCode());
                                result.setCode("SUCCESS");
                                result.setMsg("商品正在开通中");
                            } else {
                                //无法判断的格式一律是开通失败
                                service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                                result.setCode("FAIL");
                                result.setMsg("商品开通失败");
                                service.setFailReason("开通失败--对方反返回数据格式错误:" + JSON.toJSONString(rt, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));
                            }
                        }
                    } catch (Exception e) {
                        log.error("商品开通异常", e);
                        result.setCode("FAIL");
                        result.setMsg("商品开通失败");
                        //调用开通商品失败后更新状态为开通失败
                        service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                        service.setFailReason("商品开通代码抛出异常:" + e.getMessage());
                        sis.updateById(service);
                        return result;
                    }
                    if (ServiceStatus.Normal.getCode().equals(service.getServiceStatus())) {
                        msgUtil.sendMsg(token, MsgSubType.ServiceOpenNotice.getCode(), service.getSiteCode(), service.getAccountId(), service.getProductId(), service.getServiceNo(), "", "", "");
                    }
                    sis.updateById(service);
                    return result;
                }
            }
            //生成服务编号
            String serviceNo = IdWorker.getIdStr();
            //创建时间
            Date createDate = new Date();
            serviceInfo.setServiceNo(serviceNo);
            serviceInfo.setUuid(IdWorker.get32UUID());
            serviceInfo.setCreateDate(createDate);

            //生成appId
            String appId = appIdUtil.getAppIdMD5(serviceInfo.getAccountId(), serviceInfo.getProductId(), serviceInfo.getVersionId());
            //设置商品状态
            serviceInfo.setServiceStatus(ServiceStatus.Normal.getCode());
            String state = "success";
            //开通商品
            String openUrl = serviceInfo.getOpenUrl();
            OpenParam body = new OpenParam();
            if (OpenType.Cycle.getCode().equals(serviceInfo.getOpenType())) {
                Date expireDate = serviceInfo.getExpireDate();
                body.setExpirationDate(DateUtil.format(expireDate, DateUtil.FORMAT_1));
            }
            if (OpenType.Demand.getCode().equals(serviceInfo.getOpenType())) {
                Charge[] charg = JSON.parseObject(serviceInfo.getChargeCodes(), Charge[].class);
                body.setCharg(charg);
            }
            body.setOpenType(serviceInfo.getOpenType());
            //如果查不到商品code则默认传id
            body.setProductCode(serviceInfo.getProductId());
            JSONArray jsonArray = ps.queryProduct(token, serviceInfo.getProductId());
            if (null != jsonArray && jsonArray.size() == 1) {
                //获取商品code
                Object o = jsonArray.get(0);
                String str = JSON.toJSONString(o);
                JSONObject object = JSON.parseObject(str, JSONObject.class);
                String code = object.get("code").toString();
                body.setProductCode(code);
            }
            body.setUserCode(serviceInfo.getAccountId());
            body.setAppId(appId);
            body.setVersionCode(serviceInfo.getVersionId());
            serviceInfo.setAppId(appId);
            try {
                Result rt = ReqUtil.operaProd(token, body, openUrl);
                if (null == rt) {
                    state = "fail";
                    result.setCode("FAIL");
                    result.setMsg("商品开通失败");
                    serviceInfo.setFailReason("开通失败--调用开通接口返回值为null");
                } else {
                    //检查是否开通成功
                    if ("success".equalsIgnoreCase(rt.getState())) {
                        result.setCode("SUCCESS");
                        result.setMsg("商品开通成功");
                        serviceInfo.setMessage(rt.getMessage());
                    } else if ("failed".equalsIgnoreCase(rt.getState())) {
                        result.setCode("FAIL");
                        result.setMsg("商品开通失败");
                        serviceInfo.setServiceStatus(ServiceStatus.OpenFail.getCode());
                        serviceInfo.setFailReason("调用开通接口返回失败:" + JSON.toJSONString(rt, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));
                        state = "fail";
                    } else if ("opening".equalsIgnoreCase(rt.getState())) {
                        result.setCode("SUCCESS");
                        result.setMsg("商品正在开通中");
                        serviceInfo.setServiceStatus(ServiceStatus.Opening.getCode());
                        state = "opening";
                    } else {
                        result.setCode("FAIL");
                        result.setMsg("商品开通失败");
                        serviceInfo.setServiceStatus(ServiceStatus.OpenFail.getCode());
                        serviceInfo.setFailReason("开通失败--对反返回数据格式错误:" + JSON.toJSONString(rt, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));
                        //无法判断的格式一律是开通失败
                        state = "fail";
                    }
                }
            } catch (Exception e) {
                log.info("商品开通异常", e);
                result.setCode("FAIL");
                result.setMsg("商品开通失败");
                //TODO 调用开通商品失败后创建一个开通失败的商品
                serviceInfo.setServiceStatus(ServiceStatus.OpenFail.getCode());
                serviceInfo.setFailReason("商品开通代码抛出异常:" + ExceptionKit.toString(e));
                sis.insert(serviceInfo);
                return result;
            }
            if ("success".equals(state)) {
                //发送开通成功的消息
                msgUtil.sendMsg(token, MsgSubType.ServiceOpenNotice.getCode(), serviceInfo.getSiteCode(), serviceInfo.getAccountId(), serviceInfo.getProductId(), serviceInfo.getServiceNo(), "", "", "");
            }
            //保存信息
            sis.insert(serviceInfo);
        } catch (Exception e) {
            log.info("系统异常:", e);
            result.setCode("FAIL");
            result.setMsg("系统异常");
        }
        return result;
    }

    /**
     * 开通商品回调接口
     *
     * @param param
     * @return
     */
    @PostMapping("/notice")
    @ApiOperation(value = "卖家通知服务是否开通成功", httpMethod = "POST")
    public Result noticeOpen(@RequestBody NoticeOpen param) {
        Result result = new Result();
        if (StringUtils.isBlank(param.getAppId()) || null == param.getIsOpen()) {
            result.setState("failed");
            result.setError_msg("参数错误,请检查appId与isOpen是否有值");
            return result;
        }
        try {
            log.info("收到服务异步开通通知:{}", JSON.toJSONString(param, SerializerFeature.WriteNullStringAsEmpty));
            String appId = param.getAppId();
            boolean isOpen = param.getIsOpen();
            String message = param.getMessage();
            ServiceInfo serviceInfo = sis.findByAppId(appId);
            if (null == serviceInfo) {
                result.setState("failed");
                result.setError_msg("未能查找到对应服务,请检appId是否正确");
                return result;
            }
            if (ServiceStatus.Opening.getCode().equalsIgnoreCase(serviceInfo.getServiceStatus())) {
                serviceInfo.setServiceStatus(ServiceStatus.Normal.getCode());
                if (!isOpen) {
                    serviceInfo.setServiceStatus(ServiceStatus.OpenFail.getCode());
                    serviceInfo.setFailReason("异步通知开通失败," + message);
                }
                //开通成功推送消息
                if (isOpen) {
                    serviceInfo.setDestroyDate(null);
                    msgUtil.sendMsg(HeaderUtil.getAuth(), MsgSubType.ServiceOpenNotice.getCode(), serviceInfo.getSiteCode(), serviceInfo.getAccountId(), serviceInfo.getProductId(), serviceInfo.getServiceNo(), "", "", "");
                }
                serviceInfo.setUpdateDate(new Date());
                serviceInfo.setMessage(message);
                sis.updateById(serviceInfo);
                result.setState("success");
                return result;
            }

        } catch (Exception e) {
            log.error("系统异常", e);
            result.setState("failed");
            result.setError_msg("系统异常:" + e.getMessage());
        }
        return result;
    }

    /**
     * 续费商品
     *
     * @param serviceInfo
     * @return
     */
    @PutMapping("/renew")
    @ApiOperation(value = "续费商品", httpMethod = "PUT")
    public ResultInfo renew(@RequestBody ServiceInfo serviceInfo) {
        ResultInfo result = new ResultInfo();
        RenewParam param = new RenewParam();
        String token = HeaderUtil.getAuth();
        param.setAppId(serviceInfo.getAppId());
        param.setOpenType(serviceInfo.getOpenType());
        String renewUrl = serviceInfo.getRenewUrl();
        //查不到商品的code则传id
        Result rt = null;
        try {
            param.setProductCode(serviceInfo.getProductId());
            JSONArray jsonArray = ps.queryProduct(token, serviceInfo.getProductId());
            if (null != jsonArray && jsonArray.size() > 0) {
                //获取商品code
                JSONObject product = jsonArray.getJSONObject(0);
                String code = product.getString("code");
                renewUrl = product.getString("renewInterface");
                param.setProductCode(code);
            }
            param.setUserCode(serviceInfo.getAccountId());
            param.setVersionCode(serviceInfo.getVersionId());
            rt = ReqUtil.operaProd(token, param, renewUrl);
            ServiceInfo service = new ServiceInfo();
            service.setUuid(serviceInfo.getUuid());
            if (null == rt) {
                service.setFailReason("续费失败,续费接口返回null");
                update(service);
                result.setCode("FAIL");
                result.setCode("续费失败");
                return result;
            }
            if ("success".equalsIgnoreCase(rt.getState())) {
                //清空销毁倒计时
                serviceInfo.setDestroyDate(null);
                serviceInfo.setServiceStatus(ServiceStatus.Normal.getCode());
                update(serviceInfo);
                result.setCode("SUCCESS");
                result.setMsg("续费成功");
                return result;
            }
            service.setFailReason("续费失败:" + JSON.toJSONString(rt));
            result.setCode("FAIL");
            result.setCode("续费失败");
            update(service);
        } catch (Exception e) {
            log.info("续费失败:", e);
            ServiceInfo service = new ServiceInfo();
            service.setUuid(serviceInfo.getUuid());
            service.setFailReason("续费失败:" + e.getMessage());
            result.setCode("FAIL");
            result.setCode("续费失败");
            update(service);
        }
        return result;
    }

    /**
     * 查询是否有已经购买过服务
     *
     * @param accountId
     * @param productId
     * @return
     */
    @GetMapping("/isOpened")
    @ApiOperation(value = "查询是否有已经购买过服务", httpMethod = "GET")
    public List<ServiceInfo> isOpened(@RequestParam String accountId, @RequestParam String productId) {
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(productId)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "参数不能为空", new RuntimeException());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", accountId);
        map.put("productId", productId);
        List<ServiceInfo> serviceInfos = sis.selectByMap(map);
        List<ServiceInfo> result = new ArrayList<>();
        if (null != serviceInfos && serviceInfos.size() > 0) {
            for (ServiceInfo service : serviceInfos) {
                if (ServiceStatus.Opening.getCode().equals(service.getServiceStatus()) ||
                        ServiceStatus.Normal.getCode().equals(service.getServiceStatus()) ||
                        ServiceStatus.Closed.getCode().equals(service.getServiceStatus())) {
                    result.add(service);
                }
            }

        }
        return result;
    }

    /**
     * 我的服务分页查询
     *
     * @param page
     * @param size
     * @param entity
     * @return
     */
    @PostMapping("/pages")
    @ApiOperation(value = "我的服务", httpMethod = "POST")
    public Page<ServiceInfo> pages(@RequestParam int page, @RequestParam int size, @RequestBody(required = false) ServiceInfo entity) {
        Page<ServiceInfo> pages = new Page<>(page, size);
        if (null != entity) {
            if (null != entity.getNum()) {
                entity.setEndDate(DateUtils.addDays(new Date(), entity.getNum()));
            }
        }
        return sis.page(pages, entity);
    }

    /**
     * 查询关联商品是否开通或开通过
     *
     * @param siteCode
     * @param productId
     * @return
     */
    @GetMapping("/queryRelatedProds")
    @ApiOperation(value = "查询状态为已关闭、开通中、正常的关联商品", httpMethod = "GET")
    public List<ServiceInfo> relatedProducts(@RequestParam @ApiParam("父账号siteCode") String siteCode,
                                             @RequestParam @ApiParam("关联商品Id)") String productId) {
        List<ServiceInfo> list = sis.relatedProducts(siteCode, productId, ServiceStatus.Normal.getCode(), ServiceStatus.Closed.getCode(), ServiceStatus.Opening.getCode());
        if (null == list) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 服务列表查询
     *
     * @param map
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "服务列表查询", hidden = true)
    public List<ServiceInfo> list(@RequestBody(required = false) Map<String, Object> map) {
        return sis.selectByMap(map);
    }

    /**
     * 查询服务详情
     *
     * @param serviceNo
     * @return
     */
    @GetMapping
    @ApiOperation(value = "查询服务详情", httpMethod = "GET")
    public ServiceInfo select(@RequestParam String serviceNo) {

        Map<String, Object> map = new HashMap<>();
        map.put("serviceNo", serviceNo);
        List<ServiceInfo> list = sis.selectByMap(map);
        if (null == list || list.size() == 0) {
            throw new AppException(ExceptionType.SYS_PARAMETER, new RuntimeException("未能查询到商品服务号为" + serviceNo + "的商品"));
        }
        if (list.size() > 1) {
            throw new AppException(ExceptionType.SYS_RUNTIME, new RuntimeException("查询到" + list.size() + "个服务号为" + serviceNo + "的商品"));
        }

        return list.get(0);

    }

    /**
     * 更新服务信息
     *
     * @param serviceInfo
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新服务信息", httpMethod = "PUT")
    public boolean update(@RequestBody ServiceInfo serviceInfo) {
        return sis.updateById(serviceInfo);
    }

    /**
     * 计费接口
     *
     * @return
     */
    @PostMapping("/charging")
    @ApiOperation(value = "计费接口", httpMethod = "POST")
    public Result billing(@RequestBody Charging charging) {

        Result result = new Result();
        if (null == charging) {
            result.setState("failed");
            result.setError_msg("参数为空,请检查传入格式是否正确");
            return result;
        }
        Field[] fields = charging.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                if (annotation == null) {
                    continue;
                }
                field.setAccessible(true);
                String name = field.getName();
                if (annotation.required()) {
                    Object o = field.get(charging);
                    if (null == o || StringUtils.isBlank(o.toString())) {
                        result.setState("failed");
                        result.setError_msg("参数" + name + "不能为空");
                        return result;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, e);
        }

        if (null == charging.getUsage() || charging.getUsage().length == 0) {
            result.setState("failed");
            result.setError_msg("按需计费参数usage不能为空");
            return result;
        }
        for (Usage usage : charging.getUsage()) {
            if (StringUtils.isBlank(usage.getId()) || null == usage.getValue()) {
                result.setState("failed");
                result.setError_msg("用量usage中的id和value不能为空");
                return result;
            }
            if (usage.getValue() < 0) {
                result.setState("failed");
                result.setError_msg("用量value不能小于0");
                return result;
            }
        }

        //将参数放入Rabbitmq
        producer.publishChargeMsg(charging);
        result.setState("success");
        return result;

    }

    /**
     * 关闭服务接口
     *
     * @param appIdMap
     * @return
     */
    @PostMapping("/close")
    @ApiOperation(value = "关闭服务", httpMethod = "POST")
    public ResultInfo closeService(@RequestBody Map<String, String> appIdMap) {
        if (!appIdMap.containsKey("appId") || StringUtils.isBlank(appIdMap.get("appId"))) {
            return ResultInfo.withFail("appId不能为空");
        }
        ServiceInfo service = sis.findByAppId(appIdMap.get("appId").trim());

        if (null == service) {
            return ResultInfo.withFail("未查询到服务,请检查appId是否正确");
        }

        CloseParam close = new CloseParam();
        close.setAppId(service.getAppId());
        close.setOpenType(service.getOpenType());
        close.setUserCode(service.getAccountId());
        if (OpenType.Demand.getCode().equals(service.getOpenType())) {
            Charge[] charges = JSON.parseObject(service.getChargeCodes(), Charge[].class);
            close.setCharg(charges);
        }
        ServiceInfo update = new ServiceInfo();
        try {
            //查询关闭接口
            JSONArray jsonArray = ps.queryProduct(HeaderUtil.getAuth(), service.getProductId());
            JSONObject product = jsonArray.getJSONObject(0);
            String closeUrl = product.getString("closeInterface");
            Result result = ReqUtil.operaProd(HeaderUtil.getAuth(), close, closeUrl);
            if (null == result || !"success".equalsIgnoreCase(result.getState())) {
                log.info("调用关闭商品接口失败,info:" + JSON.toJSONString(result) + ",请人工通知卖家");
                update.setFailReason("调用关闭接口失败:" + JSON.toJSONString(result, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));
            }
        } catch (Exception e) {
            log.error("关闭商品失败", e);
            update.setFailReason("关闭商品异常:" + e.getMessage());
            //TODO 发送站内信通知相关人员
        }

        update.setUuid(service.getUuid());
        update.setServiceStatus(ServiceStatus.Closed.getCode());
        //设置14天后销毁
        update.setDestroyDate(DateUtils.addDays(new Date(), 14));
        sis.updateById(update);
        //TODO 准备销毁服务
        //dsp.publishMsg(service.getUuid());
        return ResultInfo.withSuccess("关闭成功");
    }

    /**
     * 扫描已关闭的按量服务然后重新开通
     *
     * @param flag
     */
    @PostMapping("/finishRecharge")
    @ApiOperation(value = "扫描已关闭的按量服务然后重新开通")
    public void finishRecharge(@RequestParam boolean flag, @RequestParam String accountId) {
        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("serviceStatus", ServiceStatus.Closed.getCode());
            map.put("openType", OpenType.Demand.getCode());
            map.put("accountId", accountId);
            List<ServiceInfo> list = sis.selectByMap(map);
            if (null != list && list.size() > 0) {
                String token = HeaderUtil.getAuth();
                for (ServiceInfo service : list) {
                    try {
                        //开通商品，默认使用保存的开通接口，如果查询到商品则使用查询到的开通接口
                        String openUrl = service.getOpenUrl();
                        OpenParam body = new OpenParam();
                        Charge[] charg = JSON.parseObject(service.getChargeCodes(), Charge[].class);
                        body.setCharg(charg);
                        body.setOpenType(service.getOpenType());
                        //如果查不到商品code则默认传id
                        body.setProductCode(service.getProductId());
                        JSONArray jsonArray = ps.queryProduct(token, service.getProductId());
                        if (null != jsonArray && jsonArray.size() > 0) {
                            //获取商品code
                            JSONObject product = jsonArray.getJSONObject(0);
                            String code = product.getString("code");
                            openUrl = product.getString("openInterface");//获取开通接口
                            body.setProductCode(code);
                        }
                        body.setUserCode(service.getAccountId());
                        body.setAppId(service.getAppId());
                        body.setVersionCode(service.getVersionId());
                        Result rt = ReqUtil.operaProd(token, body, openUrl);
                        if (null == rt) {
                            service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                            service.setFailReason("重新开通失败,开通返回null");
                        } else {
                            //检查是否开通成功
                            if ("success".equalsIgnoreCase(rt.getState())) {
                                service.setServiceStatus(ServiceStatus.Normal.getCode());
                                //清空销毁时间
                                service.setDestroyDate(null);
                            } else if ("failed".equalsIgnoreCase(rt.getState())) {
                                service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                                service.setFailReason("重新开通失败:" + JSON.toJSONString(rt));
                            } else if ("opening".equalsIgnoreCase(rt.getState())) {
                                service.setServiceStatus(ServiceStatus.Opening.getCode());
                            } else {
                                //无法判断的格式一律是开通失败
                                service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                                service.setFailReason("重新开通失败:" + JSON.toJSONString(rt));
                            }
                        }
                        if (ServiceStatus.Normal.getCode().equals(service.getServiceStatus())) {
                            msgUtil.sendMsg(token, MsgSubType.ServiceOpenNotice.getCode(), service.getSiteCode(), service.getAccountId(), service.getProductId(), service.getServiceNo(), "", "", "");
                        }
                    } catch (Exception e) {
                        service.setFailReason("重新开通失败:" + e.getMessage());
                        service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                    }
                }
                //批量更新
                sis.updateBatchById(list);
            }
        }
    }

    /**
     * 重新开通服务
     *
     * @param appId
     * @return
     */
    @GetMapping("/reOpen")
    @ApiOperation(value = "重新开通", httpMethod = "GET")
    public ResultInfo reOpen(@RequestParam String appId) {
        try {
            if (!redisLock.lock(appId, "reOpen", 30000)) {
                return ResultInfo.withFail("请勿重复操作");
            }
            ServiceInfo service = sis.findByAppId(appId);
            if (null == service) {
                return ResultInfo.withFail("未找到该服务");
            }
            if (ServiceStatus.Normal.getCode().equals(service.getServiceStatus())) {
                return ResultInfo.withFail("服务正常,无需重新开通");
            }
            if (ServiceStatus.Destroy.getCode().equals(service.getServiceStatus())) {
                return ResultInfo.withFail("该服务已销毁,无法重新开通");
            }
            if (OpenType.Cycle.getCode().equals(service.getOpenType())) {
                return ResultInfo.withFail("包周期产品不支持重新开通,请续费");
            }
            String accountId = service.getAccountId();
            String productId = service.getProductId();
            //判断该用户是否有相同的正在运行或开通中的服务
            Map<String, Object> map = new HashMap<>();
            map.put("accountId", accountId);
            map.put("productId", productId);
            List<ServiceInfo> serviceInfos = sis.selectByMap(map);
            if (null != serviceInfos && serviceInfos.size() > 0) {
                for (ServiceInfo serviceInfo : serviceInfos) {
                    if (ServiceStatus.Normal.getCode().equals(serviceInfo.getServiceStatus()) ||
                            ServiceStatus.Opening.getCode().equals(serviceInfo.getServiceStatus())) {
                        return ResultInfo.withFail("已经开通过该服务");
                    }
                }

            }
            //判断该用户是否符合重新开通的条件
            String token = HeaderUtil.getAuth();
            Balance balance = bs.query(token, accountId);
            BigDecimal balDecimal = balance.getBalance(); //余额
            BigDecimal credits = balance.getCredits(); //信用额度
            //如果该用户已欠款则判断是否有信用额度
            if (balDecimal.compareTo(BigDecimal.ZERO) <= 0) {
                //如果无信用额度或者信用额度不足则不能开通
                if (balDecimal.add(credits).compareTo(BigDecimal.ZERO) <= 0) {
                    return ResultInfo.withFail("该用户余额不足,无法重新开通");
                }
            }
            //开通
            String openUrl = service.getOpenUrl();
            OpenParam body = new OpenParam();
            if (OpenType.Demand.getCode().equals(service.getOpenType())) {
                List<Metric> metrics = ps.queryPrice(token, null, service.getProductId());
                if (metrics != null && metrics.size() > 0) {
                    Charge[] charg = new Charge[metrics.size()];
                    for (int i = 0; i < metrics.size(); i++) {
                        Metric metric = metrics.get(i);
                        Charge charge = new Charge();
                        charge.setId(metric.getUuid());
                        charge.setTypeCode(metric.getType());
                        charge.setType(metric.getName());
                        charg[i] = charge;
                    }
                    body.setCharg(charg);
                }
            }
            body.setOpenType(service.getOpenType());
            //如果查不到商品code则默认传id
            body.setProductCode(productId);
            JSONArray jsonArray = ps.queryProduct(token, productId);
            if (null != jsonArray && jsonArray.size() > 0) {
                //获取商品code
                JSONObject product = jsonArray.getJSONObject(0);
                String code = product.get("code").toString();
                openUrl = product.get("openInterface").toString();//获取开通接口
                body.setProductCode(code);
            }
            body.setUserCode(service.getAccountId());
            body.setAppId(service.getAppId());
            body.setVersionCode(service.getVersionId());
            Result rt = ReqUtil.operaProd(token, body, openUrl);
            if (null == rt) {
                service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                service.setFailReason("重新开通失败,开通返回null");
            } else {
                //检查是否开通成功
                if ("success".equalsIgnoreCase(rt.getState())) {
                    service.setDestroyDate(null);
                    service.setServiceStatus(ServiceStatus.Normal.getCode());
                } else if ("failed".equalsIgnoreCase(rt.getState())) {
                    service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                    service.setFailReason("重新开通失败:" + JSON.toJSONString(rt));
                } else if ("opening".equalsIgnoreCase(rt.getState())) {
                    service.setServiceStatus(ServiceStatus.Opening.getCode());
                } else {
                    //无法判断的格式一律是开通失败
                    service.setServiceStatus(ServiceStatus.OpenFail.getCode());
                    service.setFailReason("重新开通失败:" + JSON.toJSONString(rt));
                }
            }
            if (ServiceStatus.Normal.getCode().equals(service.getServiceStatus())) {
                msgUtil.sendMsg(token, MsgSubType.ServiceOpenNotice.getCode(), service.getSiteCode(), service.getAccountId(), productId, service.getServiceNo(), "", "", "");
            }
            sis.updateById(service);
            if (!ServiceStatus.Normal.getCode().equals(service.getServiceStatus())) {
                return ResultInfo.withFail("重新开通失败,失败原因请在服务列表中查看");
            }
        } catch (Exception e) {
            log.info("重新开通发生异常:", e);
            return ResultInfo.withFail("重新开通失败,失败原因请在服务列表中查看");
        }
        return ResultInfo.withSuccess("重新开通成功");
    }
    
    /**
     * 特殊定制的人工服务，只返回正确
     *
     * @param appId
     * @return
     */
    @PostMapping("/human-services")
    @ApiOperation(value = "人工服务")
    public Result humanServics(@RequestParam(required=true,value="action-id") String  action,@ApiParam("开通明细") @RequestBody(required =false) OpenParam detail) {
    	
    	Result rs = new Result();
    	rs.setState("success");
    	
    	return rs;
    }
    

}
