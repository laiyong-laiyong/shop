package com.sobey.util.common.page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * @Description 分页公共类
 * @Author WuChenYang
 * @CreateTime 2020/2/19 17:01
 */
public class SobeyPageable implements Serializable, Pageable {

    private static final long serialVersionUID = 1L;

    private PageModel page;

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }

    @Override
    public int getPageNumber() {
        return page.getPagenumber();
    }

    @Override
    public int getPageSize() {
        return page.getPagesize();
    }

    @Override
    public long getOffset() {
        return (page.getPagenumber()-1)*page.getPagesize();
    }

    @Override
    public Sort getSort() {
        return page.getSort();
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
