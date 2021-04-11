package org.squirrel.framework.mybatis;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO 添加分页功能
 * @description baseMapper加载器
 * @author weicong
 * @time 2021年3月14日 下午3:59:34
 * @version 1.0
 */
public final class SquirrelMapperLoader {

	/** The Logger. */
	private static final Logger log = LoggerFactory.getLogger(SquirrelMapperLoader.class);
	/** The constant HUMP_PATTERN. */
	private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");
	private final Class<?> mapperInterface;
	private final Class<?> clazz;
	private final List<Field> clazzFields;
	/** 主键类型 */
	private final Class<?> idType;
	/** 主键名 */
	private final String idName;
	/** 表名 */
	private final String tableName;

	private SquirrelMapperLoader(Class<? extends BaseMapper<?, ?>> mapperInterface) {
		// 拿到 具体的Mapper 接口 如 UserInfoMapper
		this.mapperInterface = mapperInterface;
		Type[] genericInterfaces = mapperInterface.getGenericInterfaces();
		// 从Mapper 接口中获取 BaseMapper<UserInfo,String>
		Type mapperGenericInterface = genericInterfaces[0];
		// 参数化类型
		ParameterizedType genericType = (ParameterizedType) mapperGenericInterface;

		// 参数化类型的目的时为了解析出 [UserInfo,String]
		Type[] actualTypeArguments = genericType.getActualTypeArguments();
		// 这样就拿到实体类型 UserInfo
		this.clazz = (Class<?>) actualTypeArguments[0];
		// 拿到主键类型 String
		this.idType = (Class<?>) actualTypeArguments[1];
		// 获取所有实体类属性 本来打算采用内省方式获取
		Field[] declaredFields = this.clazz.getDeclaredFields();

		// 解析主键 
		// TODO 去掉注解，使用配置文件，减少代码入侵
		this.idName = Stream.of(declaredFields).filter(field -> field.isAnnotationPresent(PrimaryKey.class))
				.findAny().map(Field::getName).orElseThrow(() -> new IllegalArgumentException(
						String.format("no annotation @PrimaryKey found in %s", this.clazz.getName())));

		// TODO 添加忽略属性，辨识出关键字transient
		// 解析属性名并封装为下划线字段 排除了静态属性 其它没有深入 后续有需要可声明一个忽略注解用来忽略字段
		this.clazzFields = Stream.of(declaredFields).filter(field -> !Modifier.isStatic(field.getModifiers()))
				.collect(Collectors.toList());
		// 解析表名
		this.tableName = camelCaseToMapUnderscore(clazz.getSimpleName()).replaceFirst("_", "");
	}

	/**
	 * 添加通用单表操作方法
	 * 
	 * @param mapperInterface
	 * @param configuration
	 */
	public static void addMappedStatements(Class<? extends BaseMapper<?, ?>> mapperInterface,
			Configuration configuration) {
		SquirrelMapperLoader provider = new SquirrelMapperLoader(mapperInterface);
		provider.insert(configuration);
		provider.deleteById(configuration);
		provider.updateById(configuration);
		provider.selectById(configuration);
		provider.selectByIds(configuration);
	}

	private void insert(Configuration configuration) {
		String mappedStatementId = mapperInterface.getName().concat(".").concat("insert");
		// xml配置中已经注册就跳过 xml中的优先级最高
		if (isExistStatement(configuration, mappedStatementId)) {
			return;
		}
		String[] columns = clazzFields.stream().map(Field::getName).map(SquirrelMapperLoader::camelCaseToMapUnderscore)
				.toArray(String[]::new);
		String[] values = clazzFields.stream().map(Field::getName).map(name -> String.format("#{%s}", name))
				.toArray(String[]::new);
		String sql = new SQL().INSERT_INTO(tableName).INTO_COLUMNS(columns).INTO_VALUES(values).toString();

		Map<String, Object> additionalParameters = new HashMap<>();

		doAddMappedStatement(configuration, mappedStatementId, sql, SqlCommandType.INSERT, clazz,
				additionalParameters);
	}

	private void deleteById(Configuration configuration) {
		String mappedStatementId = mapperInterface.getName().concat(".").concat("deleteById");
		if (isExistStatement(configuration, mappedStatementId)) {
			return;
		}
		String condition = primaryColumn().concat(" = #{" + idName + "}");
		String sql = new SQL().DELETE_FROM(tableName).WHERE(condition).toString();
		Map<String, Object> additionalParameters = new HashMap<>();

		doAddMappedStatement(configuration, mappedStatementId, sql, SqlCommandType.DELETE, idType,
				additionalParameters);
	}

