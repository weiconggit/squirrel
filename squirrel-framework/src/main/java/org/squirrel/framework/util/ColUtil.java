package org.squirrel.framework.util;

import java.util.Collection;

/**
 * @description 
 * @author weicong
 * @time   2021年3月10日 下午11:31:32
 * @version 1.0
 */
public final class ColUtil {

	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}
	
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
}
