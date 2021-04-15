package com.sobey.module.stationMsg.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.stationMsg.model.MallMsgTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author WCY
 * @CreateTime 2020/4/3 15:55
 */
@Mapper
@Repository
public interface MallMsgTemplateMapper extends SupperMapper<MallMsgTemplate> {
    int conditionInsert(MallMsgTemplate template);
}
