package org.squirrel.sys.menu;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.auth.Auth;
import org.squirrel.framework.auth.AuthMenuLoader;
import org.squirrel.framework.auth.AuthMenu;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.util.ColUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>菜单信息 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Api(tags={"基础服务-菜单信息"})
@RestController
@RequestMapping(value = "menu")
public class MenuController {
	
	@Auth(AuthMenuLoader.GET)
	@ApiOperation(value = "获取所有权限菜单")
	@GetMapping(value = "all")
	public Rp<List<AuthMenu>> all(){
		List<AuthMenu> allMenus = AuthMenuLoader.getAllMenus();
		if (ColUtil.isEmpty(allMenus)) {
			return Rp.success(Collections.emptyList());
		}
		for (AuthMenu authMenu : allMenus) {
			List<AuthMenu> children = authMenu.getChildren();
			if (ColUtil.isNotEmpty(children)) {
				children = children.stream().distinct().collect(Collectors.toList());
				authMenu.setChildren(children);
			}
		}
		return Rp.success(allMenus);
	} 
}
