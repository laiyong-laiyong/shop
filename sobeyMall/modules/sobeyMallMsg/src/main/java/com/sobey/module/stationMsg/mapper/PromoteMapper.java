package com.sobey.module.stationMsg.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.stationMsg.model.Promote;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WCY
 * @create 2020/10/14 10:21
 */
@Mapper
@Repository
public interface PromoteMapper extends SupperMapper<Promote> {

}
