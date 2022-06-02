package org.squirrel.framework.auth;

import java.util.Set;

/**
 * @author weicong
 * @time   2021年1月21日
 * @version 1.0
 */
public class AuthorityUser {

	private String id; // 数据id
	private String name; // 用户名、昵称
	private Set<String> menuIds; // 权限菜单id集

	public String getId() {
		return id;
	}

	public AuthorityUser setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public AuthorityUser setName(String name) {
		this.name = name;
		return this;
	}


	public Set<String> getMenuIds() {
		return menuIds;
	}

	public AuthorityUser setMenuIds(Set<String> menuIds) {
		this.menuIds = menuIds;
		return this;
	}
	
}
