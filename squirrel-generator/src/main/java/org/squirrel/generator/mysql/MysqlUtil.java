package org.squirrel.generator.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.squirrel.generator.Generator;
import org.squirrel.generator.data.JdbcUtil;

/**
 * @description 
 * @author weicong
 * @time   2021年1月16日 下午12:31:14
 * @version 1.0
 */
public final class MysqlUtil {

	private MysqlUtil() {}
	
	/**
	 * 获取mysql表结构字段源数据
	 * @return
	 */
	public static List<MysqlColumn> getColumns() {
		// 定义需要的对象
		Connection connection = JdbcUtil.getConnection();
		if (connection == null) {
			return null;
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MysqlColumn> list = new ArrayList<>();
		try {
//			ps = connection.prepareStatement("SELECT * FROM information_schema.`COLUMNS` where TABLE_SCHEMA = 'test' and TABLE_NAME = 'sys_user';");
//			ps = connection.prepareStatement("SELECT * FROM information_schema.`COLUMNS` where TABLE_SCHEMA = 'test';");
			ps = connection.prepareStatement("SELECT t1.*,t2.TABLE_COMMENT FROM information_schema.`COLUMNS` AS t1\r\n" + 
					"INNER JOIN information_schema.`TABLES` AS t2 ON t1.TABLE_NAME = t2.TABLE_NAME \r\n" + 
					"where t2.TABLE_SCHEMA = '" + Generator.DB_NAME +"' and t1.TABLE_SCHEMA = '" + Generator.DB_NAME + "';");
			rs = ps.executeQuery();
			while (rs.next()) {
				MysqlColumn mysqlColumn = new MysqlColumn();
				mysqlColumn.setTableName(rs.getString("TABLE_NAME"));
				mysqlColumn.setColumnName(rs.getString("COLUMN_NAME"));
				mysqlColumn.setIsNullable(rs.getString("IS_NULLABLE"));
				mysqlColumn.setDataType(rs.getString("DATA_TYPE"));
				mysqlColumn.setColumnType(rs.getString("COLUMN_TYPE"));
				mysqlColumn.setColumnKey(rs.getString("COLUMN_KEY"));
				mysqlColumn.setColumnComment(rs.getString("COLUMN_COMMENT"));
				mysqlColumn.setTableComment(rs.getString("TABLE_COMMENT"));
				list.add(mysqlColumn);
				// TODO
				System.out.println(mysqlColumn.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(connection, null, ps, rs);
		}
		return list;
	} 
	
	/**
	 * mysql 对应的 java 类型
	 * @return
	 */
	public static Map<String, String> getJavaTypeMap(){
		Map<String, String> map = new HashMap<>();
		map.put("varchar", "String");
		map.put("char", "String");
		map.put("blob", "Byte[]");
		map.put("text", "String");
		map.put("int", "Integer");
		map.put("integer", "Integer");
		map.put("tinyint", "Integer");
		map.put("smallint", "Integer");
		map.put("mediumint", "Integer");
		map.put("bigint", "Long");
		map.put("float", "Float");
		map.put("double", "Double");
		map.put("decimal", "BigDecimal");
		map.put("boolean", "Boolean");
		map.put("id", "Long");
		map.put("date", "Date");
		// 也可以是 java Date 类型
		map.put("datetime", "LocalDateTime");
		map.put("timestamp", "LocalDateTime");
		return map;
	}
}
