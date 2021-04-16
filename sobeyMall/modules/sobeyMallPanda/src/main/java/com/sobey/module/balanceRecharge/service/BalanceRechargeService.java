package com.sobey.module.balanceRecharge.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.balanceRecharge.Util.RSAUtil;
import com.sobey.module.balanceRecharge.common.ResultInfo;
import com.sobey.module.balanceRecharge.mapper.BalanceRechargeContractMapper;
import com.sobey.module.balanceRecharge.mapper.BalanceRechargeMapper;
import com.sobey.module.balanceRecharge.model.BalanceRecharge;
import com.sobey.module.balanceRecharge.model.BalanceRechargeContract;
import com.sobey.module.balanceRecharge.model.response.Result;
import com.sobey.module.balanceRecharge.model.response.UserInfo;
import com.sobey.module.token.fegin.AuthService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.util.*;


//import com.sobey.module.balance.entity.RechargeBalanceParam;

@Service
public class BalanceRechargeService extends ServiceImpl<BalanceRechargeMapper, BalanceRecharge> {

    @Autowired
    private BalanceRechargeContractMapper brcm;

    @Autowired
    private AuthService authService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${key-pair.public}")
    private String pubKey;

    @Value("${address.beike}")
    private String address;

    @Value("${address.pay}")
    private String payAddress;

    @Value("${token}")
    private String header;

    //token获取
    private String token() {
        JSONObject jsonObject = new JSONObject(authService.getToken());
        String access_token = jsonObject.getStr("access_token");
        return access_token;
    }

    //贝壳确认接口
    private String bkConfirm(BalanceRecharge br) {
        String result = HttpRequest.put(address + "/api/lclould_recharge_confirm?lcloud_project=" + br.getProjectId() + "&lcloud_account=" + br.getLingYunAc() + "&is_lcloud_confirm=1" + "&shell_id=" + br.getShellId())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .execute().body();
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getStr("data");
    }

