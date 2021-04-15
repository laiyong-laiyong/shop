package com.sobey.module.stationMsg.controller;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.stationMsg.entity.Result;
import com.sobey.module.stationMsg.model.Promote;
import com.sobey.module.stationMsg.service.PromoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author WCY
 * @create 2020/10/14 10:54
 */
@Api(description = "推广类型接口")
@RestController
@RequestMapping("/${api.v1}/promote")
public class PromoteController {

    private static final Logger log = LoggerFactory.getLogger(PromoteController.class);

    @Autowired
    private PromoteService ps;

    /**
     * 新增推广类型
     * @param promote
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增推广类型",httpMethod = "POST")
    public Result add(@RequestBody Promote promote){

        String code = promote.getCode();
        String name = promote.getName();
        if (StringUtils.isBlank(code)){
            return Result.withFail("code不能为空");
        }
        if (StringUtils.isBlank(name)){
            return Result.withFail("name 不能为空");
        }
        promote.setUuid(IdWorker.get32UUID());
        promote.setCreateDate(new Date());
        try {
            ps.insert(promote);
        } catch (Exception e) {
            log.error("新增推广类型失败",e);
            if (StringUtils.isNotBlank(e.getMessage()) && StringUtils.containsIgnoreCase(e.getMessage(),"duplicate")){
                return Result.withFail("code重复");
            }
            return Result.withFail("新增失败");
        }
        return Result.withSuccess("新增成功");
    }

    /**
     * 更新数据
     * @param promote
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新数据",httpMethod = "PUT")
    public Result update(@RequestBody Promote promote){
        if (StringUtils.isBlank(promote.getUuid())){
            return Result.withFail("uuid不存在");
        }
        promote.setCreateDate(null);
        try {
            ps.updateById(promote);
        } catch (Exception e) {
            log.error("更新推广类型失败",e);
            if (StringUtils.isNotBlank(e.getMessage()) && StringUtils.containsIgnoreCase(e.getMessage(),"duplicate")){
                return Result.withFail("code重复");
            }
            return Result.withFail("更新失败");
        }
        return Result.withSuccess("更新成功");
    }

    /**
     * 查询推广
     * @return
     */
    @GetMapping
    @ApiOperation(value = "查询推广",httpMethod = "GET")
    @PassToken
    public List<Promote> query(@RequestParam(required = false) String name){
        Map<String,Object> map = null;
        if (StringUtils.isNotBlank(name)){
            map = new HashMap<>();
            map.put("name",name);
        }
        List<Promote> promotes = ps.selectByMap(map);
        if (null == promotes){
            promotes = new ArrayList<>();
        }
        return promotes;
    }

}
