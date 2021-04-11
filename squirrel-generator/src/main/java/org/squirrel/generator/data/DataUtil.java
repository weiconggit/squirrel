package org.squirrel.generator.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.squirrel.framework.util.StrUtil;
import org.squirrel.generator.mysql.MysqlColumn;
import org.squirrel.generator.mysql.MysqlUtil;

/**
 * @author weicong
 * @time   2021年1月7日
 * @version 1.0
 */
public final class DataUtil {

	public static final String MYSQL = "mysql";
	
	private DataUtil() {}
	
	private static List<Field> FIELD_TABLE;
	
	static {
		Field[] fields = TableData.class.getDeclaredFields();
		FIELD_TABLE = new ArrayList<>(fields.length);
		for (Field field : fields) {
			field.setAccessible(true);
			FIELD_TABLE.add(field);
		}
	}
	
	/**
	 * 获取表结构模板填充数据
	 * @param dbType 数据库类型
	 * @return
	 */
	public static List<Map<String, Object>> getTableMaps(String dbType) {
		List<TableData> tables = null;
		if (MYSQL.equals(dbType)) {
			tables = convertMysql();
		}
		
		List<Map<String, Object>> list = new ArrayList<>();
		if (tables != null && !tables.isEmpty()) {
			Map<String, Object> map = null;
			
			try {
				for (TableData table : tables) {
					map = new HashMap<>();
					for (Field field : FIELD_TABLE) {
						Object object = field.get(table);
						map.put(field.getName(), object);
					}
					list.add(map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * mysql源数据转换为通用表字段对象
	 * @return
	 */
	private static List<TableData> convertMysql(){
		List<MysqlColumn> mysqlColumns = MysqlUtil.getColumns();
		List<TableData> list = new ArrayList<>();
		TableData tableData = null;
		ColumnData columnData = null;
		Map<String, String> javaTypeMap = MysqlUtil.getJavaTypeMap();
		String javaType = null;
		String tableName = null;
		String columnName = null;
		
		String currentTable = null;
		for (MysqlColumn mysqlColumn : mysqlColumns) {
			if (currentTable == null || !currentTable.equals(mysqlColumn.getTableName())) {
				currentTable = mysqlColumn.getTableName();
				tableData = new TableData();
				tableName = mysqlColumn.getTableName();
				tableData.setTableName(tableName);
				tableData.setTableNameNoUnd(getNoUnd(tableName));
				tableData.setTableNameHump(getHump(tableName, true));
				tableData.setTableNameCN(mysqlColumn.getTableComment());
				tableData.setColumnDatas(new ArrayList<>());
				list.add(tableData);
			}
			columnData = new ColumnData();
			columnName = mysqlColumn.getColumnName();
			columnData.setName(getHump(columnName, false));
			columnData.setNameHump(getHump(columnName, true));
			columnData.setNameCN(mysqlColumn.getColumnComment()); // 暂时是备注获取
			columnData.setNullable(mysqlColumn.getIsNullable().equals("YES"));
			columnData.setLength(getFieldLength(mysqlColumn.getColumnType()));
			javaType = javaTypeMap.get(mysqlColumn.getDataType());
			if (StrUtil.isEmpty(javaType)) {
				System.err.println(String.format("mysql类型：%s，没有其对应的java类类型", mysqlColumn.getDataType()));
			} else {
				// Tinyint(1) 特殊处理为 Boolean
				if (mysqlColumn.getDataType().equals("tinyint") && columnData.getLength() == 1) {
					columnData.setType("Boolean");
				} else {
					columnData.setType(javaType);
				}
			}
			// ID 特殊处理
			if (StrUtil.equals(columnName, "id")) {
				columnData.setLength(0);
				columnData.setNameCN("ID");
				tableData.getColumnDatas().add(columnData);
				continue;
			}
			tableData.getColumnDatas().add(columnData);
		}
		list.forEach(System.out::println);
		return list;
	}
	
	/**
	 * 获取字段长度（整数类型是大小）
	 * @return
	 */
	private static int getFieldLength(String columnType) {
		if (StrUtil.isEmpty(columnType)) {
			return 0;
		}
		if (columnType.contains("(")) {
			String len = columnType.substring(columnType.indexOf("(")+1, columnType.indexOf(")"));
			return Integer.parseInt(len);
		}
		return 0;
	}
	
	/**
	 * 驼峰命令
	 * @param name
	 * @param needFirstUpper 是否需要首字大写
	 * @return
	 */
	private static String getHump(String name, boolean needFirstUpper) {
		if (name == null) {
			return name;
		}
		if (name.contains("_")) {
			String[] splitTemp = name.split("_");
			return StrUtil.upperFirstLetterOfWords(splitTemp, needFirstUpper);
		} 
		if (needFirstUpper) {
			return StrUtil.upperFirstLetter(name);
		}
		return name;
	}
	
	/**
	 * 无下滑线的名称
	 * @param name
	 * @return
	 */
	private static String getNoUnd(String name) {
		if (name == null) {
			return name;
		}
		if (name.contains("_")) {
			return name.replace("_", "");
		} 
		return name;
	}
}
