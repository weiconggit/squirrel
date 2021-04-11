package org.squirrel.sys.role;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * <p>角色信息 实体 bean</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@ApiModel(value = "角色信息", subTypes = RoleVO.class)
public class Role {

	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty("ID")
    private Long id;
    
	@ApiModelProperty("角色名称")
	@NotNull
	@NotEmpty
	@Length(min = 1, max = 32)
    private String name;
    
	@ApiModelProperty("删除标识，0：正常，1：删除")
    private Boolean isdel;
    
	@ApiModelProperty("创建者")
	@MaxLength(value = 32)
    private String creator;
    
	@ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
	@ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    

	/**
	 * set ID
	 * @return Role
	 */
	public Role setId(Long id){
		this.id = id;
		return this;
	}
	/**
	 * get ID
	 * @return Role
	 */
	public Long getId(){
		return this.id;
	}
	
	/**
	 * set 角色名称
	 * @return Role
	 */
	public Role setName(String name){
		this.name = name;
		return this;
	}
	/**
	 * get 角色名称
	 * @return Role
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * set 删除标识，0：正常，1：删除
	 * @return Role
	 */
	public Role setIsdel(Boolean isdel){
		this.isdel = isdel;
		return this;
	}
	/**
	 * get 删除标识，0：正常，1：删除
	 * @return Role
	 */
	public Boolean getIsdel(){
		return this.isdel;
	}
	
	/**
	 * set 创建者
	 * @return Role
	 */
	public Role setCreator(String creator){
		this.creator = creator;
		return this;
	}
	/**
	 * get 创建者
	 * @return Role
	 */
	public String getCreator(){
		return this.creator;
	}
	
	/**
	 * set 创建时间
	 * @return Role
	 */
	public Role setCreateTime(LocalDateTime createTime){
		this.createTime = createTime;
		return this;
	}
	/**
	 * get 创建时间
	 * @return Role
	 */
	public LocalDateTime getCreateTime(){
		return this.createTime;
	}
	
	/**
	 * set 更新时间
	 * @return Role
	 */
	public Role setUpdateTime(LocalDateTime updateTime){
		this.updateTime = updateTime;
		return this;
	}
	/**
	 * get 更新时间
	 * @return Role
	 */
	public LocalDateTime getUpdateTime(){
		return this.updateTime;
	}
	
	@Override
	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append("Role:[");
		sb.append(", id="+this.getId());
		sb.append(", name="+this.getName());
		sb.append(", isdel="+this.getIsdel());
		sb.append(", creator="+this.getCreator());
		sb.append(", createTime="+this.getCreateTime());
		sb.append(", updateTime="+this.getUpdateTime());
		sb.append("]");
		return sb.toString();
	}

}
