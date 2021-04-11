package org.squirrel.generator.data;

/**
 * 通用数据表结构字段
 * @author weicong
 * @time   2021年1月7日
 * @version 1.0
 */
public class ColumnData {
	
	private String name;		// 首字小写的驼峰命令
	private String nameHump;	// 首字大写的驼峰命令
	private String nameCN;		
	private boolean isNullable; 
	private String type;		// 数据类型，varchar string
	private int length;			// 字段长度
	private String index;		// 索引
	private String comment;		// 备注信息
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the nameHump
	 */
	public String getNameHump() {
		return nameHump;
	}
	/**
	 * @param nameHump the nameHump to set
	 */
	public void setNameHump(String nameHump) {
		this.nameHump = nameHump;
	}
	/**
	 * @return the nameCN
	 */
	public String getNameCN() {
		return nameCN;
	}
	/**
	 * @param nameCN the nameCN to set
	 */
	public void setNameCN(String nameCN) {
		this.nameCN = nameCN;
	}
	/**
	 * @return the isNullable
	 */
	public boolean isNullable() {
		return isNullable;
	}
	/**
	 * @param isNullable the isNullable to set
	 */
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "ColumnData [name=" + name + ", nameHump=" + nameHump + ", nameCN=" + nameCN + ", isNullable="
				+ isNullable + ", type=" + type + ", length=" + length + ", index=" + index + ", comment=" + comment
				+ "]";
	}

}
