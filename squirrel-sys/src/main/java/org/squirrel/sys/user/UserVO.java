package org.squirrel.sys.user;

import java.util.List;

import org.squirrel.sys.userrole.UserRoleVO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>用户信息 视图VO bean</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@TableName("user")
public class UserVO extends User{

	@TableField(exist = false)
	private List<UserRoleVO> voUserRoles;

	public final List<UserRoleVO> getVoUserRoles() {
		return voUserRoles;
	}

	public final void setVoUserRoles(List<UserRoleVO> voUserRoles) {
		this.voUserRoles = voUserRoles;
	}
}
