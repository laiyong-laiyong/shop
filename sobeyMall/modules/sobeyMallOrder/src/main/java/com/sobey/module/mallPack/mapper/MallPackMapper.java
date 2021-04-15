package com.sobey.module.mallPack.mapper;

import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.mallPack.model.MallPack;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/5/20 11:31
 */
@Mapper
@Repository
public interface MallPackMapper extends SupperMapper<MallPack> {

    List<MallPack> list(MallPack mallPack);
}
