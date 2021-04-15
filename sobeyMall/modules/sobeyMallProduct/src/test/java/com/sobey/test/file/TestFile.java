package com.sobey.test.file;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sobey.module.metric.enumeration.MetricType;
import com.sobey.util.common.json.JsonKit;


public class TestFile {

	@Test
	public void test() {
//		String data = FileUtil.read("/redisson/singleServer.json");
//		System.out.println(data);
		MetricType[] values = MetricType.values();
		List<MetricType> list = new ArrayList<MetricType>();
		for (MetricType item : values) {
			list.add(item);
		}
		System.out.println(list);
	}
	
	
	@Test
	public void test2() {
		 String authUrl = "https://auth.sobeylingyun.com/v1.0";
	     authUrl = authUrl.substring(0, authUrl.lastIndexOf("/")) + "/oauth/token";
	     System.out.println(authUrl);
	        
	}
	
	
	
	
	

}
