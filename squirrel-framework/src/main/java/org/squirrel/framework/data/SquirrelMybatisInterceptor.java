package org.squirrel.framework.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 
 * @author weicong
 * @time   2022年2月12日 下午7:30:09
 * @version 1.0
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
	})
public class SquirrelMybatisInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(SquirrelMybatisInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		ParameterHandler parameterHandler = (ParameterHandler)metaObject.getValue("delegate.parameterHandler");
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>)parameterHandler.getParameterObject();
		// 从方法参数中寻找BasePage对象，若有则开启分页查询
		BasePage<?> basePage = null;
		for (Object param : params.values()) {
			if (param instanceof BasePage) {
				basePage = (BasePage<?>)param;
				break;
			}
		}
		if (basePage != null) {
			String pageSql = (String) metaObject.getValue("delegate.boundSql.sql");
			int total = getTotal(invocation, statementHandler, pageSql);
			if (total != 0) {
				basePage.setTotal(total);
				pageSql += " limit "+ basePage.getOffset() + "," + basePage.getLimit();
				metaObject.setValue("delegate.boundSql.sql", pageSql);
			}
		}
		return invocation.proceed();
    }
    
    /**
     * 查询总数
     * @param invocation
     * @param statementHandler
     * @param sql
     * @return
     */
    private int getTotal(Invocation invocation, StatementHandler statementHandler, String sql) {
        Connection connection = (Connection) invocation.getArgs()[0];
        // 从sql结果中再次查询就是总数
        String countSql = "select count(0) from (" + sql + ") as total";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total = 0;
        try {
        	ps = connection.prepareStatement(countSql);
        	statementHandler.getParameterHandler().setParameters(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
            	total = rs.getInt(1);
    		}
		} catch (Exception e) {
			log.error("count error", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				log.error("close error", e);
			}
		}
        return total;
    }

    @Override
    public Object plugin(Object object) {
    	// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (object instanceof StatementHandler) {
            return Plugin.wrap(object, this);
        } else {
            return object;
        }
    }

}
