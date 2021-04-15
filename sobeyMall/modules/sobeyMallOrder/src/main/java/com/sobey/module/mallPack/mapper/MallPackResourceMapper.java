package com.sobey.module.mallPack.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.mallPack.model.MallPackResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/5/20 11:34
 */
@Mapper
@Repository
public interface MallPackResourceMapper extends SupperMapper<MallPackResource> {
    void batchSave(@Param("resources") List<MallPackResource> resources);

    int count(@Param("resName") String resourceName,
              @Param("ids") List<String> mallPackIds);

    List<MallPackResource> manualPages(@Param("startIndex") int startIndex,
                                       @Param("pageSize") int pageSize,
                                       @Param("resName") String resourceName,
                                       @Param("ids") List<String> mallPackIds);

//    List<MallPackResource> queryEffective(@Param("accountId") String accountId,
//                                          @Param("productId") String productId,
//                                          @Param("nowDate") String nowDate);

    List<MallPackResource> queryEffectiveByMetricId(@Param("accountId") String accountId,
                                                    @Param("productId") String productId,
                                                    @Param("metricId") String metricId,
                                                    @Param("nowDate") String nowDate);

    List<MallPackResource> statisticEffective(@Param("accountId") String accountId,
                                              @Param("productId") String productId,
                                              @Param("nowDate") String nowDate);
}
