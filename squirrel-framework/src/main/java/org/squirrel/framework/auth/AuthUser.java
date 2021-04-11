package org.squirrel.framework.auth;

import java.util.Set;

/**
 * @author weicong
 * @time   2021年1月21日
 * @version 1.0
 */
public class AuthUser {

	private String id;
	private String name;
	private Set<String> roleIds;
	private Set<String> menuIds;
	private Set<String> staticResource;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public AuthUser setId(String id) {
		this.id = id;
		return this;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public AuthUser setName(String name) {
		this.name = name;
		return this;
	}
	/**
	 * @return the roleIds
	 */
	public Set<String> getRoleIds() {
		return roleIds;
	}
	/**
	 * @param roleIds the roleIds to set
	 */
	public AuthUser setRoleIds(Set<String> roleIds) {
		this.roleIds = roleIds;
		return this;
	}
	/**
	 * @return the menuIds
	 */
	public Set<String> getMenuIds() {
		return menuIds;
	}
	/**
	 * @param menuIds the menuIds to set
	 */
	public AuthUser setMenuIds(Set<String> menuIds) {
		this.menuIds = menuIds;
		return this;
	}
	/**
	 * @return the staticResource
	 */
	public Set<String> getStaticResource() {
		return staticResource;
	}
	/**
	 * @param staticResource the staticResource to set
	 */
	public AuthUser setStaticResource(Set<String> staticResource) {
		this.staticResource = staticResource;
		return this;
	}
	
}
