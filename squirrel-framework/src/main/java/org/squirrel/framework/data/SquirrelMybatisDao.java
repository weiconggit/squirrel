package org.squirrel.framework.data;

import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.LinkedHashMap;
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
	 * 单条插入
	 * @param tableName 表名
	 * @param insertKeys  (key1,key2,...)
	 * @param insertValues (value1,value2...)
	 * @return
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
	 * @return
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

//	/**
//	 * TODO
//	 * 无ID新增，有ID更新
//	 * @param param
//	 * @return
//	 */
//	@Update("<script>"
//			+ "UPDATE ${tableName} SET "
//			+ "<foreach collection=\"setKeyValues.entrySet()\" separator=\"\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
//			+ "${key} = #{item}"
//			+ "</foreach>"
//			+ "<where>"
//			+ "<foreach collection=\"whereKeyValues.entrySet()\" separator=\"\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
//			+ "<if test=\"item.value != null\">"
//			+ "AND ${key} = #{item}"
//			+ "</if>"
//			+ "</foreach>"
//			+ "</where>"
//			+ "</script>")
//	int insertOnDuplicateKeyUpdate(@Param("tableName") String tableName, @Param("setKeyValues") Map<String, Object> setKeyValues, @Param("whereKeyValues") Map<String, Object> whereKeyValues);

	/**
	 * 条件更新
	 * @param tableName 表名
	 * @param setKeyValues key1 = value1,key2 = value2
	 * @param whereKeyValues WHERE keyA = valueA AND keyB = valueB ...
	 * @return
	 */
	@Update("<script>"
			+ "UPDATE ${tableName} SET "
			+ "<foreach collection=\"setKeyValues.entrySet()\" separator=\",\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
			+ "${key} = #{item}"
			+ "</foreach>"
			+ "<where>"
			+ "<foreach collection=\"whereKeyValues.entrySet()\" separator=\"\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
			+ "<if test=\"item.value != null\">"
			+ "AND ${key} = #{item}"
			+ "</if>"
			+ "</foreach>"
			+ "</where>"
			+ "</script>")
	int update(@Param("tableName") String tableName, @Param("setKeyValues") Map<String, Object> setKeyValues, @Param("whereKeyValues") Map<String, Object> whereKeyValues);

	/**
	 * 条件删除
	 * @param tableName 表名
	 * @param whereKeyValues WHERE keyA = valueA AND keyB = valueB ...
	 * @return
	 */
	@Delete("<script>"
			+ "DELETE FROM ${tableName}"
			+ "<where>"
				+ "<foreach collection=\"whereKeyValues.entrySet()\" separator=\",\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
					+ "AND ${key} = #{item}"
				+ "</foreach>"
			+ "</where>"
			+ "</script>")
	int delete(@Param("tableName") String tableName, @Param("whereKeyValues") Map<String, Object> whereKeyValues);
	

	/**
	 * ids删除
	 * @param tableName 表名
	 * @param ids
	 * @return
	 */
	@Delete("<script>"
			+ "DELETE FROM ${tableName} WHERE id IN "
				+ "<foreach item=\"item\" collection=\"ids\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
					+ "#{item}"
				+ "</foreach>"
			+ "</script>")
	int deleteByIds(@Param("tableName") String tableName, @Param("ids") Collection<String> ids);

	/**
	 * ids查询数据
	 * @param tableName 表名
	 * @param selectKeys key1,key2,...
	 * @param ids
	 * @param sortMap ORDER BY keyA ASC, keyB DESC...
	 * @return
	 */
	@Select("<script>"
			+ "SELECT "
			+ "<foreach item=\"item\" collection=\"selectKeys\" separator=\",\" open=\"\" close=\"\" index=\"\">"
				+ "${item}"
			+ "</foreach> "
			+ " FROM ${tableName} WHERE id IN "
			+ "<foreach item=\"item\" collection=\"ids\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
				+ " #{item}"
			+ "</foreach>"
			+ "<if test=\"sortMap != null \">"
				+ "ORDER BY "
				+ "<foreach collection=\"sortMap.entrySet()\" separator=\",\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
				+ "${key} #{item}"
				+ "</foreach>"
			+ "</if>"
			+ "</script>")
	List<T> selectByIds(@Param("tableName") String tableName,
						@Param("selectKeys") List<String> selectKeys,
						@Param("ids") Collection<String> ids,
						@Param("sortMap") LinkedHashMap<String, String> sortMap);

	/**
	 * 查询
	 * @param tableName 表名
	 * @param selectKeys key1,key2,...
	 * @param whereMap WHERE keyA = valueA AND keyB = valueB ...
	 * @param sortMap ORDER BY keyA ASC, keyB DESC...
	 * @return
	 */
	@Select("<script>"
			+ "SELECT "
				+ "<foreach item=\"item\" collection=\"selectKeys\" separator=\",\" open=\"\" close=\"\" index=\"\">"
					+ "${item}"
				+ "</foreach> "
			+ " FROM ${tableName}"
			+ "<if test=\"whereMap != null\">"
			+ "<where>"
				+ "<foreach collection=\"whereMap.entrySet()\" separator=\",\" open=\"\" close=\"\" index=\"key\" item=\"item\" >"
					+ "AND ${key} = #{item}"
				+ "</foreach>"
			+ "</where>"
			+ "</if>"
			+ "<if test=\"sortMap != null \">"
				+ "ORDER BY "
				+ "<foreach collection=\"sortMap.entrySet()\" separator=\",\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
				+ "${key} #{item}"
				+ "</foreach>"
			+ "</if>"
			+ "</script>")
	List<T> select(@Param("tableName") String tableName,
				   @Param("selectKeys") List<String> selectKeys,
				   @Param("whereMap") Map<String, Object> whereMap,
				   @Param("sortMap") LinkedHashMap<String, String> sortMap);

	/**
	 * 分页查询
	 * @param page
	 * @param tableName 表名
	 * @param selectKeys key1,key2,...
	 * @param whereMap WHERE keyA = valueA AND keyB = valueB ...
	 * @param sortMap ORDER BY keyA ASC, keyB DESC...
	 * @return
	 */
	@Select("<script>"
			+ "SELECT "
				+ "<foreach item=\"item\" collection=\"selectKeys\" separator=\",\" open=\"\" close=\"\" index=\"\">"
					+ "${item}"
				+ "</foreach> "
			+ " FROM ${tableName}"
			+ "<if test=\"whereMap != null\">"
			+ "<where>"
				+ "<foreach collection=\"whereMap.entrySet()\" separator=\",\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
					+ "AND ${key} = #{item}"
				+ "</foreach>"
			+ "</where>"
			+ "</if>"
			+ "<if test=\"sortMap != null \">"
				+ "ORDER BY "
				+ "<foreach collection=\"sortMap.entrySet()\" separator=\",\" open=\"\" close=\"\" index=\"key\" item=\"item\">"
				+ "${key} #{item}"
				+ "</foreach>"
			+ "</if>"
			+ "</script>")
	List<T> page(@Param("page") BasePage<T> page,
				 @Param("tableName") String tableName,
				 @Param("selectKeys") List<String> selectKeys,
				 @Param("whereMap") Map<String, Object> whereMap,
				 @Param("sortMap") LinkedHashMap<String, String> sortMap);







	// select count(1) from table where is_active is null;
	// SELECT * FROM product a JOIN (select id from product limit 866613, 20) b ON a.ID = b.id
	/**
	 * 条件查询总数
	 * @param param
	 * @return
	 */
