package com.sobey.module.stationMsg.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.stationMsg.mapper.MallMsgTemplateMapper;
import com.sobey.module.stationMsg.model.MallMsgTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/3 15:57
 */
@Service
public class MallMsgTemplateService extends ServiceImpl<MallMsgTemplateMapper, MallMsgTemplate> {

    @Autowired
    private MallMsgTemplateMapper mmtm;

    public int conditionInsert(MallMsgTemplate template) {
        return mmtm.conditionInsert(template);
    }

    public Page<MallMsgTemplate> pages(Page<MallMsgTemplate> page) {
        List<MallMsgTemplate> list = mmtm.selectByMap(null);
        page.setRecords(list);
        return page;
    }
}
