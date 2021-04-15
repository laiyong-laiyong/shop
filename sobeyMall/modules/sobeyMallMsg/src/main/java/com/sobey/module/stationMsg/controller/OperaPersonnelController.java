package com.sobey.module.stationMsg.controller;

import com.sobey.framework.jwt.model.Token;
import com.sobey.framework.jwt.model.UserInfo;
import com.sobey.module.stationMsg.entity.Result;
import com.sobey.module.stationMsg.enumeration.IsOnDuty;
import com.sobey.module.stationMsg.model.OperationsPersonnel;
import com.sobey.module.stationMsg.service.OperaPersonnelService;
import com.sobey.util.common.json.JsonKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/7/9 14:41
 */
@RestController
@RequestMapping("/${api.v1}/operaPersonnel")
@Api(description = "运维人员值班安排相关接口")
public class OperaPersonnelController {

    private static final Logger log = LoggerFactory.getLogger(OperaPersonnelController.class);

    @Autowired
    private OperaPersonnelService ops;

    /**
     * 创建运维值班人员
     *
     * @param personnel
     * @return
     */
    @PostMapping
    @ApiOperation(value = "创建运维值班人员", httpMethod = "POST")
    public Result add(@RequestBody OperationsPersonnel personnel) {
        try {
            Field[] fields = personnel.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                if (null == annotation) {
                    continue;
                }
                if (annotation.required()) {
                    Object val = field.get(personnel);
                    if (null == val || StringUtils.isBlank(val.toString())) {
                        return Result.withFail("参数" + field.getName() + "不能为空");
                    }
                }
            }
            personnel.setIsOnDuty("1");
            ops.insert(personnel);
            return Result.withSuccess("创建成功");
        } catch (Exception e) {
            log.info("新增运维名单异常:", e);
            return Result.withFail("系统异常");
        }

    }

    /**
     * 删除一条数据
     * @param uuid
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除一条数据", httpMethod = "DELETE")
    public Result del(@RequestParam String uuid) {
        boolean b = ops.deleteById(uuid);
        return b ? Result.withSuccess("删除成功") : Result.withFail("删除失败");
    }

    /**
     * 修改值班状态
     * @param uuid
     * @param isOnDuty
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改值班状态",httpMethod = "PUT")
    public Result update(@RequestParam String uuid,
                         @RequestParam String isOnDuty){

        if (!IsOnDuty.contains(isOnDuty)){
            return Result.withFail("未知的值班状态");
        }

        OperationsPersonnel personnel = new OperationsPersonnel();
        personnel.setUuid(uuid);
        personnel.setIsOnDuty(isOnDuty);
        boolean b = ops.updateById(personnel);
        return b ? Result.withSuccess("修改成功") : Result.withFail("修改失败");
    }

    /**
     * 查询值班人员列表
     * @param isOnDuty
     * @return
     */
    @GetMapping
    @ApiOperation(value = "查询值班人员列表",httpMethod = "GET")
    public List<OperationsPersonnel> list(@RequestParam(required = false) @ApiParam(value = "值班状态") String isOnDuty){
        if (StringUtils.isBlank(isOnDuty)){
            Map<String,Object> map = new HashMap<>();
            map.put("isOnDuty",isOnDuty);
            return ops.selectByMap(map);
        }
        return ops.selectByMap(null);
    }

    private String decodeToken(String token){
        try {
            Jwt jwt = JwtHelper.decode(token);
            Token bean = JsonKit.jsonToBean(jwt.getClaims(), Token.class);
            if (null == bean){
                return "未知";
            }
            UserInfo user_info = bean.getUser_info();
            return user_info.getLogin_name();
        } catch (Exception e) {
            log.info("token解析异常:",e);
            return "未知";
        }
    }

}
