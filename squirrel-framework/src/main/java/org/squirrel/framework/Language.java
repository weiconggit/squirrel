package org.squirrel.framework;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.squirrel.framework.util.StrUtil;

/**
 * @description 翻译管理
 * @author weicong
 * @time   2021年2月27日 下午6:54:12
 * @version 1.0
 */
public final class Language {

	private Language() {}
	
	public static final String LANGUAGE_PROPERTIES = "language.properties";
	
	private static Map<String, String> lan = null;
	
	static {
		Properties properties = new Properties();
		Enumeration<URL> resources;
		try {
			resources = Thread.currentThread().getContextClassLoader().getResources(LANGUAGE_PROPERTIES);
			while (resources.hasMoreElements()) {
				InputStreamReader in =  new InputStreamReader(resources.nextElement().openStream(),"utf-8");
				properties.load(in);
			}
			lan = new HashMap<String, String>((Map)properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final String LOG_SIGN = get("squirrelLogSign");
	
	/**
	 * 获取一个翻译
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String string = lan.get(key);
		if (StrUtil.isEmpty(string)) {
			return key;
		}
		return string;
	}
}
