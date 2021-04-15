package com.sobey.test.file;

import static org.junit.Assert.*;

import java.util.Date;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;


public class TestFile {

	@Test
	public void test() {
//		String data = FileUtil.read("/redisson/singleServer.json");
//		System.out.println(data);
		
		System.out.println(DurationFormatUtils.formatPeriod(new Date("2012/05/05 08:08:08").getTime(), new Date("2013/08/07 12:12:12").getTime(), "y'年'-M'月'-d'日' H:m:s")); 
	}
	
	
	

}
