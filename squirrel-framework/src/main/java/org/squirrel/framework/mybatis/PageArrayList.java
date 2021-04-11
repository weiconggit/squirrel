package org.squirrel.framework.mybatis;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 
 * @author weicong
 * @time   2021年3月20日 上午10:57:49
 * @version 1.0
 */
public class PageArrayList<T> extends ArrayList<T>{

	private static final long serialVersionUID = 2316918096364505028L;

	/**
     * 记录数
     */
    private List<T> records;

    /**
     * 总数
     */
    private long total;
}
