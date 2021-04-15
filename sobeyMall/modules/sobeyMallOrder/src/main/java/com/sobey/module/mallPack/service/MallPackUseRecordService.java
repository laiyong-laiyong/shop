package com.sobey.module.mallPack.service;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.mallPack.model.MallPackUseRecord;
import com.sobey.module.utils.DateUtil;
import com.sobey.module.utils.QueryParamUtil;
import com.sobey.util.common.page.PageModel;
import com.sobey.util.common.page.SobeyPageable;
import com.sobey.util.common.page.SortUtil;
import com.sobey.util.common.regex.PatternUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/5/22 16:32
 * 套餐使用记录
 */
@Service
public class MallPackUseRecordService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${mongo.collection.mall_pack_use_record}")
    private String mall_pack_use_record;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 使用明细分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param param    packName-套餐包名称 resourceName-资源名 consumptionDate-抵扣日期 yyyy-MM-dd
     * @return
     */
    public Page<MallPackUseRecord> pages(int pageNum, int pageSize, Map<String, Object> param) {
        Query query = new Query();
        query.addCriteria(handleQueryParam(param));
        PageModel page = new PageModel();
        SobeyPageable pageable = new SobeyPageable();
        //对时间降序排序
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "consumptionDate"));
        Sort sort = SortUtil.getSort(orders);

        page.setPagenumber(pageNum);
        page.setPagesize(pageSize);
        page.setSort(sort);
        pageable.setPage(page);
        //查询出一共多少条
        long total = mongoTemplate.count(query, MallPackUseRecord.class, mall_pack_use_record);
        //查询
        List<MallPackUseRecord> list = mongoTemplate.find(query.with(pageable), MallPackUseRecord.class, mall_pack_use_record);
        //封装
//        Page<MallPackOrder> pages = new PageImpl<>(list, pageable, total);

        return new PageImpl<>(list, pageable, total);
    }

    /**
     * 查询使用明细列表
     *
     * @param param
     * @return
     */
    public List<MallPackUseRecord> list(Map<String, Object> param) {
        Query query = new Query(handleQueryParam(param));
        return mongoTemplate.find(query, MallPackUseRecord.class, mall_pack_use_record);
    }

    /**
     * 新增使用明细
     *
     * @param mallPackUseRecord
     */
    public void insert(MallPackUseRecord mallPackUseRecord) {
        mongoTemplate.insert(mallPackUseRecord,mall_pack_use_record);
    }

    public Criteria handleQueryParam(Map<String, Object> param) {
        if (null == param || param.size() == 0) {
            return new Criteria();
        }
        Object packName = param.remove("packName");
        Object resourceName = param.remove("resourceName");
        Object consumptionDate = param.remove("consumptionDate");
        Criteria criteria = QueryParamUtil.getCriteria(param);
        if (!QueryParamUtil.isBlank(packName)) {
            criteria.and("packName").regex(PatternUtil.obscure(packName.toString()));
        }
        if (!QueryParamUtil.isBlank(resourceName)) {
            criteria.and("resourceName").regex(PatternUtil.obscure(resourceName.toString()));
        }
        if (!QueryParamUtil.isBlank(consumptionDate)) {
            try {
                Date start = DateUtil.parse(consumptionDate.toString(), DateUtil.FORMAT_4);
                Date end = DateUtils.addSeconds(DateUtils.addMinutes(DateUtils.addHours(start, 23), 59), 59);
                criteria.andOperator(
                        Criteria.where("consumptionDate").gte(start),
                        Criteria.where("consumptionDate").lte(end));
            } catch (ParseException e) {
                throw new AppException(ExceptionType.SYS_PARAMETER, "日期格式错误", e);
            }
        }
        return criteria;
    }

}
