package com.sobey.module.voucher.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.common.util.MsgUtil;
import com.sobey.module.voucher.mapper.VoucherAccountMapper;
import com.sobey.module.voucher.mapper.VoucherMapper;
import com.sobey.module.voucher.model.Voucher;
import com.sobey.module.voucher.model.VoucherAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoucherService extends ServiceImpl<VoucherMapper, Voucher> {

    @Autowired
    private VoucherMapper vm;
    @Autowired
    private VoucherAccountMapper vam;
    @Autowired
    private MsgUtil msgUtil;

    @Transactional(rollbackFor = Exception.class)
    public void updateVou(Voucher voucher){
        String accountId = voucher.getAccountId();
        Map<String,Object> map = new HashMap<>();
        map.put("accountId",accountId);
        List<VoucherAccount> voucherAccounts = vam.selectByMap(map);
        if (null == voucherAccounts || voucherAccounts.size() == 0){
            throw new AppException(ExceptionType.SYS_PARAMETER,"账户"+accountId+"不存在",new RuntimeException());
        }
        VoucherAccount voucherAccount = voucherAccounts.get(0);
        double amount = voucherAccount.getVouAmount().doubleValue();
        voucherAccount.setVouAmount(BigDecimal.valueOf(amount+voucher.getAmount().doubleValue()));
        voucherAccount.setUpdateTime(voucher.getUpdateTime());
        vm.updateById(voucher);
        vam.updateById(voucherAccount);
    }

    public Page<Voucher> pages(Page<Voucher> page,Voucher voucher){

        List<Voucher> list = vm.pages(page, voucher);
        return page.setRecords(list);

    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(String token, List<Voucher> vouchers) {
        vm.batchSave(vouchers);
        //发送消息
        msgUtil.sendVoucherCreateNotice(token,vouchers);

    }
}
