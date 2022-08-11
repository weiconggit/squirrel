package org.squirrel.framework.data.web;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.squirrel.framework.data.DataOperatorParam;

/**
 * @description mybatis通用Dao
 * @author weicong
 * @time   2021年7月24日
 * @version 1.0
 */
public interface MybatisBaseDao<T> {

	/**
	 * 单条或批量插入
	 * @param param
	 * @return
	 */
	@Insert("<script>"
			+ "INSERT INTO ${param.tableName} "
			+ "<foreach item=\"item\" collection=\"param.insertKeys\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
				+ "${item}"
			+ "</foreach>"
			+ " VALUES "
			+ "<foreach item=\"itemList\" collection=\"param.values\" separator=\",\" open=\"\" close=\"\" index=\"\">"
				+ "<foreach item=\"item\" collection=\"itemList\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
					+ "#{item}"
				+ "</foreach>"
			+ "</foreach>"
			+ "</script>")
	int insert(@Param("param") DataOperatorParam param);

	/**
	 * 条件更新
	 * @param param
	 * @return
	 */
	@Update("<script>"
			+ "UPDATE ${param.tableName} SET "
				+ "<foreach item=\"item\" collection=\"param.setKeyValues\" separator=\"\" open=\"\" close=\"\" index=\"\">"
					+ "${item.key} = #{item.value}"
				+ "</foreach>"
			+ "<where>"
				+ "<foreach item=\"item\" collection=\"param.whereKeyValues\" separator=\"\" open=\"\" close=\"\" index=\"\">"
					+ "<if test=\"item.value != null\">" 
					+ "AND ${item.key} = #{item.value}" 
					+ "</if>"
				+ "</foreach>"
			+ "</where>"
			+ "</script>")
	int update(@Param("param") DataOperatorParam param);

	/**
	 * 条件删除
	 * @param param
	 * @return
	 */
//	@Delete("<script>"
//			+ "DELETE FROM ${param.tableName}"
//			+ "<where>"
//			+ "<foreach item=\"item\" collection=\"param.whereKeyValues\" separator=\",\" open=\"\" close=\"\" index=\"\">"
//			+ "AND ${item.key} = #{item.value}"
//			+ "</foreach>"
//			+ "</where>"
//			+ "</script>")
//	int delete(@Param("param") DataOperatorParam param);
	

	/**
	 * ids删除
	 * @param param
	 * @return
	 */
	@Delete("<script>"
			+ "DELETE FROM ${param.tableName} WHERE id IN "
				+ "<foreach item=\"item\" collection=\"param.whereKeyValues\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
					+ "#{item.value}"
				+ "</foreach>"
			+ "</script>")
	int deleteByIds(@Param("param") DataOperatorParam param);

	/**
	 * 查询
	 * @param param
	 * @return
	 */
	@Select("<script>"
			+ "SELECT "
				+ "<foreach item=\"item\" collection=\"param.selectKeys\" separator=\",\" open=\"\" close=\"\" index=\"\">"
					+ "${item}"
				+ "</foreach> "
			+ " FROM ${param.tableName}"
			+ "<where>"
				+ "<foreach item=\"item\" collection=\"param.whereKeyValues\" separator=\",\" open=\"\" close=\"\" index=\"\">"
					+ "AND ${item.key} = #{item.value}"
				+ "</foreach>"
			+ "</where>"
			+ "</script>")
	List<T> select(@Param("param") DataOperatorParam param);
	
	
	@Select("<script>"
			+ "SELECT "
				+ "<foreach item=\"item\" collection=\"param.selectKeys\" separator=\",\" open=\"\" close=\"\" index=\"\">"
					+ "${item}"
				+ "</foreach> "
			+ " FROM ${param.tableName}"
			+ "<if test=\"param.whereKeyValues != null\">" 
			+ "<where>"
				+ "<foreach item=\"item\" collection=\"param.whereKeyValues\" separator=\",\" open=\"\" close=\"\" index=\"\">"
					+ "AND ${item.key} = #{item.value}"
				+ "</foreach>"
			+ "</where>"
			+ "</if>"
			+ "</script>")
	List<T> page(@Param("page") BasePage<T> page, @Param("param") DataOperatorParam param);
	
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
