package org.squirrel.framework.database;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.squirrel.framework.SquirrelInitializer;
import org.squirrel.framework.database.data.DataConstant;
import org.squirrel.framework.spring.ApplicationContextHelper;
import org.squirrel.framework.util.StrUtil;

/**
 * @description 
 * @author weicong
 * @time   2022年2月6日 上午11:24:17
 * @version 1.0
 */
public abstract class AbstractBaseService<T> implements BaseService<T>, SquirrelInitializer {

	/** dao实现 */
	private BaseDao<T> baseDao;
	
	@Override
	public BaseDao<T> getBaseDao() {
		return baseDao;
	}

	@Override
	public void init() {
		Type[] actualTypeArguments = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		Class<T> classVO = (Class<T>) actualTypeArguments[0];
		String simpleName = classVO.getSimpleName();
		String daoSuffix = DataConstant.DAO_SUFFIX;
		String beanSuffix = DataConstant.BEAN_SUFFIX;
		String daoName = simpleName.substring(0, simpleName.length() - beanSuffix.length()) + daoSuffix;
		daoName = StrUtil.lowerFirstLetter(daoName);
		this.baseDao = (BaseDao<T>)ApplicationContextHelper.getBean(daoName);
	}
	
}
