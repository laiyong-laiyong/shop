package com.sobey.module.voucher.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.common.response.ResultInfo;
import com.sobey.module.common.util.RedisLock;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackOrder;
import com.sobey.module.fegin.mallPack.packOrder.service.PackOrderService;
import com.sobey.module.fegin.order.entity.response.Order;
import com.sobey.module.fegin.order.service.OrderService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.voucher.model.Voucher;
import com.sobey.module.voucher.model.VoucherAccount;
import com.sobey.module.voucher.service.VoucherAccountService;
import com.sobey.util.business.header.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wcy
 * 代金券账户查询
 */
@RestController
@RequestMapping("/${api.v1}/vouAccount")
@Api(description = "代金券账户相关接口")
public class VoucherAccountController {

    private static final Logger log = LoggerFactory.getLogger(VoucherAccountController.class);

    @Autowired
    private VoucherAccountService vas;
    @Autowired
    private OrderService os;
    @Autowired
    private PackOrderService pos;
    @Autowired
    private ProductService ps;
    @Autowired
    private RedisLock redisLock;

    /**
     * 查询代金券账户详情
     *
     * @param accountId
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation(value = "查询余额账户详情", httpMethod = "GET")
    public VoucherAccount detail(@RequestParam String accountId) {
        if (StringUtils.isBlank(accountId)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "accountId不能为空", new RuntimeException("accountId不能为空"));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", accountId);
        List<VoucherAccount> accounts = vas.selectByMap(map);
        if (null == accounts || accounts.size() != 1) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "账户不存在", new RuntimeException("账户不存在"));
        }
        return accounts.get(0);
    }

    /**
     * 更新代金券账户
     *
     * @param vouAccount
     */
    @PutMapping
    public void update(@RequestBody VoucherAccount vouAccount) {
        vas.updateById(vouAccount);
    }

    /**
     * 代金券支付
     *
     * @param orderNo
     * @return
     */
    @PostMapping("/pay/{orderNo}")
    @ApiOperation(value = "代金券支付", httpMethod = "POST")
    public ResultInfo pay(@PathVariable(value = "orderNo") @ApiParam(value = "订单号,rest风格") String orderNo) {
        ResultInfo res = new ResultInfo();
        if (StringUtils.isBlank(orderNo)) {
            res.setRt_code("FAIL");
            res.setRt_msg("参数为空");
            return res;
        }
        try {
            if (!redisLock.lock(orderNo, "vouPay", 30000)) {
                res.setRt_code("FAIL");
                res.setRt_msg("请勿重复支付");
                return res;
            }
            String token = HeaderUtil.getAuth();
            Order order = os.findByOrderNo(token, orderNo);
            if (null == order) {
                res.setRt_code("FAIL");
                res.setRt_msg("未查询到订单信息");
                return res;
            }
            if (PayStatus.Paid.getCode().equals(order.getPaymentStatus())) {
                res.setRt_code("FAIL");
                res.setRt_msg("订单已支付");
                return res;
            }
            //判断是否已失效
            if (PayStatus.Expired.getCode().equals(order.getPaymentStatus())){
                res.setRt_code("FAIL");
                res.setRt_msg("订单已失效,请重新下单");
                return res;
            }
            String accountId = order.getAccountId();
            String productId = order.getProductId();
            Map<String, Object> map = new HashMap<>();
            map.put("accountId", accountId);
            List<VoucherAccount> list = vas.selectByMap(map);
            if (null == list || list.size() != 1) {
                res.setRt_code("FAIL");
                res.setRt_msg("未查询到代金券账户");
                return res;
            }
            VoucherAccount voucherAccount = list.get(0);
            BigDecimal orderAmount = order.getOrderAmount();
            if (voucherAccount.getVouAmount().doubleValue() < orderAmount.doubleValue()) {
                res.setRt_code("FAIL");
                res.setRt_msg("代金券余额不足");
                return res;
            }
            JSONArray jsonArray = ps.queryProduct(token, productId);
            String str = JSON.toJSONString(jsonArray.get(0));
            JSONObject product = JSON.parseObject(str, JSONObject.class);
            if (!"1".equals(product.getString("voucher"))) {
                res.setRt_code("FAIL");
                res.setRt_msg("该商品不支持代金券");
                return res;
            }
            return vas.pay(order, voucherAccount);
        } catch (Exception e) {
            log.info("代金券支付异常:", e);
            res.setRt_code("FAIL");
            res.setRt_msg(e.getMessage());
            return res;
        }
    }

    /**
     * 代金券支付
     *
     * @param orderNo
     * @return
     */
    @PostMapping("/pay/packOrder/{orderNo}")
    @ApiOperation(value = "代金券支付套餐包订单", httpMethod = "POST")
    public ResultInfo payPackOrder(@PathVariable(value = "orderNo") @ApiParam(value = "订单号,rest风格") String orderNo) {
        ResultInfo res = new ResultInfo();
        if (StringUtils.isBlank(orderNo)) {
            res.setRt_code("FAIL");
            res.setRt_msg("参数为空");
            return res;
        }
        try {
            if (!redisLock.lock(orderNo, "vouPay", 30000)) {
                res.setRt_code("FAIL");
                res.setRt_msg("请勿重复支付");
                return res;
            }
            String token = HeaderUtil.getAuth();
            MallPackOrder order = pos.findByOrderNo(token, orderNo);
            if (null == order) {
                res.setRt_code("FAIL");
                res.setRt_msg("未查询到订单信息");
                return res;
            }
            if (PayStatus.Paid.getCode().equals(order.getPayStatus())) {
                res.setRt_code("FAIL");
                res.setRt_msg("订单已支付");
                return res;
            }
            //判断是否已失效
            if (PayStatus.Expired.getCode().equals(order.getPayStatus())){
                res.setRt_code("FAIL");
                res.setRt_msg("订单已失效,请重新下单");
                return res;
            }

            VoucherAccount vouAccount = vas.findByAccountId(order.getAccountId());
            if (null == vouAccount) {
                res.setRt_code("FAIL");
                res.setRt_msg("未查询到代金券信息");
                return res;
            }
            double orderAmount = order.getOrderAmount().doubleValue();
            double vouAmount = vouAccount.getVouAmount().setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            if (vouAmount < orderAmount) {
                res.setRt_code("FAIL");
                res.setRt_msg("代金券余额不足");
                return res;
            }
            return vas.payPackOrder(order, vouAccount);
        } catch (Exception e) {
            log.info("代金券支付异常:", e);
            res.setRt_code("FAIL");
            res.setRt_msg("代金券支付异常");
            return res;
        }
    }
    
    @PostMapping("/list")
    @ApiOperation(value = "列表", httpMethod = "POST")
    public List<VoucherAccount> list(@RequestBody(required=false) VoucherAccount voucher) {
    	
    	Wrapper<VoucherAccount> wp = new EntityWrapper<VoucherAccount>(voucher);
    	List<VoucherAccount> list = this.vas.selectList(wp);
    	
    	return list;
    }

}
