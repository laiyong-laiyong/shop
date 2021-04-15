package com.sobey.module.stationMsg.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.stationMsg.mapper.MallMsgBroadcastMapper;
import com.sobey.module.stationMsg.model.MallMsgBroadcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/7 18:46
 */
@Service
public class MallMsgBroadcastService extends ServiceImpl<MallMsgBroadcastMapper, MallMsgBroadcast> {

    @Autowired
    private MallMsgBroadcastMapper mmbm;

    public void deleteMsg(List<String> uuids){
        mmbm.deleteMsg(uuids);
    }

    public Page<MallMsgBroadcast> page(Page<MallMsgBroadcast> page){
        List<MallMsgBroadcast> list = mmbm.page(page);
        page.setRecords(list);
        return page;
    }
}
