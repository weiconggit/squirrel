package org.squirrel.framework.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * @description
 * @author weicong
 * @time 2021年3月20日 上午10:54:35
 * @version 1.0
 */
public class PageResultHandler implements ResultHandler<Object> {

	private final List<Object> list;

	public PageResultHandler() {
		list = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public PageResultHandler(ObjectFactory objectFactory) {
		list = objectFactory.create(List.class);
	}

	@Override
	public void handleResult(ResultContext<?> context) {
		list.add(context.getResultObject());
	}

	public List<Object> getResultList() {
		return list;
	}

}
