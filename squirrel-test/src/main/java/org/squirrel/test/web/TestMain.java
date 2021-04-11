package org.squirrel.test.web;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author weicong
 * @time   2020年11月26日
 * @version 1.0
 */
public class TestMain {

	private static final Map<String, String> map = new HashMap<>();
	
	public static void main(String[] args) {
		System.out.println(TestMain.class.getPackage().getName());
		
//		String pa = "(https|http)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
//		String ha = "http://localhost:90/sms/";
//		boolean matches = ha.matches(pa);
//		System.out.println(matches);
		
		// 已初始化好的hashMap，在多线程中get不会有安全问题
		// https://stackoverflow.com/questions/104184/is-it-safe-to-get-values-from-a-java-util-hashmap-from-multiple-threads-no-modi
//		Map<String, String> map = new HashMap<>();
//		for (int i = 0; i < 50000; i++) {
//			map.put("GET/v1/test" + i, "get" + i);
//		}
//		ExecutorService threadPool = new ThreadPoolExecutor(0, 6000,
//	            0L, TimeUnit.SECONDS,
//	            new SynchronousQueue<>());
//		
//		for (int i = 0; i < 6000; i++) {
//			final int k = i;
//			threadPool.execute(()-> {
//				String string = map.get("GET/v1/test40000");
//				System.out.println(string + "=" + k);
////				if (!string.equals("get40000")) {
////					System.err.println(string);
////				}
//			});
//		}
//		System.err.println("完成");
		
//		SysUser sysUser = new SysUser();
//		sysUser.setUsername("haha");
//		sysUser.setPhone("13600");
//		Field[] declaredFields = sysUser.getClass().getDeclaredFields();
//		for (Field field : declaredFields) {
//			field.setAccessible(true);
//			System.out.println(field.getName());
//			System.out.println(field.getType());
//			try {
//				System.out.println(field.get(sysUser));
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				e.printStackTrace();
//			}
//			System.out.println("================");
//		}
		
		
//		BeanInfo beanInfo = null;
//		try {
//			beanInfo = Introspector.getBeanInfo(sysUser.getClass());
//		} catch (IntrospectionException e) {
//			e.printStackTrace();
//		} 
//		// 获取类属性
//		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//			System.out.println(propertyDescriptor.getDisplayName());
//			System.out.println(propertyDescriptor.getPropertyType());
//			try {
//				System.out.println(propertyDescriptor.getReadMethod().invoke(sysUser, args));
//			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				e.printStackTrace();
//			}
//			System.out.println("==========");
//		}
	}
	
	
	public static Map<String, String> get(){
		return map;
	}
}
