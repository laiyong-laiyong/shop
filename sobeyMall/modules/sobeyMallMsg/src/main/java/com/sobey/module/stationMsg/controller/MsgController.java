package com.sobey.module.stationMsg.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.stationMsg.entity.MsgEntity;
import com.sobey.module.stationMsg.entity.Result;
import com.sobey.module.stationMsg.entity.WorkOrderMsg;
import com.sobey.module.stationMsg.enumeration.*;
import com.sobey.module.stationMsg.model.MallMsg;
import com.sobey.module.stationMsg.model.MallMsgBroadcast;
import com.sobey.module.stationMsg.model.MallMsgProclamation;
import com.sobey.module.stationMsg.model.MallMsgTemplate;
import com.sobey.module.stationMsg.mq.producer.WorkOrderOnlineMsgProducer;
import com.sobey.module.stationMsg.service.MallMsgBroadcastService;
import com.sobey.module.stationMsg.service.MallMsgProclamationService;
import com.sobey.module.stationMsg.service.MallMsgService;
import com.sobey.module.stationMsg.service.MallMsgTemplateService;
import com.sobey.module.stationMsg.socketIo.SocketIoEventHandler;
import com.sobey.module.stationMsg.util.MsgUtil;
import com.sobey.module.stationMsg.util.TemplateReplaceUtil;
import com.sobey.util.common.file.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author WCY
 * @CreateTime 2020/4/7 10:20
 * 消息相关
 */
@RestController
@RequestMapping("/${api.v1}/message")
@Api(description = "消息相关接口")
public class MsgController {

    private static final Logger log = LoggerFactory.getLogger(MsgController.class);

    @Autowired
    private MallMsgBroadcastService mmbs;
    @Autowired
    private MsgUtil msgUtil;
    @Autowired
    private MallMsgService mms;
    @Autowired
    private SocketIoEventHandler socketIoEventHandler;
    @Autowired
    private MallMsgTemplateService mmts;
    @Autowired
    private MallMsgProclamationService mmps;
    @Autowired
    private WorkOrderOnlineMsgProducer onlineMsgProducer;

    @Value("${upload.path}")
    private String rootPath;


    /**
     * 新增广播类消息
     *
     * @param msgBroadcast
     * @return
     */
    @PostMapping("/broadcast")
    @ApiOperation(value = "新增广播类消息", httpMethod = "POST")
    public Result createBroadcastMsg(@RequestBody MallMsgBroadcast msgBroadcast) {
        Result fail = Result.withFail("");
        String msgContent = msgBroadcast.getMsgContent();
        if (StringUtils.isBlank(msgContent)) {
            fail.setMsg("消息内容不能为空");
            return fail;
        }
        if (null == msgBroadcast.getExpireTime()) {
            fail.setMsg("请设置消息有效期");
            return fail;
        }
        msgBroadcast.setCreateTime(new Date());
        msgBroadcast.setSendStatus(SendStatus.NotSent.getCode());
        msgBroadcast.setUuid(IdWorker.get32UUID());
        mmbs.insert(msgBroadcast);
        return Result.withSuccess("新增成功");
    }

    /**
     * 发送消息
     *
     * @param templateUuid
     * @param msgEntity
     * @return
     */
    @PostMapping("/system")
    @ApiOperation(value = "发送消息", httpMethod = "POST")
    public Result createSystemMsg(@RequestParam @ApiParam(value = "模板uuid") String templateUuid,
                                  @RequestBody MsgEntity msgEntity) {
        Result fail = Result.withFail("");
        if (StringUtils.isBlank(templateUuid)) {
            fail.setMsg("templateUuid不能为空");
            return fail;
        }
        String expireTime = msgEntity.getExpireTime();
        if (StringUtils.isNotBlank(expireTime)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                dateFormat.setLenient(false);
                dateFormat.parse(expireTime);
            } catch (Exception e) {
                fail.setMsg("日期格式错误");
                return fail;
            }

        }
        List<String> userCodes = msgEntity.getUserCodes();
        if (null == userCodes || userCodes.size() == 0){
            fail.setMsg("userCodes不能为空");
            return fail;
        }
        //查询消息模板
        MallMsgTemplate mallMsgTemplate = mmts.selectById(templateUuid);
        if (null == mallMsgTemplate) {
            fail.setMsg("templateUuid不正确");
            return fail;
        }
        //替换消息模板占位符
        Map<String, String> replacements = new HashMap<>();
        String content = mallMsgTemplate.getMsgTemplate();
        Field[] fields = msgEntity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object obj = field.get(msgEntity);
                if (null == obj) {
                    continue;
                }

