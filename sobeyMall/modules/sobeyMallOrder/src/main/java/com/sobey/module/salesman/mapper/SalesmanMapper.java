package com.sobey.module.salesman.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.salesman.model.Salesman;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SalesmanMapper extends SupperMapper<Salesman> {

	List<Salesman> page(Page<Salesman> page, Salesman ct);

    List<Salesman> selectByNames(Page<Salesman> page,
                                 @Param("loginName") String loginName,
                                 @Param("name") String name);
}
