package org.squirrel.sys.menu;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * <p>菜单信息 实体 bean</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@Deprecated
@ApiModel(value = "菜单信息", subTypes = MenuVO.class)
public class Menu {

	@ApiModelProperty("ID")
    private Long id;
    
	@ApiModelProperty("菜单权限id")
	@NotNull
	@NotEmpty
	@Length(min = 1, max = 32)
    private String menuId;
    
	@ApiModelProperty("菜单名称")
	@MaxLength(value = 32)
    private String name;
    
	@ApiModelProperty("菜单类型")
    private Boolean type;
    
	@ApiModelProperty("父类菜单id")
    private Integer parentId;
    
	@ApiModelProperty("后端接口地址")
	@MaxLength(value = 128)
    private String backUrl;
    
	@ApiModelProperty("前端路由地址")
	@MaxLength(value = 128)
    private String frontUrl;
    
	@ApiModelProperty("图标")
	@MaxLength(value = 32)
    private String icon;
    
	@ApiModelProperty("排序")
    private Integer sort;
    
	@ApiModelProperty("删除标识，0：正常，1：删除")
    private Boolean isdel;
    
	@ApiModelProperty("创建者")
    private Integer creator;
    
	@ApiModelProperty("创建时间 2038年后失效")
    private LocalDateTime createTime;
    
	@ApiModelProperty("最后更新时间 2038年后失效")
    private LocalDateTime updateTime;
    

	/**
	 * set ID
	 * @return Menu
	 */
	public Menu setId(Long id){
		this.id = id;
		return this;
	}
	/**
	 * get ID
	 * @return Menu
	 */
	public Long getId(){
		return this.id;
	}
	
	/**
	 * set 菜单权限id
	 * @return Menu
	 */
	public Menu setMenuId(String menuId){
		this.menuId = menuId;
		return this;
	}
	/**
	 * get 菜单权限id
	 * @return Menu
	 */
	public String getMenuId(){
		return this.menuId;
	}
	
	/**
	 * set 菜单名称
	 * @return Menu
	 */
	public Menu setName(String name){
		this.name = name;
		return this;
	}
	/**
	 * get 菜单名称
	 * @return Menu
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * set 菜单类型
	 * @return Menu
	 */
	public Menu setType(Boolean type){
		this.type = type;
		return this;
	}
	/**
	 * get 菜单类型
	 * @return Menu
	 */
	public Boolean getType(){
		return this.type;
	}
	
	/**
	 * set 父类菜单id
	 * @return Menu
	 */
	public Menu setParentId(Integer parentId){
		this.parentId = parentId;
		return this;
	}
	/**
	 * get 父类菜单id
	 * @return Menu
	 */
	public Integer getParentId(){
		return this.parentId;
	}
	
	/**
	 * set 后端接口地址
	 * @return Menu
	 */
	public Menu setBackUrl(String backUrl){
		this.backUrl = backUrl;
		return this;
	}
	/**
	 * get 后端接口地址
	 * @return Menu
	 */
	public String getBackUrl(){
		return this.backUrl;
	}
	
	/**
	 * set 前端路由地址
	 * @return Menu
	 */
	public Menu setFrontUrl(String frontUrl){
		this.frontUrl = frontUrl;
		return this;
	}
	/**
	 * get 前端路由地址
	 * @return Menu
	 */
	public String getFrontUrl(){
		return this.frontUrl;
	}
	
	/**
	 * set 图标
	 * @return Menu
	 */
	public Menu setIcon(String icon){
		this.icon = icon;
		return this;
	}
	/**
	 * get 图标
	 * @return Menu
	 */
	public String getIcon(){
		return this.icon;
	}
	
	/**
	 * set 排序
	 * @return Menu
	 */
	public Menu setSort(Integer sort){
		this.sort = sort;
		return this;
	}
	/**
	 * get 排序
	 * @return Menu
	 */
	public Integer getSort(){
		return this.sort;
	}
	
	/**
	 * set 删除标识，0：正常，1：删除
	 * @return Menu
	 */
	public Menu setIsdel(Boolean isdel){
		this.isdel = isdel;
		return this;
	}
	/**
	 * get 删除标识，0：正常，1：删除
	 * @return Menu
	 */
	public Boolean getIsdel(){
		return this.isdel;
	}
	
	/**
	 * set 创建者
	 * @return Menu
	 */
	public Menu setCreator(Integer creator){
		this.creator = creator;
		return this;
	}
	/**
	 * get 创建者
	 * @return Menu
	 */
	public Integer getCreator(){
		return this.creator;
	}
	
	/**
	 * set 创建时间 2038年后失效
	 * @return Menu
	 */
	public Menu setCreateTime(LocalDateTime createTime){
		this.createTime = createTime;
		return this;
	}
	/**
	 * get 创建时间 2038年后失效
	 * @return Menu
	 */
	public LocalDateTime getCreateTime(){
		return this.createTime;
	}
	
	/**
	 * set 最后更新时间 2038年后失效
	 * @return Menu
	 */
	public Menu setUpdateTime(LocalDateTime updateTime){
		this.updateTime = updateTime;
		return this;
	}
	/**
	 * get 最后更新时间 2038年后失效
	 * @return Menu
	 */
	public LocalDateTime getUpdateTime(){
		return this.updateTime;
	}
	
	@Override
	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append("Menu:[");
		sb.append(", id="+this.getId());
		sb.append(", menuId="+this.getMenuId());
		sb.append(", name="+this.getName());
		sb.append(", type="+this.getType());
		sb.append(", parentId="+this.getParentId());
		sb.append(", backUrl="+this.getBackUrl());
		sb.append(", frontUrl="+this.getFrontUrl());
		sb.append(", icon="+this.getIcon());
		sb.append(", sort="+this.getSort());
		sb.append(", isdel="+this.getIsdel());
		sb.append(", creator="+this.getCreator());
		sb.append(", createTime="+this.getCreateTime());
		sb.append(", updateTime="+this.getUpdateTime());
		sb.append("]");
		return sb.toString();
	}

}
