package com.sobey.module.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.msg.request.entity.MsgEntity;
import com.sobey.module.fegin.msg.request.entity.MsgTemplate;
import com.sobey.module.fegin.msg.request.entity.OperationsPersonnel;
import com.sobey.module.fegin.msg.request.service.MsgService;
import com.sobey.module.fegin.msg.request.service.ShortMsgService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.fegin.serviceInfo.entity.response.ServiceInfo;
import com.sobey.module.fegin.serviceInfo.service.ServiceInfoService;
import com.sobey.module.fegin.user.response.UserDetail;
import com.sobey.module.fegin.user.service.UserService;
import com.sobey.module.voucher.model.Voucher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author WCY
 * @createTime 2020/4/14 11:04
 * 处理消息的工具类
 */
@Component
public class MsgUtil {

    private static final Logger log = LoggerFactory.getLogger(MsgUtil.class);

    @Autowired
    private MsgService msgService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private ShortMsgService sms;

    /**
     * @param token
     * @param msgTypeCode
     * @param accountId
     * @param productId
     * @param transAmount 交易金额
     * @param balance     余额充值时的余额
     * @param credits 信用额度
     */
    @Async("executors")
    public void sendMsg(String token,
                        String msgTypeCode,
                        String siteCode,
                        String accountId,
                        String productId,
                        String serviceNo,
                        String transAmount,
                        String balance,
                        String credits) {

        try {
            //获取模板
            Map<String, Object> map = new HashMap<>();
            map.put("msgTypeCode", msgTypeCode);
            List<MsgTemplate> templates = msgService.queryTemplate(token, map);
            if (null == templates || templates.size() != 1) {
                log.error("未查询到模板信息,消息发送失败");
                return;
            }
            MsgEntity msgEntity = new MsgEntity();
            if (StringUtils.isNotBlank(productId)) {
                JSONArray jsonArray = productService.queryProduct(token, productId);
                if (null != jsonArray && jsonArray.size() == 1) {
                    //获取商品名称
                    Object o = jsonArray.get(0);
                    String str = JSON.toJSONString(o);
                    JSONObject object = JSON.parseObject(str, JSONObject.class);
                    String productName = object.get("name").toString();
                    msgEntity.setProductName(productName);
                }
            }
            if (StringUtils.isNotBlank(accountId)) {
                UserDetail userDetail = userService.userDetail(siteCode,accountId, token);
                if (null != userDetail) {
                    //获取用户名
                    String username = userDetail.getLogin_name();
                    msgEntity.setUsername(username);
                }
            }

            if (StringUtils.isNotBlank(serviceNo)) {
                //查询服务信息
                Map<String, Object> m = new HashMap<>();
                m.put("serviceNo", serviceNo);
                List<ServiceInfo> serviceInfos = sis.list(token, m);
                if (null != serviceInfos && serviceInfos.size() == 1) {
                    ServiceInfo serviceInfo = serviceInfos.get(0);
                    Date expireDate = serviceInfo.getExpireDate();
                    if (null != expireDate) {
                        msgEntity.setExpireTime(DateUtil.format(expireDate,DateUtil.FORMAT_2));
                        double remainDay = (double) (expireDate.getTime() - System.currentTimeMillis()) / org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY;
                        BigDecimal decimal = BigDecimal.valueOf(remainDay).setScale(0, BigDecimal.ROUND_DOWN);
                        msgEntity.setRemainingTime(decimal.toString());
                    }
                }
            }
            msgEntity.setBalance(balance);
            msgEntity.setTransactionAmount(transAmount);
            msgEntity.setCredits(credits);

            //获取模板id
            MsgTemplate template = templates.get(0);
            String templateUuid = template.getUuid();
            List<String> userCodes = new ArrayList<>();
            userCodes.add(accountId);
            msgEntity.setUserCodes(userCodes);
            //发送消息
            Map<String, String> result = msgService.sendMsg(token, templateUuid, msgEntity);
            if (null != result) {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    if ("SUCCESS".equalsIgnoreCase(entry.getValue())) {
                        log.info("消息发送成功");
                    }
                }
            }
        } catch (Exception e) {
            log.error("消息发送异常", e);
        }
    }

    /**
     * 发送创建代金券的信息
     * @param token
     * @param list
     */
    @Async(value = "executors")
    public void sendVoucherCreateNotice(String token, List<Voucher> list){

        Map<String,Object> map = new HashMap<>();
        map.put("msgTypeCode", MsgSubType.VoucherCreateSuccessNotice.getCode());
        List<MsgTemplate> templates = msgService.queryTemplate(token, map);
        if (null == templates || templates.size() != 1) {
            log.error("未查询到模板信息,消息发送失败");
            return;
        }
        MsgTemplate template = templates.get(0);
        for (Voucher voucher : list) {
            try {
                String issuedAccountId = voucher.getIssuedAccountId();
                String issuedAccount = voucher.getIssuedAccount();
                String voucherCode = voucher.getVouCode();
                String phone = voucher.getIssuedPhone();
                MsgEntity msgEntity = new MsgEntity();
                List<String> userCodes = new ArrayList<>();
                userCodes.add(issuedAccountId);
                msgEntity.setUserCodes(userCodes);
                msgEntity.setUsername(issuedAccount);
                msgEntity.setVoucherCode(voucherCode);
                msgService.sendMsg(token,template.getUuid(),msgEntity);
                //发送短信
                sms.sendNormalShortMessage(token,phone,issuedAccount,voucherCode,"voucher_short_message");
            } catch (Exception e) {
                log.info("发送代金券"+voucher.getVouCode()+"的创建成功站内消息或短信时发生异常",e);
            }
        }
    }

    /**
     * 发送欠费提醒给运维人员
     * @param token
     * @param accountId 用户的userCode  用于查询用户名使用
     * @param balance
     * @param credits
     */
    @Async("executors")
    public void sendNoticeToAdmin(String token,String siteCode,String accountId,String balance,String credits){
        try {
            //获取模板
            Map<String, Object> map = new HashMap<>();
            map.put("msgTypeCode", MsgSubType.ArrearsNoticeToAdmin.getCode());
            List<MsgTemplate> templates = msgService.queryTemplate(token, map);
            if (null == templates || templates.size() != 1) {
                log.info("未查询到模板信息,消息发送失败");
                return;
            }
            MsgEntity msgEntity = new MsgEntity();
            //查询值班的运维人员名单
            List<OperationsPersonnel> list = msgService.listOnDuty(token, "1");
            if (null == list || list.size() == 0){
                log.info("没有运维人员值班");
                return;
            }
            Map<String,String> userCodePhoneNum = new HashMap<>();
            for (OperationsPersonnel personnel : list) {
                userCodePhoneNum.put(personnel.getAccountId(),personnel.getPhoneNum());
            }
            msgEntity.setUserCodes(new ArrayList<>(userCodePhoneNum.keySet()));
            String username = "";
            if (StringUtils.isNotBlank(accountId)) {
                UserDetail userDetail = userService.userDetail(siteCode,accountId, token);
                if (null != userDetail) {
                    //获取用户名
                    username = userDetail.getLogin_name();
                    msgEntity.setUsername(username);
                }
            }
            msgEntity.setBalance(balance);
            msgEntity.setCredits(credits);
            //发送站内信
            msgService.sendMsg(token,templates.get(0).getUuid(),msgEntity);
            //发送短信
            for (String phoneNum : userCodePhoneNum.values()) {
                try {
                    msgService.sendArrearsMsgToAdmin(token,phoneNum,username);
                } catch (Exception e) {
                    log.info("给号码为"+phoneNum+"的运维值班人员发送短信失败:",e);
                }
            }
        } catch (Exception e) {
            log.info("发送欠费提醒给运维人员发生异常:",e);
        }
    }

}