//	@Select("<script>"
//			+ "SELECT count(id) FROM ${param.tableName} "
//			+ "<where>"
//				+ "<foreach item=\"item\" collection=\"param.whereKeyValues\" separator=\",\" open=\"\" close=\"\" index=\"\">"
//					+ "AND ${item.key} = #{item.value}"
//				+ "</foreach>"
//			+ "</where>"
//			+ "</script>")
//	int count(@Param("param") DataOperatorParam param);

	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
//	@Select("<script>"
//			+ "SELECT "
//			+ "<foreach item=\"item\" collection=\"param.selectKeys\" separator=\",\" open=\"\" close=\"\" index=\"\">"
//				+ "${item}"
//			+ "</foreach> "
//			+ " FROM ${param.tableName} a JOIN "
//			+ "(select id from ${param.tableName} limit #{basePage.offset}, #{basePage.limit}) b ON a.id = b.id"
//			+ "<where>"
//				+ "<foreach item=\"item\" collection=\"param.whereKeyValues\" separator=\",\" open=\"\" close=\"\" index=\"\">"
//					+ "AND b.${item.key} = #{item.value}"
//				+ "</foreach>"
//			+ "</where>"
//			+ "</script>")
//	List<T> page(@Param("param") DataOperatorParam param, @Param("basePage") BasePage<T> basePage);
	
}
