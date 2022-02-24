package org.squirrel.framework.database.page;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 
 * @author weicong
 * @time   2022年2月6日
 * @version 1.0
 */
public class BasePage<T> {
	private static final long serialVersionUID = -1809171090924771706L;
	private Integer current;// 当前页
	private Integer limit; 	// 每页条数
	private Integer offset; // 偏移量
	private Integer pageSize; // 总页数
	private Integer total;	// 总数
	private List<T> list;	// 数据

	public BasePage() {}
	
	public BasePage(Integer current, Integer limit) {
		this.current = current == null ? 1 : current;
		this.limit = limit == null ? 10 : limit;
	}
	
	public final Integer getCurrent() {
		return current;
	}
//	public final void setCurrent(Integer current) {
//		this.current = current;
//	}
	public final Integer getLimit() {
		return limit;
	}
//	public final void setLimit(Integer limit) {
//		this.limit = limit;
//	}
	public final Integer getTotal() {
		return total;
	}
	
	public final void setTotal(Integer total) {
		this.total = total;
		if (this.total != null && this.total != 0) {
			int pageSizeTemp = total/limit; // 2/10=0,13/10=1
			int pageSizeRem = total%limit; // 2%10=2,13%10=3,20%10=0
			this.pageSize = pageSizeRem == 0 ? pageSizeTemp : pageSizeTemp + 1;
			this.current = this.current > this.pageSize ? pageSize : this.current;
			this.offset = (this.current - 1) * limit;
		} 
	}
	
	public final List<T> getList() {
		return list;
	}
	public final void setList(List<T> list) {
		this.list = list;
	}
	public Integer getPageSize() {
		return pageSize;
	}
//	public void setPageSize(Integer pageSize) {
//		this.pageSize = pageSize;
//	}

	public Integer getOffset() {
		return offset;
	}
//	public void setOffset(Integer offset) {
//		this.offset = offset;
//	}

//	@Override
//	public String toString() {
//		return "BasePage [current=" + current + ", limit=" + limit + ", offset=" + offset + ", pageSize=" + pageSize
//				+ ", total=" + total + ", list=" + list + "]";
//	}
	
	
}
