package com.sobey.test.file;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

public class TestFile {

	@Test
	public void test() {
		DateTime lastMonth = DateUtil.lastMonth();
		String format = DateUtil.format(lastMonth, DatePattern.NORM_DATE_PATTERN);
		System.out.println(format);
		
		for (int i = 1; i <= 53; i++) {
			String rs = RandomUtil.randomString(10);
			System.out.println("lingyun_"+rs);
		}
		
//		for (int i = 1; i <= 7; i++) {
//			System.out.println(i);
//		}
		
		
	}

}
