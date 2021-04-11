package org.squirrel.framework.mybatis;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.util.Assert;

/**
 * @description
 * @author weicong
 * @time 2021年3月14日 下午3:40:58
 * @version 1.0
 */
public class SquirrelMapperFactoryBean<T extends BaseMapper<?, ?>> extends MapperFactoryBean<T> {

	public SquirrelMapperFactoryBean() {
	}

	public SquirrelMapperFactoryBean(Class<T> mapperInterface) {
		super(mapperInterface);
	}

	@Override
	protected void checkDaoConfig() {
		super.checkDaoConfig();
		
		Assert.notNull(super.getSqlSessionTemplate(), "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
		
		Class<T> mapperInterface = super.getMapperInterface();
		
		Assert.notNull(mapperInterface, "Property 'mapperInterface' is required");
		
		// isAssignableFrom 此处遇到BaseMapper自身时，并不能返回false
		if (BaseMapper.class.getName().equals(mapperInterface.getName())) {
			return;
		}
		Configuration configuration = getSqlSession().getConfiguration();
		// 继承BaseMapper的接口
		if (BaseMapper.class.isAssignableFrom(mapperInterface)) {
			SquirrelMapperLoader.addMappedStatements(mapperInterface, configuration);
		}
	}

}
