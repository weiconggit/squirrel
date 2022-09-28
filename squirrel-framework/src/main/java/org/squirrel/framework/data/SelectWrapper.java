package org.squirrel.framework.data;

public class SelectWrapper {

    private String sql;

    public SelectWrapper() {
    }

    public SelectWrapper eq(String key, Object value){
        sql += key + " = " + value;
        return this;
    }

    public SelectWrapper ne(String key, Object value){
        sql += key + " <> " + value;
        return this;
    };

    public SelectWrapper and(){
        sql += " AND ";
        return this;
    }

    public SelectWrapper or(){
        sql += " OR ";
        return this;
    }

    public SelectWrapper frontParentheses(){
        sql += "(";
        return this;
    }

    public SelectWrapper backParentheses(){
        sql += ")";
        return this;
    }
}
