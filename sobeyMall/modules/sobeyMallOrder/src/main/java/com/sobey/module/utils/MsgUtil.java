package com.sobey.module.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.msg.request.entity.MsgEntity;
import com.sobey.module.fegin.msg.request.entity.MsgTemplate;
import com.sobey.module.fegin.msg.request.entity.OperationsPersonnel;
import com.sobey.module.fegin.msg.request.service.MsgService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.fegin.user.response.UserDetail;
import com.sobey.module.fegin.user.service.UserService;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.productservice.service.ServiceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author WCY
 * @CreateTime 2020/4/14 11:04
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

    /**
     * @param token
     * @param msgTypeCode
     * @param accountId
     * @param productId
     * @param transAmount 交易金额
     * @param balance     余额
     * @param credits 信用额度
     */
    @Async("executors")
    public void sendMsg(String token, String msgTypeCode, String siteCode,String accountId, String productId, String serviceNo, String transAmount, String balance,String credits) {

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
                UserDetail userDetail = userService.userDetail(siteCode,accountId, token,"sobeyLingYunMall");
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
                List<ServiceInfo> serviceInfos = sis.selectByMap(m);
                if (null != serviceInfos && serviceInfos.size() == 1) {
                    ServiceInfo serviceInfo = serviceInfos.get(0);
                    Date expireDate = serviceInfo.getExpireDate();
                    if (null != expireDate) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                        msgEntity.setExpireTime(format.format(expireDate));
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
     * 发送服务即将到期提醒
     *
     * @param token
     * @param templateUuid
     * @param accountId
     * @param productId
     * @param remainDay
     */
    @Async("executors")
    public void sendServiceExpireRemain(String token, String templateUuid, String siteCode,String accountId, String productId, String remainDay, Date expireDate) {
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
            UserDetail userDetail = userService.userDetail(siteCode,accountId, token,"sobeyLingYunMall");
            if (null != userDetail) {
                //获取用户名
                String username = userDetail.getLogin_name();
                msgEntity.setUsername(username);
            }
        }
        msgEntity.setRemainingTime(remainDay);
        List<String> userCodes = new ArrayList<>();
        userCodes.add(accountId);
        msgEntity.setUserCodes(userCodes);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        msgEntity.setExpireTime(format.format(expireDate));
        msgService.sendMsg(token, templateUuid, msgEntity);
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
                UserDetail userDetail = userService.userDetail(siteCode,accountId, token,"sobeyLingYunMall");
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
    
    /**
     * 代金券使用完毕后通知客户
     * 
     * @param token
     * @param siteCode
     * @param accountId
     */
    public void sendNoticeToCustom(String token,String siteCode,String accountId){
    	 UserDetail userDetail = userService.userDetail(siteCode,accountId, token,"sobeyLingYunMall");
         if (null != userDetail) {
             //获取用户名
             String phone = userDetail.getPhone();
             String username = userDetail.getLogin_name();
             try {
            	 msgService.sendArrearsMsgToAdmin(token,phone,username);
             } catch (Exception e) {
            	 log.error("给号码为"+phone+"的运维值班人员发送短信失败:",e);
             }
         }
    }

}
