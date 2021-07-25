package org.squirrel.sys.user;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.database.AbstractBaseController;

import io.swagger.annotations.Api;

/**
 * <p>用户信息 Cotroller</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
//@Api(tags={"基础服务-用户信息"})
//@RestController
//@RequestMapping(value = "user")
//public class UserController extends AbstractBaseController<UserVO>{
	public class UserController {
	
//	@Resource
//	private UserRoleService userRoleService;


//	@Override
	protected void afterSave(UserVO data) {
//		List<UserRoleVO> voUserRoles = data.getVoUserRoles();
//		if (ColUtil.isNotEmpty(voUserRoles)) {
//			voUserRoles.forEach(v -> v.setUserId(data.getId()));
//			boolean saveBatch = userRoleService.saveBatch(voUserRoles);
//			if (!saveBatch) {
//				// TODO
//				throw new BizException("关联角色新增失败");
//			}
//		}
	}
}
