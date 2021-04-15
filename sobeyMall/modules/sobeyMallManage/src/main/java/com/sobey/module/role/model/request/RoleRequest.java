package com.sobey.module.role.model.request;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @Since 2020/1/22 10:47
 */

public class RoleRequest {

	private Long id;// 角色主键
	private String code;// 全局code,用于更新和删除用
	private String name;// 显示名
	private String site_code;// 所属站点
	private String description;
	private Boolean disabled; // 是否被禁 1表示禁用，0表示启用
	private String client_code;// 所属系统
	private Map<String, Object> extend;// 角色扩展信息

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	private Date create_time;// 创建时间

	private List<String> client_codes;// 能访问的系统
	private List<String> client_names;// 能访问的系统的名字

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

	public String getSite_code() {
		return site_code;
	}

	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}

	public String getClient_code() {
		return client_code;
	}

	public void setClient_code(String client_code) {
		this.client_code = client_code;
	}

	public List<String> getClient_codes() {
		return client_codes;
	}

	public void setClient_codes(List<String> client_codes) {
		this.client_codes = client_codes;
	}

	public List<String> getClient_names() {
		return client_names;
	}

	public void setClient_names(List<String> client_names) {
		this.client_names = client_names;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
