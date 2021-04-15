package com.sobey.module.productservice.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.productservice.model.ConsumeFailMsg;
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
public interface ConsumeFailMsgMapper extends SupperMapper<ConsumeFailMsg> {


    List<ConsumeFailMsg> page(@Param("page") Page<ConsumeFailMsg> page,
                              @Param("failMsg") ConsumeFailMsg consumeFailMsg);
}
