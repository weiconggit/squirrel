package org.squirrel.framework.database.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 数据操作参数
 * @author weicong
 * @time   2021年7月24日 下午6:39:49
 * @version 1.0
 */
public class DataOperatorParam {

	/** 表名 sys_user*/
	private String tableName;

	// 新增
	/** 字段名，插入的部分sql (name1, name2, ...)*/
	private String insertKeysSql;
	/** 字段对应多个对象值 */
	private List<List<Object>> values;

	// 其他
	private String whereSql;
	
	public DataOperatorParam() {
		this.values = new ArrayList<>();
	}

	public final String getTableName() {
		return tableName;
	}

	public final void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public final String getWhereSql() {
		return whereSql;
	}

	public final void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}

	public String getInsertKeysSql() {
		return insertKeysSql;
	}

	public void setInsertKeysSql(String insertKeysSql) {
		this.insertKeysSql = insertKeysSql;
	}

	public List<List<Object>> getValues() {
		return values;
	}
	public void setValues(List<List<Object>> values) {
		this.values = values;
	}
}
