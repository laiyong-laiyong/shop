package com.sobey.module.workOrdercategory.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.workOrdercategory.mapper.CategoryMapper;
import com.sobey.module.workOrdercategory.model.Category;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

	@Autowired
	private CategoryMapper ctMp;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<Category> page(Page<Category> page, Category ct) {
		List<Category> cts = ctMp.page(page, ct);
		page.setRecords(cts);
		return page;
	}

}
