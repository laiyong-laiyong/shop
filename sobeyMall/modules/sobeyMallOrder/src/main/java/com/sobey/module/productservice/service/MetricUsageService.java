package com.sobey.module.productservice.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.productservice.mapper.MetricUsageMapper;
import com.sobey.module.productservice.model.MetricUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/25 15:18
 */
@Service
public class MetricUsageService extends ServiceImpl<MetricUsageMapper, MetricUsage> {

    @Autowired
    private MetricUsageMapper mum;

    public List<MetricUsage> getUsages(List<String> orderNoList,String start,String end){

        return mum.getUsages(orderNoList,start,end);

    }

    public List<MetricUsage> getCurrentMonthUsage(String first, String last, List<String> orderNos) {
        return mum.getCurrentMonthUsage(first,last,orderNos);
    }

    public List<MetricUsage> getTotalUsages(List<String> orderNoList) {
        return mum.getTotalUsages(orderNoList);
    }

    public List<MetricUsage> getUsagesByOrderNos(List<String> orderNos){
        return mum.getUsagesByOrderNos(orderNos);
    }
}
