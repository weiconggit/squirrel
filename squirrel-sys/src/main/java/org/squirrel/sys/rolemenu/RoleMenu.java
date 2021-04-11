package org.squirrel.sys.rolemenu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * <p>角色菜单关联表 实体 bean</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@ApiModel(value = "角色菜单关联表", subTypes = RoleMenuVO.class)
public class RoleMenu {

	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty("ID")
    private Long id;
    
	@ApiModelProperty("角色id")
	@NotNull
	@NotEmpty
	@Length(min = 1, max = 32)
    private Long roleId;
    
	@ApiModelProperty("菜单id")
	@NotNull
	@NotEmpty
	@Length(min = 1, max = 32)
    private String menuId;
    

	/**
	 * set ID
	 * @return RoleMenu
	 */
	public RoleMenu setId(Long id){
		this.id = id;
		return this;
	}
	/**
	 * get ID
	 * @return RoleMenu
	 */
	public Long getId(){
		return this.id;
	}
	
	/**
	 * set 角色id
	 * @return RoleMenu
	 */
	public RoleMenu setRoleId(Long roleId){
		this.roleId = roleId;
		return this;
	}
	/**
	 * get 角色id
	 * @return RoleMenu
	 */
	public Long getRoleId(){
		return this.roleId;
	}
	
	/**
	 * set 菜单id
	 * @return RoleMenu
	 */
	public RoleMenu setMenuId(String menuId){
		this.menuId = menuId;
		return this;
	}
	/**
	 * get 菜单id
	 * @return RoleMenu
	 */
	public String getMenuId(){
		return this.menuId;
	}
	
	@Override
	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append("RoleMenu:[");
		sb.append(", id="+this.getId());
		sb.append(", roleId="+this.getRoleId());
		sb.append(", menuId="+this.getMenuId());
		sb.append("]");
		return sb.toString();
	}

}
