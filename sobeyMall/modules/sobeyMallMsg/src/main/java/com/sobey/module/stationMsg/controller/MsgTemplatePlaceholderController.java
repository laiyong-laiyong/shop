package com.sobey.module.stationMsg.controller;

import com.sobey.module.stationMsg.model.MallMsgTemplatePlaceholder;
import com.sobey.module.stationMsg.service.MallMsgTemplatePlaceholderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/10 15:54
 * 占位符相关接口
 */
@Api(description = "占位符相关接口")
@RestController
@RequestMapping("/${api.v1}/placeholder")
public class MsgTemplatePlaceholderController {

    @Autowired
    private MallMsgTemplatePlaceholderService mmtps;

    @GetMapping
    @ApiOperation(value = "占位符查询", httpMethod = "GET")
    public List<MallMsgTemplatePlaceholder> list() {

        List<MallMsgTemplatePlaceholder> list = mmtps.selectByMap(null);

        if (null == list || list.size() == 0) {
            throw new RuntimeException("未查询到数据");
        }
        list.sort((o1, o2) -> {
            int i1 = Integer.parseInt(o1.getUuid());
            int i2 = Integer.parseInt(o2.getUuid());
            return Integer.compare(i1, i2);
        });
        return list;
    }

}
