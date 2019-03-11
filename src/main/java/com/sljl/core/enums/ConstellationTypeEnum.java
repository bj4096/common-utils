/**
 *
 */
package com.sljl.core.enums;

/**
 * 星座枚举类
 *
 * @author L.Y.F
 *
 */
public enum ConstellationTypeEnum {

    /** 未知星座 */
    CONSTELLATION_UNKNOWN(-1, "未知"),
    /** 白羊座 */
    CONSTELLATION_1(1, "白羊座"),
    /** 金牛座 */
    CONSTELLATION_2(2, "金牛座"),
    /** 双子座 */
    CONSTELLATION_3(3, "双子座"),
    /** 巨蟹座 */
    CONSTELLATION_4(4, "巨蟹座"),
    /** 狮子座 */
    CONSTELLATION_5(5, "狮子座"),
    /** 处女座 */
    CONSTELLATION_6(6, "处女座"),
    /** 天秤座 */
    CONSTELLATION_7(7, "天秤座"),
    /** 天蝎座 */
    CONSTELLATION_8(8, "天蝎座"),
    /** 射手座 */
    CONSTELLATION_9(9, "射手座"),
    /** 摩羯座 */
    CONSTELLATION_10(10, "摩羯座"),
    /** 水瓶座 */
    CONSTELLATION_11(11, "水瓶座"),
    /** 双鱼座 */
    CONSTELLATION_12(12, "双鱼座"),
    ;

    private Integer code;
    private String value;

    private ConstellationTypeEnum(Integer code, String value) {
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
     * 默认返回“未知星座”
     * @param code
     * @return
     */
    public static ConstellationTypeEnum getEnum(Integer code) {
        for (ConstellationTypeEnum constellationType : ConstellationTypeEnum.values()) {
            if (constellationType.code == code) {
                return constellationType;
            }
        }
        return CONSTELLATION_UNKNOWN;
    }

    /**
     * 默认返回“未知星座”
     * @param value
     * @return
     */
    public static ConstellationTypeEnum getEnum(String value) {
        for (ConstellationTypeEnum constellationType : ConstellationTypeEnum.values()) {
            if (constellationType.value.equals(value)) {
                return constellationType;
            }
        }
        return CONSTELLATION_UNKNOWN;
    }

}
