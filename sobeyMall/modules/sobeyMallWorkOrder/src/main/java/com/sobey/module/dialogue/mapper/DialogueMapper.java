package com.sobey.module.dialogue.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.dialogue.model.Dialogue;

@Mapper
@Repository
public interface DialogueMapper extends SupperMapper<Dialogue> {


	List<Dialogue> page(Page<Dialogue> page, Dialogue entity);
	List<Dialogue> list(Dialogue entity);
	List<HashMap<String, Object>> statistic();


}
