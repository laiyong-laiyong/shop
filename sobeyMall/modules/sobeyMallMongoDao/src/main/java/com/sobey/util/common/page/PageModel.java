package com.sobey.util.common.page;

import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * @Description 分页模型
 * @Author WuChenYang
 * @CreateTime 2020/2/19 16:29
 */
public class PageModel implements Serializable {

    private static final long serialVersionUID = 1L;
    // 当前页
    private Integer pagenumber = 1;
    // 当前页面条数
    private Integer pagesize = 10;
    // 排序条件
    private Sort sort;

    public Integer getPagenumber() {
        return pagenumber;
    }

    public PageModel setPagenumber(Integer pagenumber) {
        if (pagenumber <= 0) {
            return this;
        }
        this.pagenumber = pagenumber;
        return this;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public PageModel setPagesize(Integer pagesize) {
        if (pagesize <= 0) {
            return this;
        }
        this.pagesize = pagesize;
        return this;
    }

    public Sort getSort() {
        return sort;
    }

    public PageModel setSort(Sort sort) {
        this.sort = sort;
        return this;
    }


}
