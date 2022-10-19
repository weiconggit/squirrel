package org.squirrel.framework.data;

/**
 * @description order条件参数
 * @author weicong
 * @time   2022年10月11日
 * @version 1.0
 */
public class OrderBean {

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private final String name; // 字段名称，sys_name
    private final String type; // 排序类型，升序：ASC，降序：DESC

    public OrderBean(String name, String type) {
        this.name = name;
        // 类型校验，错误类型，直接默认升序
        if (ASC.equals(type) || DESC.equals(type)) {
            this.type = type;
        } else {
            this.type = ASC;
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
