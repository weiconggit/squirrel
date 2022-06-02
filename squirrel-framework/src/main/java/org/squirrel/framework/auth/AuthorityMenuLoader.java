package org.squirrel.framework.auth;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.squirrel.framework.SquirrelProperties;
import org.squirrel.framework.auth.annotation.Authority;
import org.squirrel.framework.spring.ApplicationContextHelper;
import org.squirrel.framework.util.StrUtil;

/**
 * 权限信息管理
 * @author weicong
 * @time   2021年1月21日
 * @version 1.0
 */
public final class AuthorityMenuLoader {

	private AuthorityMenuLoader(){}

	/**通用权限标识查询*/
	public static final String GET = "get";
	/**通用权限标识新增*/
	public static final String ADD = "add";
	/**通用权限标识编辑*/
	public static final String EDIT = "edit";
	/**通用权限标识删除*/
	public static final String DEL = "del";
	/** 权限菜单缓存 */
	private static List<AuthorityMenu> menusCache;

	/**
	 * 加载所有权限菜单
	 * @return
	 */
	public static List<AuthorityMenu> loadMenus(){
		// 优先使用缓存
		if (menusCache != null) {
			return menusCache;
		}
		List<AuthorityMenu> menus = new ArrayList<>();
		Map<String, Object> beansWithAnnotation = ApplicationContextHelper.getContext().getBeansWithAnnotation(Authority.class);
		for (Object object : beansWithAnnotation.values()) {
			Class<?> clazz = object.getClass();
			Class<?> superclass = clazz.getSuperclass();
			Authority authAnnotation = clazz.getDeclaredAnnotation(Authority.class);
			// 没有该注解的不处理
			String authValue = authAnnotation.value();
			// 没有auth注解值，则不处理
			if (StrUtil.isEmpty(authValue)) {
				continue;
			}
			AuthorityMenu authMenu = new AuthorityMenu(authValue, SquirrelProperties.get(authValue), "0", null, null);
			// 通用controller中的方法处理
			if (superclass != null) {
				loadMenu(superclass, authMenu);
			}
			// 自身中的方法处理
			loadMenu(clazz, authMenu);
			menus.add(authMenu);
		}
		menusCache = menus;
		return menus;
	}
	
	/**
	 * 加载一个类中的权限菜单
	 * @param clazz controller class
	 * @param authMenu 父类菜单
	 */
	private static void loadMenu(Class<?> clazz, AuthorityMenu authMenu) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			String authId = null; // 父级菜单id+authVal
			String authVal = null;
			String rqUri = null;
			String rqMethod = null;
			Annotation[] meAnnotations = method.getDeclaredAnnotations();
			for (Annotation annotation : meAnnotations) {
				if (annotation instanceof GetMapping) {
					GetMapping getMapping = (GetMapping)annotation;
					rqUri = getUri(getMapping.value());
					rqMethod = "GET";
				} else if (annotation instanceof PostMapping) {
					PostMapping postMapping = (PostMapping)annotation;
					rqUri = getUri(postMapping.value());
					rqMethod = "POST";
				} else if (annotation instanceof PutMapping) {
					PutMapping putMapping = (PutMapping)annotation;
					rqUri = getUri(putMapping.value());
					rqMethod = "PUT";
				} else if (annotation instanceof DeleteMapping) {
					DeleteMapping deleteMapping = (DeleteMapping)annotation;
					rqUri = getUri(deleteMapping.value());
					rqMethod = "DELETE";
				} else if (annotation instanceof Authority) {
					Authority auth = (Authority)annotation;
					authVal = auth.value();
					authId = authMenu.getId() + auth.value();
				}
			}
			if (authId != null && authVal != null && rqUri != null && rqMethod != null) {
				rqUri = rqMethod + "/" + authMenu.getId() + "/" + rqUri;
				AuthorityMenu child = new AuthorityMenu(authId, SquirrelProperties.get(authVal), "1", rqUri, rqMethod);
				authMenu.getChildren().add(child);
			}
		}
	}
	
	private static final String getUri(String[] uri) {
		if (uri != null) {
			return uri[0];
		}
		return "";
	}
}
