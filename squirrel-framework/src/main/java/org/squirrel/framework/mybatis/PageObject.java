package org.squirrel.framework.mybatis;

import java.util.List;

/**
 * @author weicong
 * @time   2021年3月19日
 * @version 1.0
 */
public class PageObject<T> {

    private long page; // 当前页
    private long limit; // 每页数量
    private long total; // 总记录数
	private long pages; // 总页数
	private List<T> list; // 数据

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getPages() {
		return pages;
	}
	public void setPages(long pages) {
		this.pages = pages;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	
}
