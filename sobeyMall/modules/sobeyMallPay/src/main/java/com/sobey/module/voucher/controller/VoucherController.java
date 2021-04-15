package com.sobey.module.voucher.controller;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.common.response.ResultInfo;
import com.sobey.module.common.util.DateUtil;
import com.sobey.module.common.util.VoucherUtil;
import com.sobey.module.fegin.serviceInfo.service.ServiceInfoService;
import com.sobey.module.voucher.entity.CreateVouParam;
import com.sobey.module.voucher.enumeration.ReceiveStatus;
import com.sobey.module.voucher.enumeration.VoucherType;
import com.sobey.module.voucher.model.Voucher;
import com.sobey.module.voucher.model.VoucherAccount;
import com.sobey.module.voucher.mq.producer.VoucherExpireProducer;
import com.sobey.module.voucher.service.VoucherAccountService;
import com.sobey.module.voucher.service.VoucherService;
import com.sobey.util.business.header.HeaderUtil;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author wcy
 * 代金券相关操作
 */
@RestController
@RequestMapping("/${api.v1}/voucher")
@Api(description = "代金券相关接口")
public class VoucherController {

    private static final Logger log = LoggerFactory.getLogger(VoucherController.class);

    @Autowired
    private VoucherUtil voucherUtil;
    @Autowired
    private VoucherService vs;
    @Autowired
    private VoucherAccountService vas;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private VoucherExpireProducer producer;

    /**
     * 创建代金券
     *
     * @param list
     * @param request
     * @return
     */
    @PostMapping
    @ApiOperation(value = "创建代金券", httpMethod = "POST")
    public ResultInfo create(@RequestBody List<CreateVouParam> list,
                             @RequestParam String loginName,
                             @RequestParam String userCode,
                             HttpServletRequest request) {
        ResultInfo result = VoucherUtil.validateCreateVouParam(list);
        try {
            if ("FAIL".equalsIgnoreCase(result.getRt_code())) {
                return result;
            }
            String token = request.getHeader("Authorization");
            //批量生成代金券
            List<Voucher> vouchers = new ArrayList<>();
            Date now = new Date();
            //计算过期时间 默认30天过期
            Date expireTime = DateUtils.addDays(now, 30);
            //主键集合
            List<String> uuids = new ArrayList<>();
            for (CreateVouParam vouParam : list) {
                Voucher voucher = new Voucher();
                String uuid = IdWorker.get32UUID();
                uuids.add(uuid);

                voucher.setUuid(uuid);
                voucher.setReceiveStatus(ReceiveStatus.No.getCode());
                BeanUtils.copyProperties(vouParam, voucher);
                voucher.setAmount(vouParam.getAmount().setScale(2, RoundingMode.DOWN));
                voucher.setCreateName(loginName);
                String voucherCode = voucherUtil.getVouCodeMd5(voucher.getAmount(), userCode);
                voucher.setVouCode(voucherCode);
                voucher.setCreateTime(now);
                voucher.setExpireTime(expireTime);
                vouchers.add(voucher);
            }
            vs.batchSave(token, vouchers);
            //过期倒计时
            producer.produce(uuids);
            result.setRt_code("SUCCESS");
            result.setRt_msg("代金券已生成");
        } catch (Exception e) {
            log.info("创建代金券异常:", e);
            result.setRt_code("FAIL");
            result.setRt_msg("系统异常");
        }
        return result;
    }

    /**
     * 查询代金券类别
     *
     * @return
     */
    @GetMapping("/voucherTypes")
    @ApiOperation(value = "查询代金券类别", httpMethod = "GET")
    public List<Map<String, String>> queryTypes() {
        List<Map<String, String>> list = new ArrayList<>();
        for (VoucherType type : VoucherType.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("key", type.getCode());
            map.put("name", type.getDesc());
            list.add(map);
        }
        return list;
    }

