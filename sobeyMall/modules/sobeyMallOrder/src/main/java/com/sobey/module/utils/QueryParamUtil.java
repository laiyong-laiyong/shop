package com.sobey.module.utils;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.util.common.regex.PatternUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @Description 创建订单的查询条件
 * @Author WuChenYang
 * @CreateTime 2020/2/19 18:12
 */
public class QueryParamUtil {

    public static Criteria getCriteria(Map<String, Object> param) {
        Criteria criteria = new Criteria();
        Object startDate = null;
        Object endDate = null;
        if (null != param && param.size() > 0) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (isBlank(value)) {
                    continue;
                }
                if ("startDate".equals(key)) {
                    startDate = value;
                    continue;
                }
                if ("endDate".equals(key)) {
                    endDate = value;
                    continue;
                }
                if ("account".equals(key)){
                    criteria.and(key).regex(PatternUtil.obscure(value.toString()));
                    continue;
                }
                criteria.and(key).is(value.toString());
            }
        }
        if (null != startDate || null != endDate) {
            Date start = null;
            Date end = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            try {
                if (null == startDate) {
                    end = dateFormat.parse(endDate.toString());
                    criteria.and("createDate").lt(end);
                    return criteria;
                }
                if (null == endDate) {
                    start = dateFormat.parse(startDate.toString());
                    criteria.and("createDate").gte(start);
                    return criteria;
                }
                start = dateFormat.parse(startDate.toString());
                end = dateFormat.parse(endDate.toString());
                criteria.andOperator(
                        Criteria.where("createDate").gte(start),
                        Criteria.where("createDate").lt(end));
            } catch (ParseException e) {
                throw new AppException(ExceptionType.SYS_PARAMETER,"日期格式错误",e);
            }
        }
        return criteria;
    }

    public static boolean isBlank(Object obj) {
        if (null == obj || StringUtils.isBlank(obj.toString())) {
            return true;
        }
        return false;
    }

}
