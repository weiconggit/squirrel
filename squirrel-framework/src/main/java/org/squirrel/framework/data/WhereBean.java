package org.squirrel.framework.data;

import java.util.Arrays;
import java.util.List;

/**
 * @description where条件参数
 * @author weicong
 * @time   2022年10月10日
 * @version 1.0
 */
public class WhereBean {

    public static final String EQ = "=";
    public static final String NE = "!=";
    public static final String GT = "<![CDATA[>]]>";
    public static final String GTE = "<![CDATA[>=]]>";
    public static final String LT = "<![CDATA[<]]>";
    public static final String LTE = "<![CDATA[<=]]>";
    public static final String LIKE = "LIKE";
    public static final String IN = "IN";
    public static final List<String> ALL_TYPE = Arrays.asList(EQ, NE, GT, GTE, LT, LTE, LIKE, IN);

    private final String name; // 字段名，sys_name
    private final Object value; // 字段对应值
    private final String type; // 条件类型，=,>,<,!=,like等，可任意输入，但需确保能sql正确

    public WhereBean(String name, Object value) {
        this.name = name;
        this.value = value;
        // 默认类型为相等
        this.type = EQ;
    }

    public WhereBean(String name, Object value, String type) {
        this.name = name;
        this.value = value;
        // 类型错误，默认类型为相等
        if (ALL_TYPE.contains(type)) {
            this.type = type;
        } else {
            this.type = EQ;
        }
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

}
