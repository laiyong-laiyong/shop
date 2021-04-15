package com.sobey.framework.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * springboot 启动创建数据库
 * @author liujiasheng 
 */
//@Configuration
//@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class StartupCreateDatabase {
	
	@Value("${spring.datasource.driver-class-name}")
	private String driver;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username ;
	@Value("${spring.datasource.password}")
	private String password ;

    @PostConstruct
    public void init() throws ClassNotFoundException, URISyntaxException, SQLException {
        // 通过 hikari 获取数据库连接信息

        Class.forName(driver);
        URI uri = new URI(url.replace("jdbc:", ""));
        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();

        String connectUrl = "jdbc:mysql://" + host + ":" + port+"?serverTimezone=Asia/Shanghai";
        try (Connection connection = DriverManager.getConnection(connectUrl, username, password);
             Statement statement = connection.createStatement()) {
            // 创建数据库
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + path.replace("/", "") + "` DEFAULT CHARACTER SET = `utf8` COLLATE `utf8_general_ci`;");
        }
    }
}

