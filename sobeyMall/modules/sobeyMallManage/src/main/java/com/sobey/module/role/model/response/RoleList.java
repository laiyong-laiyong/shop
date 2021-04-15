
package com.sobey.module.role.model.response;

import java.util.List;

/**
 * @Description 资源权限查询角色列表
 * @Author WuChenYang
 * @Since 2020/1/22 11:20
 */
public class RoleList {

	private Integer page;
	private Integer page_size;
	private List<Result> result_list;
	private Integer size;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPage_size() {
		return page_size;
	}

	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}

	public List<Result> getResult_list() {
		return result_list;
	}

	public void setResult_list(List<Result> result_list) {
		this.result_list = result_list;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
