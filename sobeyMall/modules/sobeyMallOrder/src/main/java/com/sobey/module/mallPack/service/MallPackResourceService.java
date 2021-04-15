package com.sobey.module.mallPack.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.mallPack.mapper.MallPackResourceMapper;
import com.sobey.module.mallPack.model.MallPackResource;
import com.sobey.module.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/5/20 11:39
 */
@Service
public class MallPackResourceService extends ServiceImpl<MallPackResourceMapper, MallPackResource> {

    @Autowired
    private MallPackResourceMapper mprm;

    /**
     * 手动分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param resourceName 资源名称查询条件
     * @param mallPackIds  套餐包表mall_pack主键
     * @return
     */
    public List<MallPackResource> manualPages(int pageNum,
                                              int pageSize,
                                              String resourceName,
                                              List<String> mallPackIds) {
        int startIndex = (pageNum - 1) * pageSize;
        return mprm.manualPages(startIndex, pageSize, resourceName, mallPackIds);
    }


    public int count(String resourceName, List<String> mallPackIds) {
        return mprm.count(resourceName, mallPackIds);
    }

    /**
     * 查询某个用户购买的所有有效的套餐包资源
     *
     * @param accountId
     * @param productId
     * @return
     */
//    public List<MallPackResource> queryEffective(String accountId, String productId) {
//        Date now = new Date();
//        return mprm.queryEffective(accountId,productId, DateUtil.format(now,DateUtil.FORMAT_1));
//    }

    /**
     * 统计有效的套餐包资源,按metricId分组统计
     * @param accountId
     * @param productId
     * @return
     */
    public List<MallPackResource> statisticEffective(String accountId,String productId){
        Date now = new Date();
        return mprm.statisticEffective(accountId,productId,DateUtil.format(now,DateUtil.FORMAT_1));
    }

    /**
     * 查询某个用户购买的特定单价metricId有效的套餐包资源
     *
     * @param accountId
     * @param productId
     * @return
     */
    public List<MallPackResource> queryEffective(String accountId, String productId,String metricId) {
        Date now = new Date();
        return mprm.queryEffectiveByMetricId(accountId,productId, metricId,DateUtil.format(now,DateUtil.FORMAT_1));
    }
}
