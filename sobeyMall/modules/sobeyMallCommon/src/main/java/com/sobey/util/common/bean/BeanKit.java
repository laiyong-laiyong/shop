package com.sobey.util.common.bean;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.HashMap;
import java.util.Map;

/**
 * 类拷贝
 * Created by Rukiy on 2016-07-14
 */
public class BeanKit {

	/*缓存*/
	private static Map<Class, Map<Class, BeanCopier>> beanCopierCache = new HashMap<>();

	/**
	 * 属性拷贝
	 *
	 * @param from
	 * @param to
	 * @param <T>
	 * @return
	 */
	public static <T> T copyTo(Object from, Object to) {

		return copyTo(from, to, null);
	}

	public static <T> T copyTo(Object from, Object to, Converter converter) {
		if (from == null || to == null) {
			return null;
		}

		Class fromClass = from.getClass();
		Class<T> toClass = (Class<T>) to.getClass();

		BeanCopier beanCopier = getBeanCopier(fromClass, toClass);

		beanCopier.copy(from, to, converter);

		return (T) to;
	}

	private static BeanCopier getBeanCopier(Class fromClass, Class toClass) {
		Map<Class, BeanCopier> beanCopierMap = beanCopierCache.get(fromClass);
		if (beanCopierMap == null) {
			synchronized (BeanKit.class) {
					beanCopierMap = beanCopierCache.computeIfAbsent(fromClass, k -> new HashMap<>());
			}
		}

		BeanCopier beanCopier = beanCopierMap.get(toClass);

		if (beanCopier == null) {
			synchronized (BeanKit.class) {
				beanCopier = beanCopierMap.get(toClass);
				if (beanCopier == null) {
					beanCopier = BeanCopier.create(fromClass, toClass, false);
					beanCopierMap.put(toClass, beanCopier);
				}
			}
		}

		return beanCopier;
	}
}
