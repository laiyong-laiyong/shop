package com.sobey.module.productservice.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.productservice.model.MetricUsage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/13 16:40
 */
@Mapper
@Repository
public interface MetricUsageMapper extends SupperMapper<MetricUsage> {

    List<MetricUsage> getCurrentMonthUsage(@Param("first") String first,
                                           @Param("last") String last,
                                           @Param("orderNos") List<String> orderNos);

    List<MetricUsage> getTotalUsages(@Param("orderNos") List<String> orderNos);

    List<MetricUsage> getUsages(@Param("orderNos") List<String> orderNos,
                                @Param("start") String start,
                                @Param("end") String end);

    List<MetricUsage> getUsagesByOrderNos(@Param("orderNos") List<String> orderNos);
}
