package com.sobey.module.stationMsg.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.stationMsg.model.MallMsgProclamation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/14 14:41
 */
@Mapper
@Repository
public interface MallMsgProclamationMapper extends SupperMapper<MallMsgProclamation> {
    List<MallMsgProclamation> pages(@Param("pages") Page<MallMsgProclamation> pages,
                                    @Param("entity") MallMsgProclamation proclamation);

    int updatePublishStatus(@Param("uuids") List<String> uuids,
                            @Param("status")String code,
                            @Param("publishTime") Date publishTime,
                            @Param("updateTime")Date updateTime);
}
