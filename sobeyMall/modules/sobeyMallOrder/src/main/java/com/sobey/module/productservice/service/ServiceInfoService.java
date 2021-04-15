package com.sobey.module.productservice.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.productservice.mapper.ServiceInfoMapper;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.util.business.header.HeaderUtil;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/13 16:41
 */
@Service
public class ServiceInfoService extends ServiceImpl<ServiceInfoMapper, ServiceInfo> {

    @Autowired
    private ServiceInfoMapper sim;
    @Autowired
    private ProductService ps;

    public Page<ServiceInfo> page(Page<ServiceInfo> page,ServiceInfo param){

        List<ServiceInfo> list = sim.page(page, param);
        /**
         * C这里需要查询是否是关联商品
         */
        if(CollUtil.isNotEmpty(list)) {
        	for (ServiceInfo item : list) {
				if (item != null) {
					String productId = item.getProductId();
					JSONArray array = ps.queryProduct(HeaderUtil.getAuth(), productId);
					if (array != null && array.size() >=1) {
						JSONObject product = array.getJSONObject(0);
						String enble = product.getString("enableRelation");
						item.setEnableRelation(enble);
					}
				}
			}
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 判断是否有已经开通或开通中的服务
     * @param accountId
     * @param productId
     * @return
     */
    public List<ServiceInfo> isOpened(@NotNull String accountId, @NotNull String productId){
        return sim.isOpened(accountId, productId);
    }

    /**
     * 根据appId查询
     * @param appId
     * @return
     */
    public ServiceInfo findByAppId(String appId){
        return sim.findByAppId(appId);
    }

    /**
     * 根据给定的到期时间去查询服务,例如如果查询还有一天到期就会查询服务到期时间大于等于一天小于两天的服务
     * @param start
     * @param end
     * @return
     */
    public List<ServiceInfo> remainNotice(Date start,Date end){
        return sim.remainNotice(start,end);
    }

    /**
     * 扫描到期的服务
     * @param expireTime
     * @return
     */
    public List<ServiceInfo> findExpiredService(Date expireTime,String serviceStatus){
        return sim.findExpiredService(expireTime,serviceStatus);
    }

    /**
     * 实时统计当月开通的商品数
     * @param date
     * @param serviceStatus 服务状态不是开通失败的
     * @return
     */
    public int collectServiceNum(Date date,String serviceStatus){
        return sim.collectServiceNum(date,serviceStatus);
    }

    /**
     * 查询关联商品是否开通或开通过
     * @param siteCode
     * @param productId
     * @param serviceStatuses
     * @return
     */
    public List<ServiceInfo> relatedProducts(String siteCode, String productId,String... serviceStatuses) {
        return sim.relatedProducts(siteCode,productId,serviceStatuses);
    }
}
