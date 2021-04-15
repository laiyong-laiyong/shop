package com.sobey.module.role.model.response;

import java.util.List;

import com.sobey.module.role.model.request.ResourcePermissions;

/**
 * @Description 获取一个角色被赋予资源权限
 * @Author WuChenYang
 * @Since 2020/1/22 14:29
 */
public class resourceResultV3 {

	private List<ResourcePermissions> results;
	private Integer page;
	private Integer size;
	private Integer total;
	
	/**
	 * @return the results
	 */
	public List<ResourcePermissions> getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(List<ResourcePermissions> results) {
		this.results = results;
	}
	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
	}
	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}
	/**
	 * @return the total
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}

	

}
