/**
 * 
 */
package com.sobey.module.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sobey.framework.config.Server;

/**
 * @author lgc
 * @date 2020年1月22日 上午10:13:21
 */
@Component
public class UrlComponent {

	@Autowired
	private Server server;
	private String http = "http://";

	public String getDomain() {

		String domain = http + server.getAddress() + ":" + server.getPort()
				+ server.getServlet().getContextPath();
		return domain;
	}

	// + server.getServlet().getContextPath();

}
