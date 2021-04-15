
package com.sobey.module.role.model.response;

import java.util.Map;

public class Client {

	private String authorities;
	private String client_id;
	private Map<String, Object> extend;
	private String home_page_uri;
	private Long id;
	private Object logout_uri;
	private String name;
	private String redirect_uri;
	private String secret;
	private Boolean shown;
	private Integer weight;

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Boolean getShown() {
		return shown;
	}

	public void setShown(Boolean shown) {
		this.shown = shown;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getHome_page_uri() {
		return home_page_uri;
	}

	public void setHome_page_uri(String home_page_uri) {
		this.home_page_uri = home_page_uri;
	}

	public Object getLogout_uri() {
		return logout_uri;
	}

	public void setLogout_uri(Object logout_uri) {
		this.logout_uri = logout_uri;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

}