    //贝壳审批接口
    private String bkApproval(BalanceRecharge br, int t) {
        String url;
        if (t == 1) {
            url = address + "/api/lclould_discount_approve?lcloud_project=" + br.getProjectId() + "&lcloud_account=" + br.getLingYunAc() + "&is_lcloud_leader_approve=" + t + "&shell_id=" + br.getShellId();
        } else {
            url = address + "/api/lclould_discount_approve?lcloud_project=" + br.getProjectId() + "&lcloud_account=" + br.getLingYunAc() + "&is_lcloud_leader_approve=" + t + "&lcloud_opinion=" + br.getBackMs() + "&shell_id=" + br.getShellId();
        }
        String result = HttpRequest.put(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .execute().body();
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getStr("data");
    }

    //添加信息
    public ResultInfo insertMs(BalanceRecharge balanceRecharge, String header) {
        ResultInfo resultInfo = new ResultInfo();

        if (header.equals(this.header)) {
            String token = token();
            BalanceRechargeContract brc = new BalanceRechargeContract();
            BalanceRecharge br = new BalanceRecharge();

            br.setAccount(balanceRecharge.getAccount());
            br.setShellId(balanceRecharge.getShellId());
            br.setCreateDate(Convert.toDate(balanceRecharge.getCreateDate()));
            br.setProjectId(balanceRecharge.getProjectId());
            br.setProjectName(balanceRecharge.getProjectName());
            br.setSeller(balanceRecharge.getSeller());
            br.setPreSale(balanceRecharge.getPreSale());
            br.setWay(balanceRecharge.getWay());
            br.setLingYunAc(balanceRecharge.getLingYunAc());

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("sobeycloud-token", token);
            String login_name = balanceRecharge.getLingYunAc();
            String result = HttpUtil.createGet("https://authentication.sobeylingyun.com/v3.0/cross-tenant/user/list?keyword=" + login_name)
                    .addHeaders(headers)
                    .execute()
                    .body();
            Result result1 = new JSONObject(result).toBean(Result.class);
            for (int i = 0; i < result1.getResults().size(); i++) {
                UserInfo userInfo = result1.getResults().get(i);
                String loginName = userInfo.getLogin_name();
                if (login_name.equals(loginName)) {
                    String userCode = userInfo.getUser_code();
                    br.setLingYunId(userCode);
                }
            }
            if (br.getLingYunId() == null) {
                resultInfo.setRt_code("FAIL");
                resultInfo.setRt_msg("该账户不存在userCode");
                return resultInfo;
            }
            br.setAmount(Convert.toBigDecimal(balanceRecharge.getAmount()));
            br.setRecharge(balanceRecharge.getRecharge());
            br.setCheckFlag(0);
            br.setCheckName("");
            this.insert(br);
            if (balanceRecharge.getService() != null) {
                List<BalanceRechargeContract> list = balanceRecharge.getService();
                for (int i = 0; i < list.size(); i++) {
                    BalanceRechargeContract balanceRechargeContract = list.get(i);
                    brc.setProjectId(br.getId());
                    brc.setService(balanceRechargeContract.getService());
                    brc.setServiceSize(balanceRechargeContract.getServiceSize());
                    if (balanceRechargeContract.getContract() != null) {
                        brc.setContract(balanceRechargeContract.getContract());
                    } else {
                        brc.setContract(null);
                    }
                    if (balanceRechargeContract.getContractDate() != null) {
                        brc.setContractDate(Convert.toDate(balanceRechargeContract.getContractDate()));
                    } else {
                        brc.setContractDate(null);
                    }
                    brcm.insert(brc);
                }
            }
            resultInfo.setRt_code("SUCCESS");
            resultInfo.setRt_msg("成功");
            return resultInfo;
        } else {
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("token值错误");
            return resultInfo;
        }
    }

    //显示
    public Page<BalanceRecharge> page(Page<BalanceRecharge> page, BalanceRecharge balanceRecharge) {
        List<BalanceRecharge> brs = this.baseMapper.page(page, balanceRecharge);
        for (BalanceRecharge br : brs) {
            if (brcm.list(br.getId()) != null) {
                br.setService(brcm.list(br.getId()));
            }
        }
        page.setRecords(brs);
        return page;
    }

    //支付核查 //确认
    public ResultInfo getOne(BalanceRecharge balanceRecharge) {
        ResultInfo resultInfo = new ResultInfo();
        if (balanceRecharge == null) {
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("参数为空");
            return resultInfo;
        }
        String token = token();

        if (this.selectById(balanceRecharge.getId()) == null) {
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("该订单编号不存在");
            return resultInfo;
        }
        if (balanceRecharge.getRecharge() == 0 && balanceRecharge.getCheckFlag() == 1) {
            String result = finish(balanceRecharge, token);
            String[] list = result.split(",");
            String[] list1 = list[0].split(":");
            String[] list2 = list[1].split(":");
            if (list1[1].equals("\"FAIL\"")) {
                balanceRecharge.setCheckFlag(2);
                balanceRecharge.setCheckMs(list2[1]);
                this.updateById(balanceRecharge);
                resultInfo.setRt_code("FAIL");
                resultInfo.setRt_msg("错误原因：" + list2[1]);
                return resultInfo;
            } else {
                this.updateById(balanceRecharge);
                //更新操作人
                BalanceRecharge brBe = this.baseMapper.getOne(balanceRecharge.getId());
                String beikeResult = bkConfirm(brBe);
                resultInfo.setRt_code("SUCCESS");
                resultInfo.setRt_msg("支付审查成功    " + "确认反馈:" + beikeResult);
                return resultInfo;
            }
        }
        //额度
        else if (balanceRecharge.getRecharge() == 1 && balanceRecharge.getCheckFlag() == 1) {
            return quota(balanceRecharge);
        }
        //手动
        else if (balanceRecharge.getCheckFlag() == 3) {
            return hand(balanceRecharge);
        }
        //退回
        else if (balanceRecharge.getCheckFlag() == 4) {
            return back(balanceRecharge);
        } else {
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("error CheckFlag");
            return resultInfo;
        }
    }

    //额度核查  //审批
    public ResultInfo quota(BalanceRecharge balanceRecharge) {
        ResultInfo resultInfo = new ResultInfo();
        if (balanceRecharge == null) {
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("参数为空");
            return resultInfo;
        }

        this.updateById(balanceRecharge);

        BalanceRecharge brBe = this.baseMapper.getOne(balanceRecharge.getId());
        String beikeResult = bkApproval(brBe, 1);
        resultInfo.setRt_code("SUCCESS");
        resultInfo.setRt_msg("额度审查成功    " + "审批反馈:" + beikeResult);
        return resultInfo;
    }

    //手动处理  //确认
    public ResultInfo hand(BalanceRecharge balanceRecharge) {
        ResultInfo resultInfo = new ResultInfo();
        if (balanceRecharge == null) {
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("参数为空");
            return resultInfo;
        }

        balanceRecharge.setCheckMs("");
        this.updateById(balanceRecharge);

        BalanceRecharge brBe = this.baseMapper.getOne(balanceRecharge.getId());
        String beikeResult = bkConfirm(brBe);
        resultInfo.setRt_code("SUCCESS");
        resultInfo.setRt_msg("手动处理成功    " + "确认反馈:" + beikeResult);
        return resultInfo;
    }

    //退回     //审批
    public ResultInfo back(BalanceRecharge get) {
        ResultInfo resultInfo = new ResultInfo();
        if (get == null) {
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("参数为空");
            return resultInfo;
        }
        if (get.getRecharge() == 1) {

            this.updateById(get);

            BalanceRecharge bre = this.baseMapper.getOne(get.getId());
            String beikeResult = bkApproval(bre, 0);
            resultInfo.setRt_code("SUCCESS");
            resultInfo.setRt_msg("退回成功    " + "审批反馈:" + beikeResult);
            return resultInfo;
        } else {
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("充值不允许退回");
            return resultInfo;
        }
    }

    //充值
    public String finish(BalanceRecharge br, String token) {

        JSONObject jb;
        HttpResponse result;
        String sign;

        BalanceRecharge balanceRecharge = this.selectById(br.getId());
        try {
            sign = RSAUtil.encryptByPub(Convert.toStr(br.getAmount()), pubKey);
            jb = JSONUtil.createObj()
                    .putOnce("account", balanceRecharge.getLingYunAc())
                    .putOnce("accountId", balanceRecharge.getLingYunId())
                    .putOnce("amount", br.getAmount())
                    .putOnce("sign", sign)
                    .putOnce("operationName", br.getCheckName());
            result = HttpRequest
                    .post("http://" + payAddress + "/sobeyMallPay/V1/balance/recharge")
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .body(jb.toString())
                    .execute();
            return result.body();
        } catch (Exception e) {
            log.error(e.toString(),e);
        }
        return "FAIL";
    }

    //下载
    public void download(HttpServletRequest request, HttpServletResponse response, String language, BalanceRecharge balanceRecharge) throws IOException {
        //相应类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;Charset=UTF-8");
        // 设置响应头信息
        response.setHeader("Content-Disposition",
                "attachment;filename=rechargeInfo.xlsx");
        ExcelWriter writer = ExcelUtil.getWriter(true);
        ServletOutputStream out = null;
        switch (language) {
            //中文
            case "cn":
                writer.write(cnData(balanceRecharge), true);
                break;
            //英文
            case "en":
                writer.write(enData(balanceRecharge), true);
                break;
            //其他语言类型默认写中文
            default:
                writer.write(cnData(balanceRecharge), true);
                break;
        }
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error(e.toString(),e);
        } finally {
            writer.close();
        }
        IoUtil.close(out);
    }

