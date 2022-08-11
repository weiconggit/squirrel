package org.squirrel.framework.data;

import org.squirrel.framework.SquirrelProperties;

/**
 * @description 
 * @author weicong
 * @time   2022年2月12日 下午1:18:43
 * @version 1.0
 */
public final class DataConstant {

	private DataConstant() {}
	
	public static final String DATA_ID = SquirrelProperties.get("dataId");
	public static final String DATA_IS_DEL = SquirrelProperties.get("dataIsDel");
	public static final String DAO_SUFFIX = SquirrelProperties.get("daoSuffix");
	public static final String SERVICE_SUFFIX = SquirrelProperties.get("serviceSuffix");
	public static final String BEAN_SUFFIX = SquirrelProperties.get("beanSuffix");
//	public static final String AUTH_ADD = SquirrelProperties.get("add");
//	public static final String EDIT = SquirrelProperties.get("edit");
//	public static final String DEL = SquirrelProperties.get("del");
//	public static final String GET = SquirrelProperties.get("get");

}
