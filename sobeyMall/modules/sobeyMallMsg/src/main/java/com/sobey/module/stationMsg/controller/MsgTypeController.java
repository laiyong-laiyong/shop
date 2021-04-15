package com.sobey.module.stationMsg.controller;

import com.sobey.module.stationMsg.model.MallMsgBasicType;
import com.sobey.module.stationMsg.model.MallMsgSubType;
import com.sobey.module.stationMsg.service.MallMsgBasicTypeService;
import com.sobey.module.stationMsg.service.MallMsgSubTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author WCY
 * @CreateTime 2020/4/13 11:39
 * 消息类型相关接口
 */
@RestController
@RequestMapping("/${api.v1}/msgType")
@Api(description = "消息类型接口")
public class MsgTypeController {

    @Autowired
    private MallMsgSubTypeService mmsts;
    @Autowired
    private MallMsgBasicTypeService mmbts;

    @GetMapping("/list/sub")
    @ApiOperation(value = "根据基础消息类型获取子消息类型", httpMethod = "GET")
    public List<MallMsgSubType> msgSubTypes(@RequestParam @ApiParam(value = "基础消息类型uuid(若不传则获取所有)") String uuid) {

        Map<String, Object> map = new HashMap<>();
        map.put("basicUuid", uuid);
        return mmsts.selectByMap(map);
    }

    @GetMapping("/list/basic")
    @ApiOperation(value = "获取基础消息类型", httpMethod = "GET")
    public List<MallMsgBasicType> msgSubTypes() {

        return mmbts.selectByMap(null);
    }

}
