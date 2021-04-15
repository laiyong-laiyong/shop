package com.sobey.module.productservice.corn;

import com.sobey.framework.spring.SpringContextHolder;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.msg.request.entity.MsgTemplate;
import com.sobey.module.fegin.msg.request.service.MsgService;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.productservice.service.ServiceInfoService;
import com.sobey.module.utils.CacheUtil;
import com.sobey.module.utils.MsgUtil;
import com.sobey.util.common.collection.CollectionKit;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/4/26 14:05
 * 服务即将到期时提醒
 */
public class ExpireNoticeJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(ExpireNoticeJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("扫描服务到期时间任务执行");
        ServiceInfoService sis = SpringContextHolder.getBean(ServiceInfoService.class);
        //查询3天内到期的服务
        Date now = new Date();
        Date expireTime_1 = DateUtils.addDays(now, 1);
        Date expireTime_3 = DateUtils.addDays(now, 4);
        List<ServiceInfo> list_3 = sis.remainNotice(expireTime_1, expireTime_3);
        //查询7天到期的服务
        Date expireTime_7 = DateUtils.addDays(now, 7);
        Date expireTime_8 = DateUtils.addDays(now, 8);
        List<ServiceInfo> list_7 = sis.remainNotice(expireTime_7, expireTime_8);
        if (CollectionKit.isEmpty(list_3) && CollectionKit.isEmpty(list_7)) {
            return;
        }
        //查询服务到期提醒模板
        MsgService msgService = SpringContextHolder.getBean(MsgService.class);
        MsgUtil msgUtil = SpringContextHolder.getBean(MsgUtil.class);
        Map<String, Object> map = new HashMap<>();
        map.put("msgTypeCode", MsgSubType.ServiceExpireRemain.getCode());
        //获取token
        String token = CacheUtil.getToken();
        List<MsgTemplate> templates = msgService.queryTemplate(token, map);
        if (templates == null || templates.size() != 1) {
            log.info("未查询到服务到期提醒模板,消息发送失败");
            return;
        }
        MsgTemplate template = templates.get(0);
        if (list_3 != null && list_3.size() > 0) {
            //发送消息
            for (ServiceInfo serviceInfo : list_3) {
                String siteCode = serviceInfo.getSiteCode();
                String accountId = serviceInfo.getAccountId();
                String productId = serviceInfo.getProductId();
                Date expireDate = serviceInfo.getExpireDate();
                //计算还有多少天到期
                long millions = expireDate.getTime() - System.currentTimeMillis();

                long day = millions / DateUtils.MILLIS_PER_DAY;
                msgUtil.sendServiceExpireRemain(token, template.getUuid(), siteCode, accountId, productId, Long.toString(day), serviceInfo.getExpireDate());
            }
        }
        if (list_7 != null && list_7.size() > 0) {
            //发送消息
            for (ServiceInfo service : list_7) {
                String siteCode = service.getSiteCode();
                String accountId = service.getAccountId();
                String productId = service.getProductId();
                msgUtil.sendServiceExpireRemain(token, template.getUuid(), siteCode, accountId, productId, "7", service.getExpireDate());
            }
        }
    }
}
