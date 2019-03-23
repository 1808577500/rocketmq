package com.zcx.rocket.base;

public enum TopicEnum {
    Topic1("Topic1", "topic"),
    Topic2("Topic2", "根据topic和tag没有找到对应的消费服务");

    private String code;
    private String msg;

    private TopicEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
