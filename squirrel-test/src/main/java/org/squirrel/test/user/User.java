package org.squirrel.test.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * <p>用户信息 实体 bean</p>
 * @author weicong
 * @time   2021-02-21 
 * @version v1
 */
@ApiModel(value = "用户信息", subTypes = UserVO.class)
public class User{

//	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty("ID")
    private Long id;
    
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
    
//	@ApiModelProperty("昵称")
//	@MaxLength(value = 32)
//    private String nickname;
    
	@ApiModelProperty("电话号码")
	@MaxLength(value = 16)
    private String phone;
    
	@ApiModelProperty("头像")
	@MaxLength(value = 255)
    private String head;
    
//	@ApiModelProperty("微信openid")
//	@MaxLength(value = 32)
//    private String wxOpenid;
//
//	@ApiModelProperty("qq openid")
//	@MaxLength(value = 32)
//    private String qqOpenid;
    
	@ApiModelProperty("删除标识，0：正常，1：删除")
    private Boolean isdel;
    
//	@ApiModelProperty("创建者")
//	@MaxLength(value = 32)
//    private String creator;
    
//	@ApiModelProperty("创建时间")
//    private LocalDateTime createTime;
//
//	@ApiModelProperty("更新时间")
//    private LocalDateTime updateTime;
    

	/**
	 * set ID
	 * @return User
	 */
	public User setId(Long id){
		this.id = id;
		return this;
	}
	/**
	 * get ID
	 * @return User
	 */
	public Long getId(){
		return this.id;
	}
	
	/**
	 * set 用户名
	 * @return User
	 */
	public User setUsername(String username){
		this.username = username;
		return this;
	}
	/**
	 * get 用户名
	 * @return User
	 */
	public String getUsername(){
		return this.username;
	}
	
	/**
	 * set 密码
	 * @return User
	 */
	public User setPassword(String password){
		this.password = password;
		return this;
	}
	/**
	 * get 密码
	 * @return User
	 */
	public String getPassword(){
		return this.password;
	}
	
	/**
	 * set 昵称
	 * @return User
	 */
//	public User setNickname(String nickname){
//		this.nickname = nickname;
//		return this;
//	}
//	/**
//	 * get 昵称
//	 * @return User
//	 */
//	public String getNickname(){
//		return this.nickname;
//	}
	
	/**
	 * set 电话号码
	 * @return User
	 */
	public User setPhone(String phone){
		this.phone = phone;
		return this;
	}
	/**
	 * get 电话号码
	 * @return User
	 */
	public String getPhone(){
		return this.phone;
	}
	
	/**
	 * set 头像
	 * @return User
	 */
	public User setHead(String head){
		this.head = head;
		return this;
	}
	/**
	 * get 头像
	 * @return User
	 */
	public String getHead(){
		return this.head;
	}
	
	/**
	 * set 微信openid
	 * @return User
	 */
//	public User setWxOpenid(String wxOpenid){
//		this.wxOpenid = wxOpenid;
//		return this;
//	}
//	/**
//	 * get 微信openid
//	 * @return User
//	 */
//	public String getWxOpenid(){
//		return this.wxOpenid;
//	}
//
//	/**
//	 * set qq openid
//	 * @return User
//	 */
//	public User setQqOpenid(String qqOpenid){
//		this.qqOpenid = qqOpenid;
//		return this;
//	}
//	/**
//	 * get qq openid
//	 * @return User
//	 */
//	public String getQqOpenid(){
//		return this.qqOpenid;
//	}
	
	/**
	 * set 删除标识，0：正常，1：删除
	 * @return User
	 */
	public User setIsdel(Boolean isdel){
		this.isdel = isdel;
		return this;
	}
	/**
	 * get 删除标识，0：正常，1：删除
	 * @return User
	 */
	public Boolean getIsdel(){
		return this.isdel;
	}
	
	/**
	 * set 创建者
	 * @return User
	 */
//	public User setCreator(String creator){
//		this.creator = creator;
//		return this;
//	}
//	/**
//	 * get 创建者
//	 * @return User
//	 */
//	public String getCreator(){
//		return this.creator;
//	}
	
	/**
	 * set 创建时间
	 * @return User
	 */
//	public User setCreateTime(LocalDateTime createTime){
//		this.createTime = createTime;
//		return this;
//	}
//	/**
//	 * get 创建时间
//	 * @return User
//	 */
//	public LocalDateTime getCreateTime(){
//		return this.createTime;
//	}
//
//	/**
//	 * set 更新时间
//	 * @return User
//	 */
//	public User setUpdateTime(LocalDateTime updateTime){
//		this.updateTime = updateTime;
//		return this;
//	}
//	/**
//	 * get 更新时间
//	 * @return User
//	 */
//	public LocalDateTime getUpdateTime(){
//		return this.updateTime;
//	}
//
	@Override
	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append("User:[");
		sb.append(", id="+this.getId());
		sb.append(", username="+this.getUsername());
		sb.append(", password="+this.getPassword());
//		sb.append(", nickname="+this.getNickname());
		sb.append(", phone="+this.getPhone());
		sb.append(", head="+this.getHead());
//		sb.append(", wxOpenid="+this.getWxOpenid());
//		sb.append(", qqOpenid="+this.getQqOpenid());
		sb.append(", isdel="+this.getIsdel());
//		sb.append(", creator="+this.getCreator());
//		sb.append(", createTime="+this.getCreateTime());
//		sb.append(", updateTime="+this.getUpdateTime());
		sb.append("]");
		return sb.toString();
	}

}
