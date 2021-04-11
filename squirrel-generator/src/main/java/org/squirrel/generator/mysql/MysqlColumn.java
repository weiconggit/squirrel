package org.squirrel.generator.mysql;

/**
 * mysql字段表对象
 * @author weicong
 * @time   2021年1月6日
 * @version 1.0
 */
public class MysqlColumn {
	
	/*
	 * mysql字段表结构字段
	 *  TABLE_CATALOG
		TABLE_SCHEMA
		TABLE_NAME
		COLUMN_NAME
		ORDINAL_POSITION
		COLUMN_DEFAULT
		IS_NULLABLE
		DATA_TYPE
		CHARACTER_MAXIMUM_LENGTH
		CHARACTER_OCTET_LENGTH
		NUMERIC_PRECISION
		NUMERIC_SCALE
		DATETIME_PRECISION
		CHARACTER_SET_NAME
		COLLATION_NAME
		COLUMN_TYPE
		COLUMN_KEY
		EXTRA
		PRIVILEGES
		COLUMN_COMMENT
		GENERATION_EXPRESSION
		mysql表本身结构字段，关联查询得出
		TABLE_COMMENT
	 */
	private String tableName;
	private String columnName;
	private String isNullable;
	private String dataType;	// 数据类型
	private String columnType;	// 字段类型，含有字段长度
	private String columnKey;	// 主键等标识
	private String columnComment;	// 字段表备注信息
	private String tableComment;	// 表本身备注信息

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	@Override
	public String toString() {
		return "MysqlColumn [tableName=" + tableName + ", columnName=" + columnName + ", isNullable=" + isNullable
				+ ", dataType=" + dataType + ", columnType=" + columnType + ", columnKey=" + columnKey
				+ ", columnComment=" + columnComment + ", tableComment=" + tableComment + "]";
	}
	
}
