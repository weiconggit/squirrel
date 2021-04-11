package org.squirrel.framework.mybatis;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.squirrel.framework.SquirrelComponent;

/**
 * @author weicong
 * @time   2021年3月19日
 * @version 1.0
 */
@SquirrelComponent
@Intercepts({
	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}), 
	@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}) })
public class PageInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();
		if (target instanceof Executor) {
			if (args.length == 4) {
	            MappedStatement ms = (MappedStatement) args[0];
	            Object parameter = args[1];
	            RowBounds rowBounds = (RowBounds) args[2];
	            Object object = args[3];
	            System.err.println(object.getClass().getName());
	            ResultHandler resultHandler = (ResultHandler) args[3];
	            Executor executor = (Executor) invocation.getTarget();
	            BoundSql boundSql = ms.getBoundSql(parameter);
	            CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
	            return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
			}
		}
		if (target instanceof StatementHandler) {
			
		}
		if (target instanceof ResultSetHandler) {
			
		}
//        Object[] args = invocation.getArgs();
//        if (args.length == 2) {
//            String querySql = pageInfo.getSql();
//            pageInfo.setSql(null);
//            int position = querySql.toUpperCase().indexOf("FROM");
//            // 查询数据总数
//            Connection con = (Connection) args[0];
//            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) " + querySql.substring(position));
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            pageInfo.setTotal(rs.getInt(1));
//        } else {
//            // 分页
//            MappedStatement ms = (MappedStatement) args[0];
//            Object parameter = args[1];
//            RowBounds rowBounds = (RowBounds) args[2];
//            ResultHandler resultHandler = (ResultHandler) args[3];
//            Executor executor = (Executor) invocation.getTarget();
//            BoundSql boundSql = ms.getBoundSql(parameter);
//            CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
//            // 缓存SQL语句
//            pageInfo.setSql(boundSql.getSql());
//            // 反射获取BoundSql类变量sql的对象并添加limit语句
//            Field field = boundSql.getClass().getDeclaredField("sql");
//            field.setAccessible(true);
//            // 获取页码
//            Integer num = pageInfo.getPageNumber();
//            // 获取每页数量
//            Integer size = pageInfo.getPageSize();
//            if (num != null && size != null) {
//                field.set(boundSql, boundSql.getSql() + " limit " + num * size + "," + size);
//            }
//            return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
//        }
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		System.out.println("target = " + target);
		return Interceptor.super.plugin(target);
	}

	@Override
	public void setProperties(Properties properties) {
		Interceptor.super.setProperties(properties);
	}
}
