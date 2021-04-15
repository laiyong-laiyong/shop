package com.sobey.module.balance.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.balance.entity.RechargeBalanceParam;
import com.sobey.module.balance.entity.RequestParams;
import com.sobey.module.balance.model.Balance;
import com.sobey.module.balance.service.BalanceService;
import com.sobey.module.balance.service.BalanceTransactionService;
import com.sobey.module.common.enumeration.EffectiveDurationUnitType;
import com.sobey.module.common.enumeration.IsEffective;
import com.sobey.module.common.response.ResultInfo;
import com.sobey.module.common.util.*;
import com.sobey.module.fegin.mallPack.pack.entity.MallPack;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackOrder;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackResource;
import com.sobey.module.fegin.mallPack.packOrder.service.PackOrderService;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.order.entity.response.Order;
import com.sobey.module.fegin.order.service.OrderService;
import com.sobey.module.fegin.serviceInfo.entity.response.ServiceInfo;
import com.sobey.module.fegin.serviceInfo.service.ServiceInfoService;
import com.sobey.module.order.OrderType;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.appid.AppIdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 余额相关接口
 * @Author WuChenYang
 * @Since 2020/2/6 11:17
 */
@RestController
@RequestMapping("/${api.v1}/balance")
@Api(value = "余额相关接口", description = "余额相关接口")
public class BalanceController {

    private static final Logger log = LoggerFactory.getLogger(BalanceController.class);


    @Autowired
    private OrderService os;
    @Autowired
    private BalanceService bs;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private AppIdUtil appIdUtil;
    @Autowired
    private MsgUtil msgUtil;
    @Autowired
    private PackOrderService pos;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private BalUtil balUtil;
    @Autowired
    private NoticePackUtil noticePackUtil;
    @Autowired
    private BalanceTransactionService bts;