	private void updateById(Configuration configuration) {
		String mappedStatementId = mapperInterface.getName().concat(".").concat("updateById");
		if (isExistStatement(configuration, mappedStatementId)) {
			return;
		}
		String[] columns = clazzFields.stream().map(Field::getName)
				// 更新忽略主键
				.filter(name -> !idName.equals(name))
				.map(name -> String.format("%s = #{%s}", camelCaseToMapUnderscore(name), name)).toArray(String[]::new);
		String condition = primaryColumn().concat(" = #{" + idName + "}");
		String sql = new SQL().UPDATE(tableName).SET(columns).WHERE(condition).toString();

		Map<String, Object> additionalParameters = new HashMap<>();
		doAddMappedStatement(configuration, mappedStatementId, sql, SqlCommandType.UPDATE, clazz,
				additionalParameters);
	}

	private void selectById(Configuration configuration) {
		String mappedStatementId = mapperInterface.getName().concat(".").concat("selectById");
		if (isExistStatement(configuration, mappedStatementId)) {
			return;
		}
		String[] columns = clazzFields.stream().map(Field::getName).map(SquirrelMapperLoader::camelCaseToMapUnderscore)
				.toArray(String[]::new);
		String condition = primaryColumn().concat(" = #{" + idName + "}");
		String sql = new SQL().SELECT(columns).FROM(tableName).WHERE(condition).toString();

		Map<String, Object> additionalParameters = new HashMap<>();

		doAddMappedStatement(configuration, mappedStatementId, sql, SqlCommandType.SELECT, idType,
				additionalParameters);
	}

	private void selectByIds(Configuration configuration) {
		String mappedStatementId = mapperInterface.getName().concat(".").concat("selectByIds");
		if (isExistStatement(configuration, mappedStatementId)) {
			return;
		}
//    	String[] columns = columnFields.stream()
//    			.map(Field::getName)
//    			.map(BaseMapperProvider::camelCaseToMapUnderscore)
//    			.toArray(String[]::new);
//    	String condition = primaryColumn().concat(" = #{" + identifer + "}");
//    	String sql = new SQL()
//    			.SELECT(columns)
//    			.FROM(table)
//    			.WHERE(condition)
//    			.toString();
		// TODO 拼接动态标签
		String script = "<script> " + "select id, name, isdel from " + tableName + " where id "
				+ "in <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'> #{item} </foreach> "
				+ "</script>";

		Map<String, Object> additionalParameters = new HashMap<>();

		doAddMappedStatement(configuration, mappedStatementId, script, SqlCommandType.SELECT, idType,
				additionalParameters);
	}

	private void doAddMappedStatement(Configuration configuration, String mappedStatementId, String script,
			SqlCommandType sqlCommandType, Class<?> parameterType, Map<String, Object> additionalParameters) {
		XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
		SqlSource sqlSource = xmlLanguageDriver.createSqlSource(configuration, script, parameterType);

		List<ResultMap> resultMaps = getStatementResultMaps(configuration, clazz, mappedStatementId);
		MappedStatement mappedStatement = new MappedStatement.Builder(configuration, mappedStatementId, sqlSource,
				sqlCommandType).resultMaps(resultMaps).build();
		configuration.addMappedStatement(mappedStatement);
	}

	// ~ util
	// ==================================================

	/**
	 * 当前是否存在相同的mapperStatementId
	 * 
	 * @param configuration
	 * @param id
	 * @return
	 */
	private boolean isExistStatement(Configuration configuration, String id) {
		if (configuration.getMappedStatementNames().contains(id)) {
			log.warn("MapperStatementId {} has been registered , the BaseMapper will not override", id);
			return true;
		}
		return false;
	}

	private String primaryColumn() {
		return camelCaseToMapUnderscore(idName);
	}

	/**
	 * 转换为下划线形式
	 *
	 * @param str the str
	 * @return the string
	 */
	private static String camelCaseToMapUnderscore(String str) {
		if (str.endsWith("VO")) {
			str = str.replace("VO", "");
		}
		Matcher matcher = HUMP_PATTERN.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Gets statement result maps.
	 *
	 * @param configuration the configuration
	 * @param resultType    the result type
	 * @param statementId   the statement id
	 * @return the statement result maps
	 */
	private List<ResultMap> getStatementResultMaps(Configuration configuration, Class<?> resultType,
			String statementId) {
		List<ResultMap> resultMaps = new ArrayList<>();

		ResultMap inlineResultMap = new ResultMap.Builder(configuration, statementId + "-Inline", resultType,
				new ArrayList<>(), null).build();
		resultMaps.add(inlineResultMap);

		return resultMaps;
	}
}
