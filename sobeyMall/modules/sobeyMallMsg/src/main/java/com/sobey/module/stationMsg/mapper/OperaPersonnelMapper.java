package com.sobey.module.stationMsg.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.stationMsg.model.OperationsPersonnel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WCY
 * @createTime 2020/7/9 14:42
 */
@Repository
@Mapper
public interface OperaPersonnelMapper extends SupperMapper<OperationsPersonnel> {
}
