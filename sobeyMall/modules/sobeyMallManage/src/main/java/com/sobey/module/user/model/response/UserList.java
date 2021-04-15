
package com.sobey.module.user.model.response;

import java.util.List;

/**
 * 查询某个角色直接绑定的用户列表
 * @author WCY
 */
public class UserList {

    private Integer page;
    private Integer pageSize;
    private List<UserResponse> resultList;
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<UserResponse> getResultList() {
        return resultList;
    }

    public void setResultList(List<UserResponse> resultList) {
        this.resultList = resultList;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
