package org.squirrel.framework.data;

/**
 * @description 数据操作类型枚举
 * @author weicong
 * @time   2021年11月16日
 * @version 1.0
 */
public enum DataOperatorEnum {

    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
	SELECT("delete");

    private String type;

    DataOperatorEnum(String type){
        this.type = type;
    }
}