    /**
     * 充值代金券
     *
     * @param vouCode
     * @param accountId
     * @param account
     * @return
     */
    @PostMapping("/recharge")
    @ApiOperation(value = "充值代金券", httpMethod = "POST")
    public ResultInfo rechargeVoucher(@RequestParam @ApiParam(value = "代金券兑换码") String vouCode,
                                      @RequestParam @ApiParam(value = "用户编码") String accountId,
                                      @RequestParam @ApiParam(value = "用户名") String account) {

        ResultInfo result = new ResultInfo();
        if (StringUtils.isBlank(vouCode) || StringUtils.isBlank(accountId)) {
            result.setRt_code("FAIL");
            result.setRt_msg("vouCode和accountId不能为空");
            return result;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("vouCode", vouCode);
        List<Voucher> vouchers = vs.selectByMap(map);
        if (null == vouchers || vouchers.size() != 1) {
            result.setRt_code("FAIL");
            result.setRt_msg("兑换码错误");
            return result;
        }
        Voucher voucher = vouchers.get(0);
        if (ReceiveStatus.Yes.getCode().equalsIgnoreCase(voucher.getReceiveStatus())) {
            result.setRt_code("FAIL");
            result.setRt_msg("已领取过");
            return result;
        }
        if (ReceiveStatus.Expire.getCode().equals(voucher.getReceiveStatus())) {
            result.setRt_code("FAIL");
            result.setRt_msg("代金券已过期");
            return result;
        }
        voucher.setAccount(account);
        voucher.setAccountId(accountId);
        voucher.setUpdateTime(new Date());
        voucher.setReceiveStatus(ReceiveStatus.Yes.getCode());
        vs.updateVou(voucher);
        try {
            sis.finishRecharge(HeaderUtil.getAuth(), true, accountId);
        } catch (Exception e) {
            log.info("重新开通服务发生异常:", e);
        }
        result.setRt_code("SUCCESS");
        result.setRt_msg("代金券充值成功");
        return result;

    }

    /**
     * 代金券同步
     *
     * @param voucherAccount
     * @return
     */
    @PostMapping("/sync")
    @ApiOperation(value = "代金券账户同步")
    public ResultInfo syncVoucherAcc(@RequestBody VoucherAccount voucherAccount) {
        ResultInfo result = new ResultInfo();
        if (null == voucherAccount) {
            result.setRt_code("FAIL");
            result.setRt_msg("参数不能为空");
            return result;
        }
        if (StringUtils.isBlank(voucherAccount.getAccountId())){
            result.setRt_code("FAIL");
            result.setRt_msg("accountId不能为空");
            return result;
        }
        if (StringUtils.isBlank(voucherAccount.getSiteCode())){
            result.setRt_code("FAIL");
            result.setRt_msg("siteCode不能为空");
            return result;
        }
        
        //C只能有一个代金券账户
        Wrapper<VoucherAccount> wp = new EntityWrapper<VoucherAccount>(voucherAccount);
    	List<VoucherAccount> list = this.vas.selectList(wp);
    	if (CollUtil.isNotEmpty(list)) {
    		result.setRt_code("FAIL");
    		result.setRt_msg("该用户的代金券账户已经存在,请勿重复同步");
			return result;
		}else {
			voucherAccount.setUuid(IdWorker.get32UUID());
			voucherAccount.setVouAmount(BigDecimal.valueOf(0.00));
			voucherAccount.setCreateTime(new Date());
			vas.insert(voucherAccount);
			result.setRt_code("SUCCESS");
			result.setRt_msg("同步成功");
			return result;
		}
    	
    }

    /**
     * 代金券查询
     *
     * @param pageNum
     * @param pageSize
     * @param voucher
     * @return
     */
    @PostMapping("/pages")
    @ApiOperation(value = "代金券列表查询", httpMethod = "POST")
    public Page<Voucher> pages(@RequestParam @ApiParam(value = "页码") int pageNum,
                               @RequestParam @ApiParam(value = "每页条数") int pageSize,
                               @RequestBody(required = false) Voucher voucher) {

        Page<Voucher> pages = new Page<>(pageNum, pageSize);

        return vs.pages(pages, voucher);
    }

    /**
     * 导出代金券excel
     */
    @GetMapping("/exportExcel")
    @ApiOperation(value = "导出代金券列表为excel", httpMethod = "GET")
    public void exportExl(HttpServletResponse response) {
        List<Voucher> list = vs.selectByMap(null);
        if (null != list && list.size() > 0) {
            try {
                String filename = "VoucherList-" + IdWorker.getId() + ".xls";
                response.setHeader("Content-Disposition", "attachment;filename=" + filename);
                response.setContentType("application/vnd.ms-excel;Charset=UTF-8");
                ServletOutputStream outputStream = response.getOutputStream();
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("sheet");
                int rowIndex = 0;
                int maxColumn = 0;//列数
                //创建标题行
                HSSFRow headRow = sheet.createRow(0);
                Field[] fields = Voucher.class.getDeclaredFields();
                for (Field field : fields) {
                    ExcelProperty property = field.getAnnotation(ExcelProperty.class);
                    if (null != property) {
                        maxColumn = Integer.max(maxColumn, property.index());
                        sheet.setColumnWidth(property.index(),20*256);
                        HSSFCell cell = headRow.createCell(property.index(), CellType.STRING);
                        cell.setCellValue(property.value()[0]);
                    }
                }
                for (Voucher voucher : list) {
                    rowIndex++;
                    HSSFRow row = sheet.createRow(rowIndex);
                    for (int i = 0; i <= maxColumn; i++) {
                        HSSFCell cell = row.createCell(i, CellType.STRING);
                        for (Field field : fields) {
                            ExcelProperty property = field.getAnnotation(ExcelProperty.class);
                            if (null != property && i == property.index()) {
                                field.setAccessible(true);
                                Object val = field.get(voucher);
                                if (val == null || StringUtils.isBlank(val.toString())) {
                                    cell.setCellValue("");
                                    break;
                                }
                                if (field.getType().getTypeName().equals(Date.class.getTypeName())) {
                                    cell.setCellValue(DateUtil.format((Date) val, DateUtil.FORMAT_1));
                                    break;
                                }
                                cell.setCellValue(val.toString());
                                break;
                            }
                        }
                    }
                }
                workbook.write(outputStream);
                workbook.close();
            } catch (Exception e) {
                log.info("导出excel异常:", e);
            }
        }
    }

}
