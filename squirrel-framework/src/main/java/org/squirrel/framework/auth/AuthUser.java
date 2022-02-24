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
	private Set<String> menuIds;

	public String getId() {
		return id;
	}

	public AuthUser setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public AuthUser setName(String name) {
		this.name = name;
		return this;
	}


	public Set<String> getMenuIds() {
		return menuIds;
	}

	public AuthUser setMenuIds(Set<String> menuIds) {
		this.menuIds = menuIds;
		return this;
	}
	
}
