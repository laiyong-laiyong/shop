package com.sobey.module.salescustomer.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.exception.AppException;
import com.sobey.module.salescustomer.mapper.SalescustomerMapper;
import com.sobey.module.salescustomer.model.Salescustomer;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class SalescustomerService extends ServiceImpl<SalescustomerMapper, Salescustomer> {

	public Page<Salescustomer> page(Page<Salescustomer> page, Salescustomer entity) {

		List<Salescustomer> cts = this.baseMapper.page(page, entity);
		page.setRecords(cts);
		return page;
	}
	
	public  void check(String userCode,String loginName) {
		
		EntityWrapper<Salescustomer> wp = new EntityWrapper<Salescustomer>();
		wp.eq("customerUserCode", userCode);
		List<Salescustomer> list = this.baseMapper.selectList(wp);
		if (CollectionUtils.isNotEmpty(list)) {
			throw new AppException("客户："+loginName+"已经被绑定");
		}
		
	}

}
