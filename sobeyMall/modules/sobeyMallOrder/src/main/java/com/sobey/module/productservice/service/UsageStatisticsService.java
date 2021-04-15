package com.sobey.module.productservice.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.productservice.mapper.UsageStatisticsMapper;
import com.sobey.module.productservice.model.UsageStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/6/12 11:36
 */
@Service
public class UsageStatisticsService extends ServiceImpl<UsageStatisticsMapper, UsageStatistics> {

    @Autowired
    private UsageStatisticsMapper usm;

    public List<UsageStatistics> getUsages(String appId, String start, String end) {
        return usm.getUsages(appId,start,end);
    }

    public List<UsageStatistics> getCurrentMonthUsage(String first, String last, String appId) {
        return usm.getCurrentMonthUsage(first,last,appId);
    }

    public List<UsageStatistics> getTotalUsages(String appId) {
        return usm.getTotalUsages(appId);
    }
}
