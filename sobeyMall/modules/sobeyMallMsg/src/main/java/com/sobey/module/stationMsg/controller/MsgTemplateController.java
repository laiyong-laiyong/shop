package com.sobey.module.stationMsg.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.stationMsg.entity.Result;
import com.sobey.module.stationMsg.model.MallMsgTemplate;
import com.sobey.module.stationMsg.service.MallMsgTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author WCY
 * @CreateTime 2020/4/16 15:02
 * 模板相关接口
 */
@RestController
@Api(description = "模板相关接口")
@RequestMapping("/${api.v1}/template")
public class MsgTemplateController {

    @Autowired
    private MallMsgTemplateService mmts;

    @PostMapping
    @ApiOperation(value = "新增模板", httpMethod = "POST")
    public Result insert(@RequestBody MallMsgTemplate template) {
        Result fail = Result.withFail("");
        String msgTemplate = template.getMsgTemplate();
        if (StringUtils.isBlank(msgTemplate)) {
            fail.setMsg("模板内容不能为空");
            return fail;
        }
        template.setUuid(IdWorker.get32UUID());
        int affectRow = mmts.conditionInsert(template);
        if (affectRow == 0) {
            fail.setMsg("已存在相同类型的模板");
            return fail;
        }
        return Result.withSuccess("新增成功");
    }

    /**
     * 列表查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pages")
    @ApiOperation(value = "模板列表查询", httpMethod = "GET")
    public Page<MallMsgTemplate> pages(@RequestParam int pageNum,
                                       @RequestParam int pageSize) {
        Page<MallMsgTemplate> page = new Page<>(pageNum, pageSize);
        return mmts.pages(page);
    }

    @PostMapping("/list")
    @ApiOperation(value = "列表查询",hidden = true)
    public List<MallMsgTemplate> list(@RequestBody Map<String,Object> map){

        return mmts.selectByMap(map);

    }

    @PutMapping
    @ApiOperation(value = "更新模板")
    public Result update(@RequestBody MallMsgTemplate template) {

        Result fail = Result.withFail("");
        if (StringUtils.isBlank(template.getUuid())) {
            fail.setMsg("uuid不能为空");
            return fail;
        }
        template.setParentTypeCode(null);
        template.setMsgTypeCode(null);
        template.setUpdateTime(new Date());
        mmts.updateById(template);
        return Result.withSuccess("更新成功");
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除模板(支持批量)", httpMethod = "POST")
    public Result delete(@RequestBody List<String> uuids) {
        Result fail = Result.withFail("");
        if (null == uuids || uuids.size() == 0) {
            fail.setMsg("参数为空");
            return fail;
        }
        mmts.deleteBatchIds(uuids);
        return Result.withSuccess("删除成功");
    }

}

