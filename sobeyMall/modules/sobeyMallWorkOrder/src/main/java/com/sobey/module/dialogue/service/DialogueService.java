package com.sobey.module.dialogue.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.dialogue.mapper.DialogueMapper;
import com.sobey.module.dialogue.model.Dialogue;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class DialogueService extends ServiceImpl<DialogueMapper, Dialogue> {


	private Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<Dialogue> page(Page<Dialogue> page, Dialogue entity) {

		List<Dialogue> cts = this.baseMapper.page(page, entity);
		page.setRecords(cts);
		return page;
	}

	public List<Dialogue> list(Dialogue entity) {

		List<Dialogue> pts = this.baseMapper.list(entity);
		return pts;
	}



}
