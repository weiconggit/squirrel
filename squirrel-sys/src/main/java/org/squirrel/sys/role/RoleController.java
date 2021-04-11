package org.squirrel.sys.role;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.response.BizException;
import org.squirrel.framework.util.ColUtil;
import org.squirrel.framework.web.AbstractBaseController;
import org.squirrel.sys.rolemenu.RoleMenuService;
import org.squirrel.sys.rolemenu.RoleMenuVO;

import io.swagger.annotations.Api;

/**
 * <p>角色信息 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Api(tags={"基础服务-角色信息"})
@RestController
@RequestMapping(value = "role")
public class RoleController extends AbstractBaseController<RoleService, RoleVO>{

	@Resource
	private RoleMenuService roleMenuService;
	
	@Override
	protected void afterSave(RoleVO data) {
		List<RoleMenuVO> voRoleMenus = data.getVoRoleMenus();
		if (ColUtil.isNotEmpty(voRoleMenus)) {
			voRoleMenus.forEach(v -> v.setRoleId(data.getId()));
			boolean saveBatch = roleMenuService.saveBatch(voRoleMenus);
			if (!saveBatch) {
				// TODO
				throw new BizException("关联菜单新增失败");
			}
		}
	}
	
	
	
	
}
