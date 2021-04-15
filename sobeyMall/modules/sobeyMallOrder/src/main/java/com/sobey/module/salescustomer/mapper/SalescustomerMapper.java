package com.sobey.module.salescustomer.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.mybatisPlus.SupperMapper;
import com.sobey.module.salescustomer.model.Salescustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SalescustomerMapper extends SupperMapper<Salescustomer> {

	List<Salescustomer> page(Page<Salescustomer> page, Salescustomer ct);

    List<String> customerUserCodes(@Param("saleUserCode") String saleUserCode);
}
