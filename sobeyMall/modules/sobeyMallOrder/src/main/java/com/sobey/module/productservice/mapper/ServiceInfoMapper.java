package com.sobey.module.productservice.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.productservice.model.ServiceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/13 16:40
 */
@Mapper
@Repository
public interface ServiceInfoMapper extends SupperMapper<ServiceInfo> {

    List<ServiceInfo> page(Page<ServiceInfo> page,ServiceInfo param);

    List<ServiceInfo> isOpened(@Param("accountId") String accountId,
                               @Param("productId") String productId);

    List<ServiceInfo> remainNotice(@Param("start") Date start,
                                   @Param("end") Date end);

    List<ServiceInfo> findExpiredService(@Param("expireTime") Date expireTime,
                                         @Param("serviceStatus") String serviceStatus);

    int collectServiceNum(@Param("date") Date date,
                          @Param("serviceStatus") String serviceStatus);

    ServiceInfo findByAppId(@Param("appId") String appId);

    List<ServiceInfo> relatedProducts(@Param("siteCode") String siteCode,
                                      @Param("productId") String productId,
                                      @Param("serviceStatuses") String... serviceStatuses);
}
