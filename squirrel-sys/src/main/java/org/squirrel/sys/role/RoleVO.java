package org.squirrel.sys.role;

import java.util.List;

import org.squirrel.sys.rolemenu.RoleMenuVO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>角色信息 视图VO bean</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@TableName("role")
public class RoleVO extends Role {

	// transient 此处只是标识，不需要 Serializable，也可以用 @TableField(exist = false)
	@TableField(exist = false)
	private List<RoleMenuVO> voRoleMenus;

	public List<RoleMenuVO> getVoRoleMenus() {
		return voRoleMenus;
	}

	public void setVoRoleMenus(List<RoleMenuVO> voRoleMenus) {
		this.voRoleMenus = voRoleMenus;
	}
	

	
}