    /**
     * 余额支付
     *
     * @param param totalFee-总金额  orderNo-订单号 accountId-用户id productName-商品名称
     */
    @PostMapping("/pay")
    @ApiOperation(value = "余额支付", httpMethod = "POST")
    public ResultInfo rechargePay(@RequestBody @ApiParam(name = "balance", value = "余额支付条件,必填") RequestParams param) {
        ResultInfo rt = new ResultInfo();
        String orderNo = "";
        try {
            String token = HeaderUtil.getAuth();
            Date date = new Date();
            //参数为 账号 订单号 总金额(元)
            Field[] fields = param.getClass().getDeclaredFields();
            rt = ValidateUtil.validate(fields, param);
            if ("FAIL".equalsIgnoreCase(rt.getRt_code())) {
                return rt;
            }
            orderNo = param.getOrderNo();
            if (!redisLock.lock(orderNo, "pay", 30000)) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("请勿重复支付");
                return rt;
            }
            //查询订单信息
            Order order = os.findByOrderNo(token, orderNo);
            if (null == order) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("未查询到订单号为" + orderNo + "的订单");
                return rt;
            }
            String siteCode = order.getSiteCode();
            String accountId = order.getAccountId();
            String productId = order.getProductId();
            //判断订单是否未支付
            if (PayStatus.Paid.getCode().equals(order.getPaymentStatus())) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("该订单" + PayStatus.getDesc(order.getPaymentStatus()) + ",请勿重复支付");
                return rt;
            }
            //判断是否已失效
            if (PayStatus.Expired.getCode().equals(order.getPaymentStatus())) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("订单已失效,请重新下单");
                return rt;
            }

            //判断支付账户与订单创建账户是否一致
            if (!param.getAccountId().equals(accountId)) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("只能支付本人创建的订单");
                return rt;
            }
            BigDecimal totalFee = order.getDiscountPrice();
            //查询余额
            Map<String, Object> bsMap = new HashMap<>();
            bsMap.put("accountId", param.getAccountId());
            List<Balance> balances = bs.selectByMap(bsMap);
            //查询不到说明没有充值过余额
            if (null == balances || balances.size() == 0) {
                log.info("未查询到账户" + param.getAccountId() + "的余额信息");
                rt.setRt_code("FAIL");
                rt.setRt_msg("未查询到余额信息");
                return rt;
            }
            Balance balance = balances.get(0);
            BigDecimal balDecimal = balance.getBalance();
            BigDecimal credits = balance.getCredits();
            if (null == credits){
                credits = BigDecimal.ZERO;
            }
            if (null == balDecimal) {
                balDecimal = BigDecimal.ZERO;
            }
            if (balDecimal.compareTo(totalFee) < 0){
                if (balDecimal.add(credits).compareTo(totalFee) < 0) {
                    rt.setRt_code("FAIL");
                    rt.setRt_msg("余额与信用额度不足,请充值");
                    return rt;
                }
            }
            //是否第一次欠费
            boolean isFirstArrears = true;
            if (balDecimal.compareTo(BigDecimal.ZERO) < 0){
                isFirstArrears = false;
            }
            //扣除费用
            balDecimal = balDecimal.subtract(totalFee);
            //修改订单状态并增加商品服务信息
            Map<String, Object> updateOrder = new HashMap<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

            updateOrder.put("payAmount", totalFee);
            updateOrder.put("payDate", format.format(date));
            updateOrder.put("paymentStatus", PayStatus.Paid.getCode());
            updateOrder.put("payMethod", PayConstant.PayMethod.Balance.getCode());
            //要更新的余额信息
            Balance bal = new Balance();
            bal.setUuid(balance.getUuid());
            bal.setSign(BalUtil.encodeBal(balDecimal));
            bal.setBalance(balDecimal);
            bal.setUpdateDate(date);

            //服务信息
            ServiceInfo service = null;
            //开通商品
            if (OrderType.New.getCode().equals(order.getOrderType())) {
                String appId = appIdUtil.getAppIdMD5(accountId, productId, order.getVersionId());
                service = new ServiceInfo();
                //服务号
                String serviceNo = IdWorker.getIdStr();
                service.setUuid(IdWorker.get32UUID())
                        .setAppId(appId)
                        .setServiceNo(serviceNo)
                        .setAccountId(accountId)
                        .setAccount(order.getAccount())
                        .setOpenType(order.getOpenType())
                        .setChargeCodes(order.getChargeCodes())
                        .setProductId(productId)
                        .setProductSpecs(order.getProductSpecs())
                        .setOpenUrl(order.getOpenUrl())
                        .setRenewUrl(order.getRenewUrl())
                        .setCloseUrl(order.getCloseUrl())
                        .setCreateDate(date)
                        .setExpireDate(DateUtils.addDays(date, order.getDuration()))
                        .setVersionId(order.getVersionId())
                        .setSpecifications(order.getSpecifications())
                        .setSiteCode(siteCode)
                        .setRelatedProductId(order.getRelatedProductId())
                        .setRelatedProductName(order.getRelatedProductName());
                updateOrder.put("serviceNo", serviceNo);//更新订单中的服务编号
            }
            //续费商品
            if (OrderType.Renew.getCode().equals(order.getOrderType())) {
                //查询服务信息
                service = sis.select(token, order.getServiceNo());
                if (null == service) {
                    rt.setRt_code("FAIL");
                    rt.setRt_msg("未能查询到服务号为" + order.getServiceNo() + "的服务信息");
                    return rt;
                }
                //续费增加时长
                Date expireDate = service.getExpireDate();
                //服务是否是已过期处于关闭状态
                if (ServiceStatus.Closed.getCode().equals(service.getServiceStatus())) {
                    expireDate = date;
                }
                //以天计算
                Date renewLater = DateUtils.addDays(expireDate, order.getDuration());
                service.setExpireDate(renewLater);
                service.setUpdateDate(date);
                service.setServiceStatus(ServiceStatus.Normal.getCode());
            }
            bts.updateData(token, order, updateOrder, bal, service);
            rt.setRt_code("SUCCESS");
            rt.setRt_msg("支付成功");
            //发送消息,这里只发送续费消息，如果是新购会在创建完服务后发送消息
            if (OrderType.Renew.getCode().equals(order.getOrderType())) {
                msgUtil.sendMsg(token, MsgSubType.ServiceRenewNotice.getCode(), siteCode,accountId, productId, service.getServiceNo(), totalFee.toString(), "", "");
            }
            //欠费处理
            if (balDecimal.compareTo(BigDecimal.ZERO) < 0) {
                if (isFirstArrears) {
                    //发送欠费通知
                    msgUtil.sendMsg(token, MsgSubType.ArrearsNotice.getCode(), siteCode,accountId, "", "", "", balDecimal.setScale(2, BigDecimal.ROUND_DOWN).toString(), credits.toString());
                    //发送欠费通知给运维人员
                    msgUtil.sendNoticeToAdmin(token, siteCode,accountId, balDecimal.toString(), credits.toString());
                }
            }
            return rt;
        } catch (Exception e) {
            rt.setRt_code("FAIL");
            rt.setRt_msg("支付失败");
            log.info("余额支付异常", e);
            return rt;
        }
    }

    /**
     * 余额支付购买套餐包的订单
     *
     * @return
     */
    @PostMapping("/pay/pack")
    @ApiOperation(value = "余额支付", httpMethod = "POST")
    public ResultInfo payPackOrder(@RequestBody @ApiParam(name = "balance", value = "余额支付条件,必填") RequestParams param) {
        ResultInfo rt = new ResultInfo();
        String orderNo = "";
        try {
            Field[] fields = param.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                field.setAccessible(true);
                if (field.get(param) == null || StringUtils.isBlank(String.valueOf(field.get(param)))) {
                    rt.setRt_code("FAIL");
                    rt.setRt_msg(field.getName() + "不能为空");
                    return rt;
                }
            }
            orderNo = param.getOrderNo();
            if (!redisLock.lock(orderNo, "pay", 30000)) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("请勿重复支付");
                return rt;
            }
            Date date = new Date();
            String token = HeaderUtil.getAuth();
            //查询订单信息
            MallPackOrder order = pos.findByOrderNo(token, orderNo);
            if (null == order) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("未查询到订单号为" + orderNo + "的订单");
                return rt;
            }
            //判断订单是否未支付
            if (PayStatus.Paid.getCode().equals(order.getPayStatus())) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("该订单" + PayStatus.getDesc(order.getPayStatus()) + ",请勿重复支付");
                return rt;
            }
            //判断是否已失效
            if (PayStatus.Expired.getCode().equals(order.getPayStatus())) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("订单已失效,请重新下单");
                return rt;
            }
            String siteCode = order.getSiteCode();
            String accountId = order.getAccountId();
            //判断支付账户与订单创建账户是否一致
            if (!param.getAccountId().equals(accountId)) {
                rt.setRt_code("FAIL");
                rt.setRt_msg("只能支付本人创建的订单");
                return rt;
            }
            //总金额
            BigDecimal totalFee = order.getDiscountPrice();
            //查询余额
            Balance balAccount = bs.findByAccountId(accountId);
            //查询不到说明没有充值过余额
            if (null == balAccount) {
                log.info("未查询到账户" + param.getAccountId() + "的余额信息");
                rt.setRt_code("FAIL");
                rt.setRt_msg("未查询到余额信息");
                return rt;
            }
            //账户余额
            BigDecimal balAmount = balAccount.getBalance();
            BigDecimal credits = balAccount.getCredits();
            if (null == credits){
                credits = BigDecimal.ZERO;
            }
            if (null == balAmount) {
                balAmount = BigDecimal.ZERO;
            }
            if (balAmount.compareTo(totalFee) < 0){
                if (balAmount.add(credits).compareTo(totalFee) < 0) {
                    rt.setRt_code("FAIL");
                    rt.setRt_msg("余额与信用额度不足,请充值");
                    return rt;
                }
            }
            //是否第一次欠费
            boolean isFirstArrears = true;
            if (balAmount.compareTo(BigDecimal.ZERO) < 0){
                isFirstArrears = false;
            }
            //创建套餐包
            MallPack mallPack = new MallPack();
            String uuid = IdWorker.get32UUID();
            BeanUtils.copyProperties(order, mallPack);
            mallPack.setEffectiveDate(date);
            mallPack.setIsEffective(IsEffective.Effective.getCode());
            Date expireDate = EffectiveDurationUnitType.getExpireDate(order.getUnit(), order.getDuration(), date);
            mallPack.setExpireDate(expireDate);
            mallPack.setCreateDate(date);
            mallPack.setUuid(uuid);
            List<MallPackResource> resources = mallPack.getResources();
            for (MallPackResource resource : resources) {
                resource.setCreateDate(date);
                resource.setRemainingSize(resource.getSize());
                resource.setMallPackId(uuid);
                resource.setUuid(IdWorker.get32UUID());
                resource.setExpireDate(expireDate);
            }
            mallPack.setResources(resources);
            //新建余额账户对象用来更新对应信息
            Balance updateBal = new Balance();
            balAmount = balAmount.subtract(totalFee);
            updateBal.setUuid(balAccount.getUuid());
            updateBal.setBalance(balAmount);
            updateBal.setSign(BalUtil.encodeBal(balAmount));

            //更新订单信息
            Map<String, Object> update = new HashMap<>();
            update.put("payAmount", order.getDiscountPrice());
            update.put("payStatus", PayStatus.Paid.getCode());
            update.put("payDate", DateUtil.format(date, DateUtil.FORMAT_1));
            update.put("payMethod", PayConstant.PayMethod.Balance.getCode());
            bts.updatePackData(token, order.getId(), update, mallPack, updateBal);
            //通知卖家
            noticePackUtil.noticePack(token, order.getPackUuid(),order.getPackName(), order.getProductId(), order.getAccountId(), siteCode, expireDate, resources);
            //欠费处理
            if (balAmount.compareTo(BigDecimal.ZERO) < 0) {
                if (isFirstArrears) {
                    //发送欠费通知
                    msgUtil.sendMsg(token, MsgSubType.ArrearsNotice.getCode(), siteCode,accountId, "", "", "", balAmount.setScale(2, BigDecimal.ROUND_DOWN).toString(), credits.toString());
                    //发送欠费通知给运维人员
                    msgUtil.sendNoticeToAdmin(token, siteCode,accountId, balAmount.toString(), credits.toString());
                }
            }
            rt.setRt_code("SUCCESS");
            rt.setRt_msg("购买成功");
        } catch (Exception e) {
            log.info("购买套餐包时发生异常", e);
            rt.setRt_code("FAIL");
            rt.setRt_msg("支付失败");
        }
        return rt;
    }

    /**
     * 查询余额
     *
     * @param accountId
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "查询余额", httpMethod = "GET")
    public Balance queryBalance(@RequestParam String accountId) {
        return bs.findByAccountId(accountId);
    }

    @PutMapping("/credits")
    @ApiOperation(value = "修改授信额度", httpMethod = "PUT")
    public ResultInfo updateCredits(@RequestParam String accountId,
                                    @RequestParam BigDecimal credits) {
        ResultInfo rt = new ResultInfo();
        if (StringUtils.isBlank(accountId)) {
            rt.setRt_code("FAIL");
            rt.setRt_msg("accountId不能为空");
            return rt;
        }
        if (credits.doubleValue() < 0) {
            rt.setRt_code("FAIL");
            rt.setRt_msg("信用额度必须大于0");
            return rt;
        }
        if (bs.updateCredits(accountId, credits) >= 1) {
            rt.setRt_code("SUCCESS");
            rt.setRt_msg("更新成功");
            return rt;
        }
        rt.setRt_code("FAIL");
        rt.setRt_msg("accountId不存在");
        return rt;
    }

    /**
     * 该接口只在内部系统间使用不暴露给前端
     * @param balance
     */
    @PutMapping
    @ApiOperation(value = "更新余额信息", httpMethod = "PUT", hidden = true)
    public void update(@RequestBody Balance balance) {
        if (null == balance) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "余额更新参数错误", new RuntimeException());
        }
        BigDecimal decimal = balance.getBalance().setScale(8, BigDecimal.ROUND_DOWN);
        Balance bal = bs.selectById(balance.getUuid());
        if (null == bal) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "uuid参数错误", new RuntimeException());
        }
        balance.setSign(BalUtil.encodeBal(decimal));
        bs.updateById(balance);
    }

    /**
     * 余额充值
     *
     * @param param
     * @return
     */
    @PostMapping("/recharge")
    @ApiOperation(value = "余额充值", httpMethod = "POST")
    public ResultInfo recharge(@RequestBody RechargeBalanceParam param) {
        ResultInfo result = balUtil.validateRechargeBalanceParam(param);
        if ("FAIL".equalsIgnoreCase(result.getRt_code())) {
            return result;
        }
        Balance balAccount = bs.findByAccountId(param.getAccountId());
        if (null == balAccount) {
            result.setRt_code("FAIL");
            result.setRt_msg("未查找到该帐户余额信息");
            return result;
        }
        balAccount.setBalance(balAccount.getBalance().add(param.getAmount()));
        balAccount.setSign(BalUtil.encodeBal(balAccount.getBalance()));
        bts.updateBalAccount(balAccount, param);
        result.setRt_code("SUCCESS");
        result.setRt_msg("充值成功");
        //充值后通知服务模块扫描已经关闭的按量服务，重新开通
        try {
            sis.finishRecharge(HeaderUtil.getAuth(), balAccount.getBalance().doubleValue() > 0, balAccount.getAccountId());
            msgUtil.sendMsg(HeaderUtil.getAuth(), MsgSubType.RechargeNotice.getCode(), balAccount.getSiteCode(),balAccount.getAccountId(), null, null, param.getAmount().toString(), balAccount.getBalance().setScale(2, BigDecimal.ROUND_DOWN).toString(), "");
        } catch (Exception e) {
            log.info("开通服务或发送消息出现异常:", e);
        }
        return result;

    }

    /**
     * 获取余额充值验证码
     * @param pubKey
     * @return
     */
    @GetMapping("/verifyCode")
    @ApiOperation(value = "获取余额充值验证码",httpMethod = "GET")
    public String getVerifyCode(@RequestParam String money,@RequestParam String pubKey){
        String code = null;
        try {
            code = RSAUtil.encryptByPub(money, pubKey);
        } catch (Exception e) {
            return "公钥错误";
        }
        return code;
    }

    /**
     * 用户注册完成后同步到余额信息表
     *
     * @param balance
     * @return
     */
    @PostMapping("/sync")
    @ApiOperation(value = "同步用户信息到余额表", httpMethod = "POST")
    public ResultInfo syncUserInfo(@RequestBody Balance balance) {

        ResultInfo rt = new ResultInfo();
        if (null == balance) {
            rt.setRt_code("FAIL");
            rt.setRt_msg("参数错误");
            return rt;
        }
        if (StringUtils.isBlank(balance.getAccountId())) {
            rt.setRt_code("FAIL");
            rt.setRt_msg("accountId不能为空");
            return rt;
        }
        if (StringUtils.isBlank(balance.getSiteCode())) {
            rt.setRt_code("FAIL");
            rt.setRt_msg("siteCode不能为空");
            return rt;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", balance.getAccountId());
        List<Balance> balances = bs.selectByMap(map);
        if (balances.size() > 0) {
            rt.setRt_code("FAIL");
            rt.setRt_msg("该用户已经存在,请勿重复同步");
            return rt;
        }
        BigDecimal decimal = BigDecimal.valueOf(0.00).setScale(8, BigDecimal.ROUND_DOWN);
        balance.setBalance(decimal);
        balance.setCredits(BigDecimal.valueOf(0.00));
        balance.setUuid(IdWorker.get32UUID());
        balance.setSign(BalUtil.encodeBal(decimal));
        bs.insert(balance);
        rt.setRt_code("SUCCESS");
        rt.setRt_msg("同步成功");
        return rt;
    }
    
    
    
    @PostMapping("/list")
    @ApiOperation(value = "列表", httpMethod = "POST")
    public List<Balance> list(@RequestBody(required=false) Balance balance) {
    	
    	Wrapper<Balance> wp = new EntityWrapper<Balance>(balance);
    	List<Balance> list = this.bs.selectList(wp);
    	
    	return list;
    }
    
    

}