    //导出英文格式数据
    private ArrayList<Map<String, Object>> enData(BalanceRecharge balanceRecharge) {
        List<BalanceRecharge> list = this.baseMapper.list(balanceRecharge);
        for (BalanceRecharge br : list) {
            if (brcm.list(br.getId()) != null) {
                br.setService(brcm.list(br.getId()));
            }
        }

        ArrayList<Map<String, Object>> maps = CollUtil.newArrayList();
        StringBuffer buffer = new StringBuffer();
        if (CollectionUtil.isNotEmpty(list)) {
            for (BalanceRecharge br : list) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", br.getId());
                map.put("shell_id", br.getShellId());
                map.put("account", br.getAccount());
                map.put("createDate", DateUtil.date(br.getCreateDate()));
                map.put("projectId", br.getProjectId());
                map.put("projectName", br.getProjectName());
                map.put("way", br.getWay());
                map.put("lingYunAc", br.getLingYunAc());
                map.put("lingYunId", br.getLingYunId());
                for (BalanceRechargeContract brc : br.getService()) {
                    String string = brc.getService() + ";" + brc.getServiceSize() + ";"
                            + brc.getContract();
                    if (brc.getContractDate() == null) {
                        string += "\n";
                    } else string += DateUtil.date(brc.getContractDate()) + "\n";
                    buffer.append(string);
                }
                map.put("service", buffer.toString());
                map.put("seller", br.getSeller());
                map.put("preSale", br.getPreSale());
                map.put("amount", br.getAmount());
                map.put("recharge", br.getRecharge());
                map.put("checkFlag", br.getCheckFlag());
                map.put("checkMs", br.getCheckMs());
                map.put("checkName", br.getCheckName());
                map.put("backMs", br.getBackMs());
                maps.add(map);
                buffer.setLength(0);
            }
        }
        return maps;
    }

    //导出中文格式数据
    private ArrayList<Map<String, Object>> cnData(BalanceRecharge balanceRecharge) {
        List<BalanceRecharge> list = this.baseMapper.list(balanceRecharge);
        for (BalanceRecharge br : list) {
            if (brcm.list(br.getId()) != null) {
                br.setService(brcm.list(br.getId()));
            }
        }

        ArrayList<Map<String, Object>> maps = CollUtil.newArrayList();
        StringBuffer buffer = new StringBuffer();
        if (CollectionUtil.isNotEmpty(list)) {
            for (BalanceRecharge br : list) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", br.getId());
                map.put("申请编号", br.getShellId());
                map.put("客户名", br.getAccount());
                map.put("创建时间", DateUtil.date(br.getCreateDate()));
                map.put("项目编号", br.getProjectId());
                map.put("项目名", br.getProjectName());
                map.put("提成方式", br.getWay());
                map.put("凌云账号", br.getLingYunAc());
                map.put("凌云id", br.getLingYunId());
                for (BalanceRechargeContract brc : br.getService()) {
                    String string = brc.getService() + ";" + brc.getServiceSize() + ";"
                            + brc.getContract();
                    if (brc.getContractDate() == null) {
                        string += "\n";
                    } else string += DateUtil.date(brc.getContractDate()) + "\n";
                    buffer.append(string);
                }
                map.put("购买服务", buffer.toString());
                map.put("销售人员", br.getSeller());
                map.put("售前人员", br.getPreSale());
                map.put("金额", br.getAmount());
                map.put("支付方式(0充值/1额度)", br.getRecharge());
                map.put("审核状态(0待确认/1已完成/2失败/3手动处理/4退回)", br.getCheckFlag());
                map.put("失败原因", br.getCheckMs());
                map.put("操作账号", br.getCheckName());
                map.put("退回原因", br.getBackMs());
                maps.add(map);
                buffer.setLength(0);
            }
        }
        return maps;
    }

    public void getTotal() {

    }
}
