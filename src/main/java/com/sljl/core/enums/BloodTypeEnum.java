/**
 *
 */
package com.sljl.core.enums;

/**
 * 血型枚举类
 *
 * @author L.Y.F
 *
 */
public enum BloodTypeEnum {

    /** 未知血型 */
    BLOOD_UNKOWN(-1, "未知"),
    /** A型血 */
    BLOOD_A(1, "A型"),
    /** B型血 */
    BLOOD_B(2, "B型"),
    /** O型血 */
    BLOOD_O(3, "O型"),
    /** AB型血 */
    BLOOD_AB(4, "AB型"),
    ;

    private Integer code;
    private String value;

    private BloodTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    /**
     * 默认返回未知血型
     * @param code
     * @return
     */
    public static BloodTypeEnum getEnum(Integer code) {
        for (BloodTypeEnum bloodType : BloodTypeEnum.values()) {
            if (bloodType.code == code) {
                return bloodType;
            }
        }
        return BLOOD_UNKOWN;
    }

    /**
     * 默认返回未知血型
     * @param value
     * @return
     */
    public static BloodTypeEnum getEnum(String value) {
        for (BloodTypeEnum bloodType : BloodTypeEnum.values()) {
            if (bloodType.value.equals(value)) {
                return bloodType;
            }
        }
        return BLOOD_UNKOWN;
    }

}
