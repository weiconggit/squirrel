package org.squirrel.sys.userrole;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * <p>用户角色关联表 实体 bean</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@ApiModel(value = "用户角色关联表", subTypes = UserRoleVO.class)
public class UserRole {

	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty("ID")
    private Long id;
    
	@ApiModelProperty("角色id")
	@NotNull
	@NotEmpty
	@Length(min = 1, max = 32)
    private Long roleId;
    
	@ApiModelProperty("用户id")
	@NotNull
	@NotEmpty
	@Length(min = 1, max = 32)
    private Long userId;
    

	/**
	 * set ID
	 * @return UserRole
	 */
	public UserRole setId(Long id){
		this.id = id;
		return this;
	}
	/**
	 * get ID
	 * @return UserRole
	 */
	public Long getId(){
		return this.id;
	}
	
	/**
	 * set 角色id
	 * @return UserRole
	 */
	public UserRole setRoleId(Long roleId){
		this.roleId = roleId;
		return this;
	}
	/**
	 * get 角色id
	 * @return UserRole
	 */
	public Long getRoleId(){
		return this.roleId;
	}
	
	/**
	 * set 用户id
	 * @return UserRole
	 */
	public UserRole setUserId(Long userId){
		this.userId = userId;
		return this;
	}
	/**
	 * get 用户id
	 * @return UserRole
	 */
	public Long getUserId(){
		return this.userId;
	}
	
	@Override
	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append("UserRole:[");
		sb.append(", id="+this.getId());
		sb.append(", roleId="+this.getRoleId());
		sb.append(", userId="+this.getUserId());
		sb.append("]");
		return sb.toString();
	}

}
