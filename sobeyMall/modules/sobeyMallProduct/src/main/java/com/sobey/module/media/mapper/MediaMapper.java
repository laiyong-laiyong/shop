package com.sobey.module.media.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.media.model.Media;

@Mapper
@Repository
public interface MediaMapper extends SupperMapper<Media> {

	List<Media> page(Page<Media> page, Media ct);
	List<Media> list(Media entity);
	void remove(Media entity);

}
