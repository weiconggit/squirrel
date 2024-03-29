package org.squirrel.framework.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限菜单
 * @author weicong
 * @time   2021年1月21日
 * @version 1.0
 */
public class AuthorityMenu {

	private String id; // 若是类为@RequestMapping值，若是方法则为@Auth值
	private String name; // 翻译
	private String type; // 0：普通菜单，1：uri菜单
	private String uri; // uri菜单的地址
	private String method; // uri菜单的请求方式
	private List<AuthorityMenu> children;

	public AuthorityMenu(String id, String name, String type, String uri, String method) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.uri = uri;
		this.method = method;
		this.children = new ArrayList<>();
	}

	public String getId() {
		return id;
	}
	public AuthorityMenu setId(String id) {
		this.id = id;
		return this;
	}
	
	public String getName() {
		return name;
	}
	public AuthorityMenu setName(String name) {
		this.name = name;
		return this;
	}
	
	public List<AuthorityMenu> getChildren() {
		return children;
	}
	public AuthorityMenu setChildren(List<AuthorityMenu> children) {
		this.children = children;
		return this;
	}
	
	public String getType() {
		return type;
	}
	public AuthorityMenu setType(String type) {
		this.type = type;
		return this;
	}
	
	public String getMethod() {
		return method;
	}
	public AuthorityMenu setMethod(String method) {
		this.method = method;
		return this;
	}
	
	public String getUri() {
		return uri;
	}
	public AuthorityMenu setUri(String uri) {
		this.uri = uri;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorityMenu other = (AuthorityMenu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AuthMenu [id=" + id + ", name=" + name + ", type=" + type + ", uri=" + uri + ", method=" + method
				+ ", children=" + children + "]";
	}
}
