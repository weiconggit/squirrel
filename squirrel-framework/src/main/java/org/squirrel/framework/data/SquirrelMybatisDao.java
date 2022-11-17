package org.squirrel.framework.data;

import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @description mybatis通用Dao
 * @author weicong
 * @time   2021年7月24日
 * @version 1.0
 */
public interface SquirrelMybatisDao<T> {

	/**
	 * 条件sql脚本，不能出现test=“ xx!="11" ”，只能是test=‘ xx!="11" ’
	 */
	 String whereSqlScript = ""
			+ "<if test=\"whereBeans != null\">"
			+ 	"<where>"
			+ 		"<foreach collection=\"whereBeans\" separator=\"\" open=\"\" close=\"\" index=\"\" item=\"item\" >"
			+ 			"<if test='item.value != null and item.type != \"IN\" and item.type != \"LIKE\"' >"
			+ 				"AND ${item.name} ${item.type} #{item.value}"
			+ 			"</if>"
			+ 			"<if test='item.value != null and item.type == \"IN\"'>"
			+ 				"AND ${item.name} IN "
			+ 				"<foreach collection=\"item.value\" separator=\"\" open=\"(\" close=\")\" index=\"\" item=\"item2\" >"
			+					"#{item2}"
			+ 				"</foreach>"
			+ 			"</if>"
			+ 			"<if test='item.value != null and item.type == \"LIKE\"'>"
			+ 				"AND ${item.name} LIKE concat('%',#{item.value},'%')"
			+ 			"</if>"
			+ 		"</foreach>"
			+ 	"</where>"
			+ "</if>";

	/**
	 * 排序sql脚本
	 */
	String oderSqlScript = ""
			 + "<if test=\"orderBeans != null \">"
			 + " ORDER BY "
			 + 	"<foreach collection=\"orderBeans\" separator=\",\" open=\"\" close=\"\" index=\"\" item=\"item\">"
			 + 		"${item.name} #{item.value}"
			 + 	"</foreach>"
			 + "</if>";

	/**
	 * 单条插入
	 * @param tableName 表名
	 * @param insertKeys  (key1,key2,...)
	 * @param insertValues (value1,value2...)
	 * @return 插入条数
	 */
	@Insert("<script>"
			+ "INSERT INTO ${tableName} "
			+ "<foreach item=\"item\" collection=\"insertKeys\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
				+ "${item}"
			+ "</foreach>"
			+ " VALUES "
			+ "<foreach item=\"item\" collection=\"insertValues\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
				+ "#{item}"
			+ "</foreach>"
			+ "</script>")
	int insert(@Param("tableName") String tableName, @Param("insertKeys") List<String> insertKeys, @Param("insertValues") List<Object> insertValues);


	/**
	 * 批量插入
	 * @param tableName 表名
	 * @param insertKeys (key1,key2,...)
	 * @param insertValues (value1,value2...),(value1,value2...)
	 * @return 插入条数
	 */
	@Insert("<script>"
			+ "INSERT INTO ${tableName} "
			+ "<foreach item=\"item\" collection=\"insertKeys\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
				+ "${item}"
			+ "</foreach>"
			+ " VALUES "
			+ "<foreach item=\"itemList\" collection=\"insertValues\" separator=\",\" open=\"\" close=\"\" index=\"\">"
				+ "<foreach item=\"item\" collection=\"itemList\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
					+ "#{item}"
				+ "</foreach>"
			+ "</foreach>"
			+ "</script>")
	int insertBatch(@Param("tableName") String tableName, @Param("insertKeys") List<String> insertKeys, @Param("insertValues") List<List<Object>> insertValues);

	/**
	 * 条件更新
	 * @param tableName 表名
	 * @param setKeyValues key1 = value1,key2 = value2
	 * @param whereBeans WHERE keyA = valueA AND keyB = valueB ...
	 * @return 变更条数
	 */
	@Update("<script>"
			+ "UPDATE ${tableName} SET "
			+ "<foreach collection=\"setKeyValues.entrySet()\" separator=\",\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
			+ 	"${key} = #{item}"
			+ "</foreach>"
			+ whereSqlScript
			+ "</script>")
	int update(@Param("tableName") String tableName, @Param("setKeyValues") Map<String, Object> setKeyValues, @Param("whereBeans") List<WhereBean> whereBeans);


	/**
	 * ids逻辑删除
	 * @param tableName 表名
	 * @return 删除条数
	 */
	@Delete("<script>"
			+ "UPDATE ${tableName} SET ${logicKey} = #{logicValue} "
			+ whereSqlScript
			+ "</script>")
	int logicDelete(@Param("tableName") String tableName, @Param("logicKey") String logicKey, @Param("logicValue") boolean logicValue, @Param("whereBeans") List<WhereBean> whereBeans);


	/**
	 * 条件删除
	 * @param tableName 表名
	 * @param whereBeans WHERE keyA = valueA AND keyB = valueB ...
	 * @return 删除条数
	 */
	@Delete("<script>"
			+ "DELETE FROM ${tableName} "
			+ whereSqlScript
			+ "</script>")
	int delete(@Param("tableName") String tableName, @Param("whereBeans") List<WhereBean> whereBeans);

	/**
	 * 查询
	 * @param tableName 表名
	 * @param selectKeys key1,key2,...
	 * @param whereBeans WHERE keyA = valueA AND keyB = valueB ...
	 * @param orderBeans ORDER BY keyA ASC, keyB DESC...
	 * @return VO对象列表
	 */
	@Select("<script>"
			+ "SELECT "
				+ "<foreach item=\"item\" collection=\"selectKeys\" separator=\",\" open=\"\" close=\"\" index=\"\">"
					+ "${item}"
				+ "</foreach> "
			+ " FROM ${tableName} "
			+ whereSqlScript
			+ oderSqlScript
			+ "</script>")
	List<T> select(@Param("tableName") String tableName,
				   @Param("selectKeys") List<String> selectKeys,
				   @Param("whereBeans") List<WhereBean> whereBeans,
				   @Param("orderBeans") List<OrderBean> orderBeans);

	/**
	 * 分页查询
	 * @param page 分页对象，如果查询参数中含有page，则VO对象列表会被自动装载到Page中的数据字段
	 * @param tableName 表名
	 * @param selectKeys key1,key2,...
	 * @param whereBeans WHERE keyA = valueA AND keyB = valueB ...
	 * @param orderBeans ORDER BY keyA ASC, keyB DESC...
	 * @return VO对象列表
	 */
	@Select("<script>"
			+ "SELECT "
			+ "<foreach item=\"item\" collection=\"selectKeys\" separator=\",\" open=\"\" close=\"\" index=\"\">"
				+ "${item}"
			+ "</foreach> "
			+ " FROM ${tableName} "
			+ whereSqlScript
			+ oderSqlScript
			+ "</script>")
	List<T> page(@Param("page") BasePage<T> page,
				 @Param("tableName") String tableName,
				 @Param("selectKeys") List<String> selectKeys,
				 @Param("whereBeans") List<WhereBean> whereBeans,
				 @Param("orderBeans") List<OrderBean> orderBeans);
	
}
