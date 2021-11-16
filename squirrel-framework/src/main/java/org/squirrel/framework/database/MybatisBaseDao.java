package org.squirrel.framework.database;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.squirrel.framework.database.bean.DataOpParam;

/**
 * @description mybatis通用Dao
 * @author weicong
 * @time   2021年7月24日 下午6:02:37
 * @version 1.0
 */
public interface MybatisBaseDao<T> {
	
	/**
	 * 批量插入
	 * @param param
	 * @return
	 */
	@Insert("<script>"
			+ "INSERT INTO ${param.tableName} "
			+ "<foreach item=\"item\" collection=\"param.feilds\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
			+ "${item}"
			+ "</foreach> "
			+ "VALUES "
			+ "<foreach item=\"item\" collection=\"param.values\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
			+ "${item}"
			+ "</foreach> "
			+ "</script>")
	int insert(@Param("param") DataOpParam param);
	
	/**
	 * 批量更新
	 * @param param
	 * @return
	 */
	@Update("<script>"
			+ "UPDATE ${param.tableName} "
			+ "SET "
			+ "<foreach item=\"item\" collection=\"param.values\" separator=\",\" open=\"\" close=\"\" index=\"\">"
			+ "${item}"
			+ "</foreach> "
			+ "${param.whereSql}"
			+ "</script>")
	int update(@Param("param") DataOpParam param);
	
	/**
	 * 批量删除
	 * @param param
	 * @return
	 */
	@Delete("<script>"
			+ "DELETE FROM ${param.tableName} "
			+ "${param.whereSql}"
			+ "</script>")
	int delete(@Param("param") DataOpParam param);

	/**
	 * 批量查询
	 * @param param
	 * @return
	 */
	@Select("<script>"
			+ "SELECT "
			+ "<foreach item=\"item\" collection=\"param.feilds\" separator=\",\" open=\"\" close=\"\" index=\"\">"
			+ "${item}"
			+ "</foreach> "
			+ "FROM ${param.tableName} ${param.whereSql}"
			+ "</script>")
	List<T> select(@Param("param") DataOpParam param);
	
}
