package org.squirrel.generator.data;

import java.util.List;

/**
 * 通用数据表结构
 * @author weicong
 * @time   2021年1月7日
 * @version 1.0
 */
public class TableData {

	private String tableName;		// 源名称，sys_user
	private String tableNameNoUnd;	// 无下滑线，sysuser
	private String tableNameHump; 	// 首字大写驼峰命令，SysUser
	private String tableNameCN; 	// 中文名称，系统用户
	private List<ColumnData> columnDatas;
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the tableNameNoUnd
	 */
	public String getTableNameNoUnd() {
		return tableNameNoUnd;
	}
	/**
	 * @param tableNameNoUnd the tableNameNoUnd to set
	 */
	public void setTableNameNoUnd(String tableNameNoUnd) {
		this.tableNameNoUnd = tableNameNoUnd;
	}
	/**
	 * @return the tableNameHump
	 */
	public String getTableNameHump() {
		return tableNameHump;
	}
	/**
	 * @param tableNameHump the tableNameHump to set
	 */
	public void setTableNameHump(String tableNameHump) {
		this.tableNameHump = tableNameHump;
	}
	/**
	 * @return the tableNameCN
	 */
	public String getTableNameCN() {
		return tableNameCN;
	}
	/**
	 * @param tableNameCN the tableNameCN to set
	 */
	public void setTableNameCN(String tableNameCN) {
		this.tableNameCN = tableNameCN;
	}
	/**
	 * @return the columnDatas
	 */
	public List<ColumnData> getColumnDatas() {
		return columnDatas;
	}
	/**
	 * @param columnDatas the columnDatas to set
	 */
	public void setColumnDatas(List<ColumnData> columnDatas) {
		this.columnDatas = columnDatas;
	}
	@Override
	public String toString() {
		return "TableData [tableName=" + tableName + ", tableNameNoUnd=" + tableNameNoUnd + ", tableNameHump="
				+ tableNameHump + ", tableNameCN=" + tableNameCN + ", columnDatas=" + columnDatas.toString() + "]";
	}
	
}
