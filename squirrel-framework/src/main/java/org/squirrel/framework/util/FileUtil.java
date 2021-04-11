package org.squirrel.framework.util;

import java.io.File;

/**
 * @author weicong
 * @time   2021年1月7日
 * @version 1.0
 */
public final class FileUtil {

	private FileUtil() {}
	
	/**
	 * 删除目录和其中的所有目录及文件
	 * 
	 * @param file
	 */
	public static boolean deleteDirOrFile(File file) {
		File[] fs = file.listFiles(); // 列出文件夹下的所有资源
		if (fs == null || fs.length <= 0) {
			return true;
		}
		for (int i = 0; i < fs.length; i++) { // 遍历数组逐个删除
			File f = fs[i];
			if (f.isFile()) { // 如果是文件，则删除
				if (!f.delete()) {
					return false;
				}
			} else if (f.isDirectory()) { // 如果是文件夹，则递归调用删除方法
				deleteDirOrFile(f);
				if (!f.delete()) { // 删除此文件夹
					return false;
				}
			}
		}
		return file.delete(); // 删除最外层文件夹
	}
}
