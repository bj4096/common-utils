package com.sljl.core.enums;


/**
 * 生效属相枚举类
 *
 * @author L.Y.F
 */
public enum ZodiacTypeEnum {

    /**
     * 未知星座
     */
    ZODIAC_UNKNOWN(-1, "未知"),
    /**
     * 白羊座
     */
    ZODIAC_1(1, "鼠"),
    /**
     * 金牛座
     */
    ZODIAC_2(2, "牛"),
    /**
     * 双子座
     */
    ZODIAC_3(3, "虎"),
    /**
     * 巨蟹座
     */
    ZODIAC_4(4, "兔"),
    /**
     * 狮子座
     */
    ZODIAC_5(5, "龙"),
    /**
     * 处女座
     */
    ZODIAC_6(6, "蛇"),
    /**
     * 天秤座
     */
    ZODIAC_7(7, "马"),
    /**
     * 天蝎座
     */
    ZODIAC_8(8, "羊"),
    /**
     * 射手座
     */
    ZODIAC_9(9, "猴"),
    /**
     * 摩羯座
     */
    ZODIAC_10(10, "鸡"),
    /**
     * 水瓶座
     */
    ZODIAC_11(11, "狗"),
    /**
     * 双鱼座
     */
    ZODIAC_12(12, "猪"),
    ;

    private Integer code;
    private String value;

    private ZodiacTypeEnum(Integer code, String value) {
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
     * 默认返回“未知属相”
     *
     * @param code
     *
     * @return
     */
    public static ZodiacTypeEnum getEnum(Integer code) {
        for (ZodiacTypeEnum zodiacType : ZodiacTypeEnum.values()) {
            if (zodiacType.code == code) {
                return zodiacType;
            }
        }
        return ZODIAC_UNKNOWN;
    }

    /**
     * 默认返回“未知属相”
     *
     * @param value
     *
     * @return
     */
    public static ZodiacTypeEnum getEnum(String value) {
        for (ZodiacTypeEnum zodiacType : ZodiacTypeEnum.values()) {
            if (zodiacType.value.equals(value)) {
                return zodiacType;
            }
        }
        return ZODIAC_UNKNOWN;
    }

}
