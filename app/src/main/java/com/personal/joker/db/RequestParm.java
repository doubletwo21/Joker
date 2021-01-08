package com.personal.joker.db;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/8
 * Time: 23:11
 *
 * @description: post 的参数
 */
public class RequestParm {
    private String key;
    private Object value;

    public RequestParm(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
