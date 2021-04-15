package com.sobey.module.favorite.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.favorite.model.Favorite;

@Mapper
@Repository
public interface FavoriteMapper extends SupperMapper<Favorite> {

	List<Favorite> page(Page<Favorite> page, Favorite ct);
	List<Favorite> list(Favorite entity);

}
