package org.squirrel.framework.util;

import java.util.Objects;

/**
 * @author weicong
 * @time   2020年11月25日
 * @version 1.0
 */
public final class StrUtil {

	private StrUtil() {}
	
	public static String parseString(Object object) {
		return object == null ? null : object.toString();
	}
	
	public static boolean equals(String str1, String str2) {
		return Objects.equals(str1, str2);
	}
	
	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}
	
	public static boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}
	
	public static String lowerFirstLetter(String string) {
		if (isEmpty(string)) {
			return string;
		}
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}
	
	public static String upperFirstLetter(String string) {
		if (isEmpty(string)) {
			return string;
		}
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	/**
	 * 每个单词第一个字母大写，并且拼接为一个单词
	 * @param words
	 * @param isFirstLetterUpper 是否需要拼接后单词首字母大写
	 * @return
	 */
	public static String upperFirstLetterOfWords(String[] words, boolean needFirstLetterUpper) {
		if (words == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0, len = words.length; i < len; i++) {
			if (!needFirstLetterUpper && i == 0) {
				builder.append(words[i]);
			} else {
				builder.append(upperFirstLetter(words[i]));
			}
		}
		return builder.toString();
	}
	
}