                String name = field.getName();
                if ("userCodes".equalsIgnoreCase(name)) {
                    continue;
                }
                String value = obj.toString();
                String placeholder = "${" + name + "}";
                if ("expireTime".equalsIgnoreCase(name)) {
                    placeholder = TemplatePlaceholder.expireTime;
                }
                replacements.put(placeholder, value);
            } catch (IllegalAccessException e) {
                log.info("未获取到字段值", e);
            }
        }
        String msg = TemplateReplaceUtil.replace(content, replacements);
        ArrayList<MallMsg> mallMsgs = new ArrayList<>();
        for (String userCode : userCodes) {
            MallMsg mallMsg = new MallMsg();
            mallMsg.setUuid(IdWorker.get32UUID());
            mallMsg.setCreateTime(new Date());
            mallMsg.setMsgStatus(MsgStatus.Unread.getCode());
            mallMsg.setSubMsgType(mallMsgTemplate.getMsgTypeCode());
            mallMsg.setBasicMsgType(mallMsgTemplate.getParentTypeCode());
            mallMsg.setAccountId(userCode);
            mallMsg.setMsgContent(msg);
            mallMsgs.add(mallMsg);
        }
        msgUtil.sendMsg(mallMsgs);
        return Result.withSuccess("发送成功");
    }

    /**
     * 发送文本的消息
     * @param msgTypeCode
     * @param accountId
     * @return
     */
    @ApiOperation(value = "发送普通文本消息",httpMethod = "POST")
    @PostMapping("/sendTextMsg")
    public Result sendTextMsg(@RequestParam @ApiParam(value = "消息类型编码") String msgTypeCode,
                              @RequestParam @ApiParam(value = "用户userCode") String accountId,
                              @RequestParam @ApiParam(value = "消息内容") String msgContent){

        String basicTypeCode = MsgSubType.getBasicTypeCode(msgTypeCode);
        if (StringUtils.isBlank(basicTypeCode)){
            return Result.withFail("消息类型错误");
        }
        MallMsg msg = new MallMsg();
        msg.setUuid(IdWorker.get32UUID());
        msg.setCreateTime(new Date());
        msg.setMsgStatus(MsgStatus.Unread.getCode());
        msg.setSubMsgType(msgTypeCode);
        msg.setBasicMsgType(basicTypeCode);
        msg.setAccountId(accountId);
        msg.setMsgContent(msgContent);
        msgUtil.sendMsg(Collections.singletonList(msg));
        return Result.withSuccess("发送成功");
    }

    /**
     * 发送开通saas的消息
     * @return
     */
    @PostMapping("/sendSaasMsg")
    public Result sendMsg(@RequestBody Map<String,String> msg){
        MallMsg mallMsg = new MallMsg();
        mallMsg.setUuid(IdWorker.get32UUID());
        mallMsg.setCreateTime(new Date());
        mallMsg.setMsgStatus(MsgStatus.Unread.getCode());
        mallMsg.setSubMsgType(MsgSubType.ServiceOpenNotice.getCode());
        mallMsg.setBasicMsgType(MsgBasicType.System.getCode());
        mallMsg.setAccountId(msg.get("accountId"));
        mallMsg.setMsgContent(msg.get("msg"));
        ArrayList<MallMsg> mallMsgs = new ArrayList<>();
        mallMsgs.add(mallMsg);
        msgUtil.sendMsg(mallMsgs);
        return Result.withSuccess("发送成功");
    }

    /**
     * 更新个人消息状态为已读
     *
     * @param uuids
     * @return
     */
    @PutMapping("/system")
    @ApiOperation(value = "更新个人消息状态为已读(支持批量)", httpMethod = "PUT")
    public Result updateMsgStatus(@RequestBody @ApiParam(value = "消息id集合", example = "['1','2','3']") List<String> uuids,
                                  @RequestParam @ApiParam(value = "用户userCode") String userCode) {
        Result fail = Result.withFail("");
        if (null == uuids || uuids.size() == 0) {
            fail.setMsg("参数为空");
            return fail;
        }
        mms.updateMsgStatus(uuids, MsgStatus.HaveRead.getCode());
        //发送ws消息通知刷新消息箱
        socketIoEventHandler.sendTextMsgToPerson(userCode,"refresh");
        return Result.withSuccess("更新成功");
    }

    /**
     * 删除消息
     *
     * @param uuids
     * @return
     */
    @PostMapping("/system/delete")
    @ApiOperation(value = "删除个人消息", httpMethod = "POST")
    public Result deleteSystemMsg(@RequestBody List<String> uuids) {
        Result fail = Result.withFail("");
        if (null == uuids || uuids.size() == 0) {
            fail.setMsg("参数为空");
            return fail;
        }
        mms.deleteMsg(uuids);
        return Result.withSuccess("删除成功");
    }

    /**
     * 查询个人消息列表
     *
     * @param pageNum
     * @param pageSize
     * @param mallMsg
     * @return
     */
    @PostMapping("/system/pages")
    @ApiOperation(value = "查询个人消息列表", httpMethod = "POST")
    public Page<MallMsg> pages(@RequestParam int pageNum,
                               @RequestParam int pageSize,
                               @RequestBody(required = false) MallMsg mallMsg) {
        Page<MallMsg> page = new Page<>(pageNum, pageSize);
        return mms.pages(page, mallMsg);
    }


    /**
     * 查询广播类消息列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/broadcast/pages")
    @ApiOperation(value = "查询广播类消息列表", httpMethod = "GET")
    @PassToken
    public Page<MallMsgBroadcast> listBroadcastMsg(@RequestParam int pageNum,
                                                   @RequestParam int pageSize) {
        Page<MallMsgBroadcast> page = new Page<>(pageNum, pageSize);
        return mmbs.page(page);

    }

    /**
     * 更新广播类消息
     *
     * @param broadcast
     * @return
     */
    @PutMapping("/broadcast")
    @ApiOperation(value = "更新广播类消息", httpMethod = "PUT")
    public Result updateBroadcastMsg(@RequestBody MallMsgBroadcast broadcast) {
        Result fail = Result.withFail("");
        String uuid = broadcast.getUuid();
        if (StringUtils.isBlank(uuid)) {
            fail.setMsg("参数错误,uuid不能为空");
            return fail;
        }
        String msgContent = broadcast.getMsgContent();
        if (StringUtils.isBlank(msgContent)) {
            fail.setMsg("消息内容不能为空");
            return fail;
        }
        broadcast.setUpdateTime(new Date());
        mmbs.updateById(broadcast);
        return Result.withSuccess("更新成功");
    }

    /**
     * 推送广播消息
     *
     * @param uuid
     * @return
     */
    @PostMapping("/broadcast/push")
    @ApiOperation(value = "推送广播类消息", httpMethod = "POST")
    public Result pushBroadcastMsg(@RequestParam String uuid) {
        Result fail = Result.withFail("");
        if (StringUtils.isBlank(uuid)) {
            fail.setMsg("uuid不能为空");
            return fail;
        }
        MallMsgBroadcast mallMsgBroadcast = mmbs.selectById(uuid);
        if (null == mallMsgBroadcast) {
            fail.setMsg("未查询到数据");
            return fail;
        }
        Date expireTime = mallMsgBroadcast.getExpireTime();
        Date date = new Date();
        if (expireTime.before(date)) {
            fail.setMsg("该消息已过期");
            return fail;
        }
        if (SendStatus.Sent.getCode().equals(mallMsgBroadcast.getSendStatus())){
            fail.setMsg("请勿重复发送");
            return fail;
        }
        socketIoEventHandler.sendMsgToBroadCast(mallMsgBroadcast);
        mallMsgBroadcast.setSendStatus(SendStatus.Sent.getCode());
        mallMsgBroadcast.setUpdateTime(date);
        mallMsgBroadcast.setSentTime(date);
        mmbs.updateById(mallMsgBroadcast);
        return Result.withSuccess("发送成功");
    }

    /**
     * 删除广播类消息
     *
     * @param uuids
     * @return
     */
    @PostMapping("/broadcast/delete")
    @ApiOperation(value = "删除广播类消息", httpMethod = "POST")
    public Result deleteBroadcastMsg(@RequestBody List<String> uuids) {
        Result fail = Result.withFail("");
        if (null == uuids || uuids.size() == 0) {
            fail.setMsg("参数为空");
            return fail;
        }
        mmbs.deleteMsg(uuids);
        return Result.withSuccess("删除成功");
    }

    /**
     * 新增公告
     *
     * @param proclamation
     * @return
     */
    @PostMapping("/announcement")
    @ApiOperation(value = "新增公告", httpMethod = "POST")
    public Result add(@RequestBody MallMsgProclamation proclamation) {
        Result fail = Result.withFail("");
        if (StringUtils.isBlank(proclamation.getTitle())) {
            fail.setMsg("公告标题不能为空");
            return fail;
        }
        if (StringUtils.isBlank(proclamation.getContent())) {
            fail.setMsg("公告内容不能为空");
            return fail;
        }
        proclamation.setUuid(IdWorker.get32UUID());
        proclamation.setCreateTime(new Date());
        proclamation.setPublishStatus(ProclamationStatus.Unpublished.getCode());
        mmps.insert(proclamation);
        return Result.withSuccess("新增成功");
    }

    /**
     * 公告查询
     *
     * @param pageNum
     * @param pageSize
     * @param proclamation
     * @return
     */
    @PostMapping("/announcement/pages")
    @ApiOperation(value = "公告查询", httpMethod = "POST")
    @PassToken
    public Page<MallMsgProclamation> pages(@RequestParam int pageNum,
                                           @RequestParam int pageSize,
                                           @RequestBody(required = false) MallMsgProclamation proclamation) {

        Page<MallMsgProclamation> pages = new Page<>(pageNum, pageSize);
        return mmps.pages(pages, proclamation);
    }

    /**
     * 根据推广查询公告列表
     * @param promoteCode
     * @return
     */
    @GetMapping("/announcement/queryByPromote")
    @ApiOperation(value = "根据推广查询公告列表", httpMethod = "GET")
    @PassToken
    public List<MallMsgProclamation> queryByPromote(@RequestParam @ApiParam("推广code") String promoteCode){
        EntityWrapper<MallMsgProclamation> wrapper = new EntityWrapper<>();
        wrapper.like("promotes",promoteCode);
        return mmps.selectList(wrapper);
    }

    /**
     * 查询公告详情
     * @param uuid
     * @return
     */
    @GetMapping("/announcement/detail")
    @ApiOperation(value = "查询公告详情",httpMethod = "GET")
    @PassToken
    public MallMsgProclamation detail(@RequestParam @ApiParam(value = "主键") String uuid){
        return mmps.selectById(uuid);
    }

    /**
     * 更新公告
     *
     * @param proclamation
     * @return
     */
    @PutMapping("/announcement")
    @ApiOperation(value = "更新公告", httpMethod = "PUT")
    public Result update(@RequestBody MallMsgProclamation proclamation) {
        Result fail = Result.withFail("");
        if (StringUtils.isBlank(proclamation.getUuid())) {
            fail.setMsg("uuid不能为空");
            return fail;
        }
        proclamation.setUpdateTime(new Date());
        proclamation.setTypeDesc(MsgSubType.getDesc(proclamation.getTypeCode()));
        mmps.updateById(proclamation);
        return Result.withSuccess("更新成功");
    }

    /**
     * 发布公告
     *
     * @param uuid
     * @return
     */
    @PutMapping("/announcement/publish")
    @ApiOperation(value = "发布公告", httpMethod = "PUT")
    public Result publish(@RequestParam @ApiParam("uuid") String uuid) {
        Result fail = Result.withFail("");
        if (StringUtils.isBlank(uuid)) {
            fail.setMsg("参数为空");
            return fail;
        }
        Date date = new Date();
        MallMsgProclamation msg = mmps.selectById(uuid);
        msg.setUpdateTime(date);
        msg.setPublishTime(date);
        if (ProclamationStatus.Published.getCode().equals(msg.getPublishStatus())){
            fail.setMsg("请勿重复发布");
            return fail;
        }
        List<String> list = new ArrayList<>();
        list.add(uuid);
        int affectRow = mmps.updatePublishStatus(list, ProclamationStatus.Published.getCode(), date, date);
        if (affectRow > 0) {
            socketIoEventHandler.sendMsgToAnnouncement(msg);
            return Result.withSuccess("发布成功");
        }
        fail.setMsg("发布失败");
        return fail;
    }

    /**
     * 撤销公告
     *
     * @param uuid
     * @return
     */
    @PostMapping("/announcement/revoke")
    @ApiOperation(value = "撤销公告", httpMethod = "POST")
    public Result revoke(@RequestParam @ApiParam("uuid") String uuid) {
        Result fail = Result.withFail("");
        if (StringUtils.isBlank(uuid)) {
            fail.setMsg("参数为空");
            return fail;
        }
        Date date = new Date();
        List<String> list = new ArrayList<>();
        list.add(uuid);
        int affectRow = mmps.updatePublishStatus(list, ProclamationStatus.Revoked.getCode(), null, date);
        if (affectRow > 0) {
            socketIoEventHandler.revokeAnnouncementMsg(uuid);
            return Result.withSuccess("已撤销");
        }
        fail.setMsg("撤销失败");
        return fail;
    }

    /**
     * 删除公告
     *
     * @param uuid
     * @return
     */
    @DeleteMapping("/announcement")
    @ApiOperation(value = "删除公告", httpMethod = "DELETE")
    public Result delete(@RequestParam @ApiParam("uuid") String uuid) {
        Result fail = Result.withFail("");
        if (StringUtils.isBlank(uuid)) {
            fail.setMsg("参数为空");
            return fail;
        }
        boolean flag = mmps.deleteById(uuid);
        return Result.withSuccess("删除成功");
    }

    /**
     * 公告新增推广
     * @param uuid
     * @param promoteCode
     * @return
     */
    @PostMapping("/addPromote")
    @ApiOperation(value = "公告新增推广",httpMethod = "POST")
    public Result addPromote(@RequestParam @ApiParam("公告uuid") String uuid,
                             @RequestParam @ApiParam("推广类型code") String promoteCode){

        MallMsgProclamation proclamation = mmps.selectById(uuid);
        if (null == proclamation){
            return Result.withFail("找不到该公告！");
        }
        if (StringUtils.isBlank(promoteCode)){
            return Result.withFail("promoteCode不能为空");
        }
        //处理前端误传逗号的情况
        if (promoteCode.contains(",")){
            promoteCode = promoteCode.replace(",","");
        }
        String promotes = proclamation.getPromotes();
        if (StringUtils.isBlank(promotes)){
            proclamation.setPromotes(promoteCode);
        }
        if (StringUtils.isNotBlank(promotes)){
            promotes = promotes+","+promoteCode;
            proclamation.setPromotes(promotes);
        }
        proclamation.setUpdateTime(new Date());
        mmps.updateById(proclamation);
        return Result.withSuccess("更新成功");

    }

    /**
     * 公告删除推广
     * @param uuid
     * @param promoteCode
     * @return
     */
    @PostMapping("/delPromote")
    @ApiOperation(value = "公告删除推广",httpMethod = "POST")
    public Result delPromote(@RequestParam @ApiParam("公告uuid") String uuid,
                             @RequestParam @ApiParam("推广类型code") String promoteCode){
        MallMsgProclamation proclamation = mmps.selectById(uuid);
        if (null == proclamation){
            return Result.withFail("找不到该公告！");
        }
        if (StringUtils.isBlank(promoteCode)){
            return Result.withFail("promoteCode不能为空");
        }
        //处理前端误传逗号的情况
        if (promoteCode.contains(",")){
            promoteCode = promoteCode.replace(",","");
        }
        String promotes = proclamation.getPromotes();
        if (StringUtils.isBlank(promotes)){
            return Result.withSuccess("没有该类型推广,无需删除");
        }
        promotes = promotes.replace(promoteCode,"");
        if (StringUtils.isNotBlank(promotes)){
            if (promotes.startsWith(",")){
                promotes = promotes.substring(1);
            }
            if (promotes.endsWith(",")){
                promotes = promotes.substring(0,promotes.length()-1);
            }
            if (promotes.contains(",,")){
                promotes = promotes.replace(",,",",");
            }
        }
        proclamation.setPromotes(promotes);
        proclamation.setUpdateTime(new Date());
        mmps.updateById(proclamation);
        return Result.withSuccess("删除成功");
    }


    @PostMapping(value = "/announcement/upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "图片上传", httpMethod = "POST")
    @PassToken
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        Result fail = Result.withFail("");
        if (file.isEmpty()) {
            fail.setMsg("文件为空");
            return fail;
        }
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            fail.setMsg("文件名为空");
            return fail;
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//文件扩展名 .xxx
        String filename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) + "-" + IdWorker.getId();
        String dir = rootPath + File.separator + "announcement";
        filename = filename + suffix;
        try {
            File dest = new File(dir, filename);
            boolean flag = dest.getParentFile().mkdirs();
            file.transferTo(dest);
            String absolutePath = dest.getAbsolutePath();
            String path = absolutePath.substring(absolutePath.indexOf("sobeyMall") + "sobeyMall".length());
            return Result.withSuccess(path);
        } catch (IOException e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, "文件上传失败", e);
        }
    }

    /**
     * 修改公告图片时，删除原来的图片
     *
     * @param oldPaths
     * @return
     */
    @PostMapping("/announcement/deleteFiles")
    @ApiOperation(value = "删除废弃的图片接口", httpMethod = "POST")
    @PassToken
    public Result deleteFile(@RequestBody @ApiParam(value = "路径数组") List<String> oldPaths) {
        Result fail = Result.withFail("");
        if (oldPaths.size() == 0) {
            fail.setMsg("参数为空");
            return fail;
        }
        //获取绝对路径
        List<String> paths = oldPaths.stream().map(oldPath ->
                rootPath + oldPath.substring(oldPath.indexOf(File.separator + "media") + (File.separator + "media").length())
        ).collect(Collectors.toList());
        for (String path : paths) {
            FileUtil.forceDelete(path);
        }
        return Result.withSuccess("删除成功");
    }

    /**
     * 注册成功发送消息
     *
     * @param accountId
     * @return
     */
    @GetMapping("/welcome")
    @ApiOperation(value = "注册成功发送欢迎消息", httpMethod = "GET")
    public Result welcome(@RequestParam String accountId, @RequestParam String username) {
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(username)) {
            return Result.withFail("发送失败,参数为空");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("msgTypeCode", MsgSubType.Welcome.getCode());
        List<MallMsgTemplate> templates = mmts.selectByMap(map);
        if (null == templates || templates.size() != 1) {
            return Result.withFail("发送失败,未查询到模板");
        }
        MallMsgTemplate template = templates.get(0);
        String uuid = template.getUuid();
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setUsername(username);
        List<String> list = new ArrayList<>();
        list.add(accountId);
        msgEntity.setUserCodes(list);
        Result result = createSystemMsg(uuid, msgEntity);
        if ("FAIL".equalsIgnoreCase(result.getCode())) {
            return result;
        }
        return Result.withSuccess("发送成功");
    }

    /**
     * 工单在线聊天接口
     * @param workOrderMsg
     * @return
     */
    @PostMapping("/onlineChat")
    @ApiOperation(value = "工单在线聊天接口",httpMethod = "POST")
    public Result onlineChat(@RequestBody WorkOrderMsg workOrderMsg){
        //校验参数
        Class<? extends WorkOrderMsg> clazz = workOrderMsg.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                if (null == annotation){
                    continue;
                }
                if (annotation.required()){
                    Object o = field.get(workOrderMsg);
                    if (null == o || StringUtils.isBlank(o.toString())){
                        return Result.withFail("参数"+field.getName()+"为空");
                    }
                }
            }
        } catch (Exception e) {
            return Result.withFail("系统异常");
        }
        onlineMsgProducer.pushMsg(workOrderMsg);
        return Result.withSuccess("发送成功");
    }

    /**
     * 推送当月商品数与交易额的统计结果
     * @param map
     */
    @ApiOperation(value = "推送统计结果",hidden = true)
    @PostMapping("/pushStatistics")
    public void pushStatisticsResult(@RequestBody Map<String,Object> map){
        socketIoEventHandler.pushStatisticsResult(map);
    }

}
