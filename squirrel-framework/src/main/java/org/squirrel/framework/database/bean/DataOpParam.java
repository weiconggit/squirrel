package org.squirrel.framework.database.bean;

import java.util.List;

/**
 * @description 数据操作参数
 * @author weicong
 * @time   2021年7月24日 下午6:39:49
 * @version 1.0
 */
public class DataOpParam {

	private String tableName;
	/** 字段名 */
	private List<String> feilds;

	/** 字段对应值 */
	private List<String> values;
	private String whereSql;
	
	public DataOpParam() {}

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

	public List<String> getFeilds() {
		return feilds;
	}

	public void setFeilds(List<String> feilds) {
		this.feilds = feilds;
	}

	public static class ParamBuilder {
		
		private String key;
		private String value;
		
		/**
		 * @param key
		 * @param value
		 */
		public ParamBuilder(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		public final String getKey() {
			return key;
		}

		public final void setKey(String key) {
			this.key = key;
		}

		public final String getValue() {
			return value;
		}

		public final void setValue(String value) {
			this.value = value;
		}
		
	}
}
