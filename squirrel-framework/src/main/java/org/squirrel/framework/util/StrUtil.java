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

	/**
	 * 驼峰转下划线
	 * 示例：sysUser 或 SysUser => sys_user
	 * @param string
	 * @return
	 */
	public static String humpToUnderLine(String string){
		if (isEmpty(string)) {
			return string;
		}
		StringBuilder stringBuilder = new StringBuilder();
		char[] chars = string.toCharArray();
		for (int i = 0, size = chars.length; i < size; i++) {
			if (i == 0) {
				stringBuilder.append(Character.toLowerCase(chars[i]));
			} else {
				if (Character.isUpperCase(chars[i])) {
					stringBuilder.append("_").append(Character.toLowerCase(chars[i]));
				} else {
					stringBuilder.append(chars[i]);
				}
			}
		}
		return stringBuilder.toString();
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
	 * @param needFirstLetterUpper 是否需要拼接后单词首字母大写
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

	/**
	 * 字符串是否时正整数
	 * @param string
	 * @return
	 */
	public static boolean isNum(String string){
		if (isEmpty(string)) {
			return false;
		}
		return string.matches("\\d*");
	}

	public static boolean isNotNum(String string) {
		return !isNum(string);
	}
}
