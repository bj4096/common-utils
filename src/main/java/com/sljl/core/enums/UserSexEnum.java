package com.sljl.core.enums;

/**
 * 用户性别枚举类
 *
 * @author L.Y.F
 */
public enum UserSexEnum {

    /**
     * 性别-未知
     */
    UNKNOWN(-1, "未知"),
    /**
     * 性别-男
     */
    MALE(1, "男"),
    /**
     * 性别-女
     */
    FEMALE(2, "女"),
    ;


    private Integer code;
    private String name;

    private UserSexEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UserSexEnum getEnum(Integer code) {
        for (UserSexEnum userSex : UserSexEnum.values()) {
            if (userSex.code.equals(code)) {
                return userSex;
            }
        }
        return UserSexEnum.UNKNOWN;
    }

}