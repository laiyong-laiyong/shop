package com.sobey.module.stationMsg.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.stationMsg.mapper.MallMsgMapper;
import com.sobey.module.stationMsg.model.MallMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/3 17:50
 */
@Service
public class MallMsgService extends ServiceImpl<MallMsgMapper, MallMsg> {

    @Autowired
    private MallMsgMapper mmm;

    @Transactional(rollbackFor = Exception.class)
    public void updateMsgStatus(List<String> uuids,String msgStatus){
        mmm.updateMsgStatus(uuids,msgStatus);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMsg(List<String> uuids){
        mmm.deleteMsg(uuids);
    }

    public Page<MallMsg> pages(Page<MallMsg> page, MallMsg mallMsg) {

        List<MallMsg> list = mmm.pages(page,mallMsg);
        page.setRecords(list);
        return page;
    }
}
