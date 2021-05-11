package org.squirrel.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrel.framework.util.StrUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @description 配置和翻译
 * @author weicong
 * @time   2021年2月27日 下午6:54:12
 * @version 1.0
 */
public final class SquirrelProperties {

	private static final Logger log = LoggerFactory.getLogger(SquirrelProperties.class);

	private SquirrelProperties() {}

	private static Map<String, String> allProperties = null;

	static {
		Properties properties = new Properties();
		Enumeration<URL> resources;
		try {
			resources = Thread.currentThread().getContextClassLoader().getResources("squirrel.properties");
			while (resources.hasMoreElements()) {
				InputStreamReader in =  new InputStreamReader(resources.nextElement().openStream(), StandardCharsets.UTF_8);
				properties.load(in);
			}
			allProperties = new HashMap<String, String>((Map)properties);
		} catch (IOException e) {
			log.error("read custom properties error", e);
		}
	}



	// ~ basic config
	// ============================================================

	/** Squirrel通用日志标识 */
	public static final String LOG_SIGN = get("squirrelLogSign");
	/** redisson置文件名称 */
	public static final String REDISSON_FILE_NAME = allProperties.get("redissonFileName");



	// ~ method
	// ============================================================

	/**
	 * 获取一个配置值或翻译值
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String string = allProperties.get(key);
		if (StrUtil.isEmpty(string)) {
			return key;
		}
		return string;
	}

	/**
	 * 获取错误信息
	 * @param key
	 * @return
	 */
	public static String get(Integer key) {
		return allProperties.get(String.valueOf(key));
	}
}
