package org.squirrel.framework.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * <p>登录用户信息</p>
 * @author weicong
 * @time   2022年2月24日
 * @version v1
 */
@ApiModel(value = "登录用户信息")
public class LoginUser {

	@ApiModelProperty("用户名")
	@NotNull
	@NotEmpty
	@Length(min = 1, max = 64)
    private String username;
    
	@ApiModelProperty("密码")
	@NotNull
	@NotEmpty
	@Length(min = 1, max = 64)
    private String password;
    
	@ApiModelProperty("电话号码")
	@MaxLength(value = 16)
    private String phone;
    
	@ApiModelProperty("微信openid")
	@MaxLength(value = 32)
    private String wxOpenid;

	@ApiModelProperty("qq openid")
	@MaxLength(value = 32)
    private String qqOpenid;

	public String getUsername() {
		return username;
	}

	public LoginUser setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public LoginUser setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public LoginUser setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getWxOpenid() {
		return wxOpenid;
	}

	public LoginUser setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
		return this;
	}

	public String getQqOpenid() {
		return qqOpenid;
	}

	public LoginUser setQqOpenid(String qqOpenid) {
		this.qqOpenid = qqOpenid;
		return this;
	}
}
