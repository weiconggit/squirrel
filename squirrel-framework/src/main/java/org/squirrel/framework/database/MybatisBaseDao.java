package org.squirrel.framework.database;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.squirrel.framework.database.bean.DataOperatorParam;

/**
 * @description mybatis通用Dao
 * @author weicong
 * @time   2021年7月24日 下午6:02:37
 * @version 1.0
 */
public interface MybatisBaseDao<T> {

	/**
	 * 单条或批量插入
	 * @param param
	 * @return
	 */
	@Insert("<script>"
			+ "INSERT INTO ${param.tableName} ${param.insertKeysSql} "
			+ "VALUES "
			+ "<foreach item=\"itemList\" collection=\"param.values\" separator=\",\" open=\"\" close=\"\" index=\"\">"
			+ "<foreach item=\"item\" collection=\"itemList\" separator=\",\" open=\"(\" close=\")\" index=\"\">"
			+ "#{item}"
			+ "</foreach>"
			+ "</foreach> "
			+ "</script>")
	int insert(@Param("param") DataOperatorParam param);

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
	int update(@Param("param") DataOperatorParam param);
	
//	<update id="updatePinyin">
//	  UPDATE sys_region_continents
//	  SET C_NAME_PINYIN =
//	  <foreach collection="idNames" index="index" item="item"
//	           open="CASE ID" separator=" " close="END">
//	    WHEN #{item.id} THEN #{item.pinyin}
//	  </foreach>
//	  WHERE ID IN
//	  <foreach collection="idNames" index="index" item="item"
//	           open="(" separator="," close=")">
//	    #{item.id,jdbcType=BIGINT}
//	  </foreach>
//	</update>

	/**
	 * 批量删除
	 * @param param
	 * @return
	 */
	@Delete("<script>"
			+ "DELETE FROM ${param.tableName} "
			+ "${param.whereSql}"
			+ "</script>")
	int delete(@Param("param") DataOperatorParam param);

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
	List<T> select(@Param("param") DataOperatorParam param);
	
}
