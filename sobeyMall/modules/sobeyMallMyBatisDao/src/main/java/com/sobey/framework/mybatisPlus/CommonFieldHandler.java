package com.sobey.framework.mybatisPlus;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 公共字段自动填充
 * 
 * @author lgc
 * @date 2020年1月19日 下午4:39:10
 */
@Component
public class CommonFieldHandler extends MetaObjectHandler {

	/**
	 * mybatis-plus版本2.0.9+
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		setFieldValByName("createDate", new Date(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 更新填充
		setFieldValByName("updateDate", new Date(), metaObject);
	}
}