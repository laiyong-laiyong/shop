package com.sobey.module.productservice.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.productservice.mapper.PublishFailMsgMapper;
import com.sobey.module.productservice.model.PublishFailMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/19 14:22
 */
@Service
public class PublishFailMsgService extends ServiceImpl<PublishFailMsgMapper, PublishFailMsg> {
    @Autowired
    private PublishFailMsgMapper pfmm;
    public Page<PublishFailMsg> page(Page<PublishFailMsg> page, PublishFailMsg publishFailMsg) {
        List<PublishFailMsg> records = pfmm.page(page,publishFailMsg);
        page.setRecords(records);
        return page;
    }
}
