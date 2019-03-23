package com.zcx.rocket.base;

/**
 * rocketmq 开关
 * @author wlk
 */
public enum SwitchEnum {
    on("1"),off("0");
    private String code;

    SwitchEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
