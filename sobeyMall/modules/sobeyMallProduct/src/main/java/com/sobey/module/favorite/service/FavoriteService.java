package com.sobey.module.favorite.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.exception.AppException;
import com.sobey.module.favorite.mapper.FavoriteMapper;
import com.sobey.module.favorite.model.Favorite;
import com.sobey.module.version.model.Version;
import com.sobey.module.versionCustom.model.VersionCustom;
import com.sobey.module.versionCustomOption.model.VersionCustomOption;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class FavoriteService extends ServiceImpl<FavoriteMapper, Favorite> {

	public Page<Favorite> page(Page<Favorite> page, Favorite entity) {

		List<Favorite> cts = this.baseMapper.page(page, entity);
		page.setRecords(cts);
		return page;
	}

	public List<Favorite> list(Favorite entity) {

		List<Favorite> pts = this.baseMapper.list(entity);
		return pts;
	}
	
	public void save(Favorite entity) {
		
		List<Favorite> list = this.baseMapper.list(entity);
		if (CollectionUtils.isNotEmpty(list)) {
			//已经收藏过此商品,直接返回
			return;
		}else {
			this.insert(entity);
		}
	}

	public void remove(Favorite entity) {
		Wrapper<Favorite> wrapper = new EntityWrapper<Favorite>(entity);
		this.delete(wrapper);
	}

}
