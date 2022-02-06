package org.squirrel.framework.database.bean;

import java.util.List;

/**
 * @description 
 * @author weicong
 * @time   2022年2月6日
 * @version 1.0
 */
public class BasePage<T> {

	private Integer current;// 当前页
	private Integer limit; 	// 每页条数
	private Integer total;	// 总页数
	private List<T> list;	// 数据

	public BasePage() {}
	
	/**
	 * @param current
	 * @param limit
	 */
	public BasePage(Integer current, Integer limit) {
		super();
		this.current = current;
		this.limit = limit;
	}
	
	public final Integer getCurrent() {
		return current;
	}
	public final void setCurrent(Integer current) {
		this.current = current;
	}
	public final Integer getLimit() {
		return limit;
	}
	public final void setLimit(Integer limit) {
		this.limit = limit;
	}
	public final Integer getTotal() {
		return total;
	}
	public final void setTotal(Integer total) {
		this.total = total;
	}
	public final List<T> getList() {
		return list;
	}
	public final void setList(List<T> list) {
		this.list = list;
	}
	
}
