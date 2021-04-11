package org.squirrel.framework;

import org.squirrel.framework.mybatis.BaseMapper;

/**
 * @author weicong
 * @time   2021年3月18日
 * @version 1.0
 */
public class TestAll {

	public static void main(String[] args) {
		Class<?> class1 = BaseMapper.class.getClass();
		if (class1.isAssignableFrom(BaseMapper.class)) {
			System.out.println("--");
		}
		if (BaseMapper.class.isAssignableFrom(class1)) {
			System.out.println("===");
		}
	}
}
