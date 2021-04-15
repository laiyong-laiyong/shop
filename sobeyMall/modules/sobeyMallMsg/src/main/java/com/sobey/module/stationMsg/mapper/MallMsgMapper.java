package com.sobey.module.stationMsg.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.stationMsg.model.MallMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/3 15:55
 */
@Mapper
@Repository
public interface MallMsgMapper extends SupperMapper<MallMsg> {

    void updateMsgStatus(@Param("uuids") List<String> uuids,
                    @Param("msgStatus") String msgStatus);

    void deleteMsg(@Param("uuids") List<String> uuids);

    List<MallMsg> pages(Page<MallMsg> page, MallMsg mallMsg);
}
