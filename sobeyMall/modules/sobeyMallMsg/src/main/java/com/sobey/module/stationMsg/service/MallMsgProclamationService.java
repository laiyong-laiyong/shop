package com.sobey.module.stationMsg.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.stationMsg.mapper.MallMsgProclamationMapper;
import com.sobey.module.stationMsg.model.MallMsgProclamation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/14 14:43
 */
@Service
public class MallMsgProclamationService extends ServiceImpl<MallMsgProclamationMapper,MallMsgProclamation> {

    @Autowired
    private MallMsgProclamationMapper mmpm;

    public Page<MallMsgProclamation> pages(Page<MallMsgProclamation> pages, MallMsgProclamation proclamation) {

        List<MallMsgProclamation> list = mmpm.pages(pages,proclamation);
        pages.setRecords(list);
        return pages;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updatePublishStatus(List<String> uuids, String status, Date publishTime, Date updateTime) {
        return mmpm.updatePublishStatus(uuids,status,publishTime,updateTime);
    }
}
