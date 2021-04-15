package com.sobey.module.productservice.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.productservice.mapper.ConsumeFailMsgMapper;
import com.sobey.module.productservice.model.ConsumeFailMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/19 14:22
 */
@Service
public class ConsumeFailMsgService extends ServiceImpl<ConsumeFailMsgMapper, ConsumeFailMsg> {

    @Autowired
    private ConsumeFailMsgMapper cfm;

    public Page<ConsumeFailMsg> page(Page<ConsumeFailMsg> page, ConsumeFailMsg consumeFailMsg) {
        List<ConsumeFailMsg> records = cfm.page(page,consumeFailMsg);
        page.setRecords(records);
        return page;
    }
}
