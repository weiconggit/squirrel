package org.squirrel.framework.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.squirrel.framework.SquirrelInitializer;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.SquirrelComponent;
import org.squirrel.framework.spring.ApplicationContextHelper;
import org.squirrel.framework.util.StrUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description 
 * @author weicong
 * @time   2021年1月29日 下午11:32:06
 * @version 1.0
 */
@SquirrelComponent
public class AuthInterceptor implements HandlerInterceptor, SquirrelInitializer {

	private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
	
	public static final String AUTHORIZATION = "Authorization";
	public static final String IMG_URL_SIGN = "img";
	
	public static final String RESOURCE_TYPE_IMG = "img";
	public static final String RESOURCE_TYPE_MENU = "normal";
	
	public static final Map<String, AuthUser> TOKEN_USER = new ConcurrentHashMap<>();
	private static final Map<String, String> RESOURCE_GET = new HashMap<>();
	private static final Map<String, String> RESOURCE_NOT_GET = new HashMap<>();
		
	private ObjectMapper objectMapper;
	private AuthCache authCache;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("auth intercepte: {}", request.getRequestURI());
		return true;
		// 是否是静态资源请求，仅在无三方图片服务时提供
//		Object attribute = request.getAttribute(IMG_URL_SIGN);
//		if (attribute == null) {
//			boolean uriCheck = uriCheck(request);
//			if (!uriCheck) {
//				noAuth(response);
//			}
//			return uriCheck;
//		}
//		String imgUri = StrUtil.parseString(attribute);
//		boolean imgCheck = imgCheck(request, imgUri);
//		if (!imgCheck) {
//			noAuth(response);
//		}
//		return imgCheck;
	}
	
	private boolean uriCheck(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION);
		if (StrUtil.isEmpty(token)) {
			return false;
		}
		String method = request.getMethod();
		String uri = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
		String menuId = null;
		String key = new StringBuilder().append(method).append(uri).toString();
		if (method.equals("GET")) {
			menuId = RESOURCE_GET.get(key);
		} else {
			menuId = RESOURCE_NOT_GET.get(key);
		}
		if (StrUtil.isEmpty(menuId)) {
			return false;
		}
		AuthUser authUser = TOKEN_USER.get(token);
		if (authUser == null) {
			return false;
		}
		Set<String> menuIds = authUser.getMenuIds();
		if (menuIds != null && !menuIds.isEmpty() && menuIds.contains(menuId)) {
			return true;
		}
		return false;
	}

	private boolean imgCheck(HttpServletRequest request, String imgUri) {
		String token = StrUtil.parseString(request.getAttribute(AUTHORIZATION));
		if (token == null) {
			return false;
		}
		AuthUser authUser = TOKEN_USER.get(token);
		if (authUser == null) {
			return false;
		}
		Set<String> imgResource = authUser.getStaticResource();
		if (imgResource == null || imgResource.isEmpty()) {
			return false;
		}
		if (imgResource.contains(imgUri)) {
			return true;
		}
		return false;
	}

	@Override
	public void init() {
		authCache = ApplicationContextHelper.getBean(AuthCache.class);
		objectMapper = ApplicationContextHelper.getBean(ObjectMapper.class);
		List<AuthMenu> menus = AuthManager.getAllMenus();
		
		menus.forEach(parent -> {
			List<AuthMenu> children = parent.getChildren();
			if (children != null && !children.isEmpty()) {
				children.forEach(child -> {
					if ("GET".equals(child.getMethod())) {
						RESOURCE_GET.put(child.getUri(), child.getId());
					} else {
						RESOURCE_NOT_GET.put(child.getUri(), child.getId());
					}
				});
			}
		});
		// TODO del or log
//		RESOURCE_GET.forEach((k,v)->{
//			System.out.println("=" + k + "," + v);
//		});
//		RESOURCE_NOT_GET.forEach((k,v)->{
//			System.out.println("=" + k + "," + v);
//		});
	}
	
	/**
	 * 无权限
	 * @param response
	 */
	final void noAuth(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		try {
			response.getWriter().write(objectMapper.writeValueAsString(Rp.failed(RpEnum.NO_AUTHOR)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
