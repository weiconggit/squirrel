package org.squirrel.framework.util;

import java.util.Map;

/**
 * @description 
 * @author weicong
 * @time   2021年3月10日 下午11:26:00
 * @version 1.0
 */
public final class MapUtil {

	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}
	
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
}
