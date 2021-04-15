package com.sobey.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * 异常信息获取工具
 */
public class ExceptionKit {


	public static String toString(Throwable e) {
		return buildExceptionString(e, 20);
	}

	private static String buildExceptionString(Throwable e, int lineNum) {
		StringBuilder buffer = new StringBuilder();
		if (e != null) {
			buffer.append(e.toString()).append("\n");
			if (e instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e).getTargetException();
			}

			StackTraceElement[] stc = e.getStackTrace();
			//如果不作处理后边循环会报索引越界异常
			if (lineNum > stc.length) {
				lineNum = stc.length;
			}
			
			for (int i = 0; i < lineNum; i++) {
				buffer.append(" at ").append(stc[i].getClassName()).append(".").append(stc[i].getMethodName()).append(" (").append(stc[i].getLineNumber()).append(")\n");
			}
		}
		return buffer.toString();
	}
}
