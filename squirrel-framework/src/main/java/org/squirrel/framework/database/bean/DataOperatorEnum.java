package org.squirrel.framework.database.bean;

/**
 * @description 数据操作类型枚举
 * @author weicong
 * @time   2021年11月16日
 * @version 1.0
 */
public enum DataOpEnum {

    INSERT("add"),
    UPDATE("edit"),
    DELETE("delete");

    private String type;

    DataOpEnum(String type){
        this.type = type;
    }
}
