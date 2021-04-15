package com.sobey.util.common.page;

import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/2/19 18:22
 */
public class SortUtil {

    /**
     * 获取排序类
     * @param orders
     * @return
     */
    public static Sort getSort(List<Sort.Order> orders){

        return Sort.by(orders);

    }

}
