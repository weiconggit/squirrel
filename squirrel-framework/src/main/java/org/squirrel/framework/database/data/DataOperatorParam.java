package org.squirrel.framework.database.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 数据操作参数
 * @author weicong
 * @time   2021年7月24日 下午6:39:49
 * @version 1.0
 */
public final class DataOperatorParam {

	
	private String tableName; // 表名：sys_user
	// 新增
	private List<String> insertKeys; // 表字段名，插入头部sql: (name1,name2,...)
	private List<List<Object>> values; // 表字段对应多个对象值
	// 更新
	private List<ParamKeyValue> setKeyValues; // 表字段对应值：name1 = "name1Value",...
	// 查询
	private List<String> selectKeys; // 表字段名，查询头部sql: name1,name2,name3,...
	// 更新、删除、查询
	private List<ParamKeyValue> whereKeyValues; // 表字段对应值：name1 = "name1Value",...
	
	/**
	 * 添加查询的条件键值对
	 * @param key
	 * @param value
	 */
	public void addWhereKeyValues(String key, Object value) {
		if (this.whereKeyValues == null) {
			this.whereKeyValues = new ArrayList<>();
		}
		this.whereKeyValues.add(new ParamKeyValue(key, value));
	}
	
	/**
	 * 添加更新的set键值对
	 * @param key
	 * @param value
	 */
	public void addSetKeyValue(String key, Object value) {
		if (this.setKeyValues == null) {
			this.setKeyValues = new ArrayList<>();
		}
		this.setKeyValues.add(new ParamKeyValue(key, value));
	}
	
	public final String getTableName() {
		return tableName;
	}

	public final void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getInsertKeys() {
		return insertKeys;
	}

	public void setInsertKeys(List<String> insertKeys) {
		this.insertKeys = insertKeys;
	}

	public List<List<Object>> getValues() {
		return values;
	}
	
	public void setValues(List<List<Object>> values) {
		this.values = values;
	}
	
    public List<ParamKeyValue> getSetKeyValues() {
		return setKeyValues;
	}

	public void setSetKeyValues(List<ParamKeyValue> setKeyValues) {
		this.setKeyValues = setKeyValues;
	}

	public List<ParamKeyValue> getWhereKeyValues() {
		return whereKeyValues;
	}

	public void setWhereKeyValues(List<ParamKeyValue> whereKeyValues) {
		this.whereKeyValues = whereKeyValues;
	}

	public List<String> getSelectKeys() {
		return selectKeys;
	}

	public void setSelectKeys(List<String> selectKeys) {
		this.selectKeys = selectKeys;
	}

	/**
     * @description 参数构建
     * @author weicong
     * @time   2022年1月23日 下午5:11:13
     * @version 1.0
     */
    private final class ParamKeyValue {

        private String key; // 字段名
        private Object value; // 字段值
                
        public ParamKeyValue(String key, Object value) {
            super();
            this.key = key;
            this.value = value;
        }
        
        public String getKey() {
            return key;
        }
        public Object getValue() {
            return value;
        }
    }
    
}
