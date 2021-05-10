package org.squirrel.framework.auth;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.SquirrelProperties;
import org.squirrel.framework.spring.ApplicationContextHelper;
import org.squirrel.framework.util.StrUtil;

/**
 * 权限信息管理
 * @author weicong
 * @time   2021年1月21日
 * @version 1.0
 */
public final class AuthMenuLoader {

	/**通用权限标识查询*/
	public static final String GET = "get";
	/**通用权限标识新增*/
	public static final String ADD = "add";
	/**通用权限标识编辑*/
	public static final String EDIT = "edit";
	/**通用权限标识删除*/
	public static final String DEL = "del";
	
	private static List<AuthMenu> menus;
	
	/**
	 * 获取全部权限信息
	 * @return
	 */
	public static List<AuthMenu> getAllMenus(){
		if (menus == null) {
			menus = loadMenus();
		}
		return menus;
	}
	
	/**
	 * 加载所有权限菜单
	 * @return
	 */
	private static List<AuthMenu> loadMenus(){
		List<AuthMenu> menus = new ArrayList<>();
		Map<String, Object> beansWithAnnotation = ApplicationContextHelper.getContext().getBeansWithAnnotation(RestController.class);
		Iterator<Object> iterator = beansWithAnnotation.values().iterator();
		AuthMenu authMenu = null;
		
		while (iterator.hasNext()) {
			Class<?> clazz = iterator.next().getClass();
			Class<?> superclass = clazz.getSuperclass();
			
			RequestMapping requestMapping = clazz.getDeclaredAnnotation(RequestMapping.class);
			String moduleUri = null;
			if (requestMapping != null) {
				String[] value = requestMapping.value();
				moduleUri = value[0];
			}
			String langName = SquirrelProperties.get(moduleUri);
			langName = StrUtil.isEmpty(langName) ? moduleUri : langName;
			
			// 获取类方法对应的权限信息
			authMenu = new AuthMenu()
					.setId(moduleUri)
					.setName(langName)
					.setType("0")
					.setChildren(new ArrayList<>());
			if (superclass != null) {
				loadMenu(superclass, authMenu);
			}
			loadMenu(clazz, authMenu);
			menus.add(authMenu);
		}
		return menus;
	}
	
	/**
	 * 加载一个类中的权限菜单
	 * @param clazz
	 * @param authMenu
	 */
	private static final void loadMenu(Class<?> clazz, AuthMenu authMenu) {
		Method[] methods = clazz.getDeclaredMethods();
		
		for (Method method : methods) {
			String uri = null;
			String rqMethod = null;
			String authId = null; // 模块名称+authVal
			String authVal = null; 
			Annotation[] meAnnotations = method.getDeclaredAnnotations();
			for (Annotation annotation : meAnnotations) {
				
				if (annotation instanceof GetMapping) {
					GetMapping getMapping = (GetMapping)annotation;
					uri = getUri(getMapping.value());
					rqMethod = "GET";
				} else if (annotation instanceof PostMapping) {
					PostMapping postMapping = (PostMapping)annotation;
					uri = getUri(postMapping.value());
					rqMethod = "POST";
				} else if (annotation instanceof PutMapping) {
					PutMapping putMapping = (PutMapping)annotation;
					uri = getUri(putMapping.value());
					rqMethod = "PUT";
				} else if (annotation instanceof DeleteMapping) {
					DeleteMapping deleteMapping = (DeleteMapping)annotation;
					uri = getUri(deleteMapping.value());
					rqMethod = "DELETE";
				}
				if (annotation instanceof Auth) {
					Auth auth = (Auth)annotation;
					authVal = auth.value();
					authId = authMenu.getId() + auth.value();
				}
				
			}
			if (uri != null && rqMethod != null && authId != null && authVal != null) {
				uri = new StringBuilder().append(rqMethod).append("/")
						.append(authMenu.getId()).append("/")
						.append(uri).toString();
				AuthMenu child = new AuthMenu()
						.setId(authId)
						.setName(SquirrelProperties.get(authVal))
						.setType("1")
						.setMethod(rqMethod)
						.setUri(uri);
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
