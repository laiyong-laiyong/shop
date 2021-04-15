package com.sobey.module.role.model.response;

import java.util.List;

import com.sobey.module.role.model.request.ResourcePermissions;

/**
 * @Description 获取一个角色被赋予资源权限
 * @Author WuChenYang
 * @Since 2020/1/22 14:29
 */
public class resourceResult {

	private List<ResourcePermissions> result_list;
	private Integer page;
	private Integer size;
	private Integer total;

	public List<ResourcePermissions> getResult_list() {
		return result_list;
	}

	public void setResult_list(List<ResourcePermissions> result_list) {
		this.result_list = result_list;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
