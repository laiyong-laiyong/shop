package com.sobey.module.productservice.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.productservice.model.UsageStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/6/12 11:36
 */
@Repository
@Mapper
public interface UsageStatisticsMapper extends SupperMapper<UsageStatistics> {
    List<UsageStatistics> getUsages(@Param("appId") String appId,
                                    @Param("start") String start,
                                    @Param("end") String end);

    List<UsageStatistics> getCurrentMonthUsage(@Param("first") String first,
                                               @Param("last") String last,
                                               @Param("appId") String appId);

    List<UsageStatistics> getTotalUsages(@Param("appId") String appId);
}
