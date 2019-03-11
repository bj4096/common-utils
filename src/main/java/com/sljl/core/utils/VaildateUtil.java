package com.sljl.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统校验工具
 *
 * @author L.Y.F
 */
public class VaildateUtil {

    /**
     * 匹配6~16位字母、英文字符、英文字母、数字组成的密码
     *
     * @param passwd
     *
     * @return
     */
    public static boolean checkPasswd(String passwd) {
        if (StringUtils.isBlank(passwd)) {
            return false;
        }
        Pattern p = Pattern.compile(RegexUtil.PASSWD_REGEX);
        Matcher m = p.matcher(passwd);
        return m.matches();
    }

    /**
     * 校验给定的内容是否完全符合给定的regex正则规则
     *
     * @param regexRule
     * @param context
     *
     * @return
     */
    public static boolean isMatchRegex(String value, String regex) {
        regex = null == regex ? "" : regex;
        Matcher matcher = Pattern.compile(regex).matcher(value);
        return matcher.matches();
    }

    /**
     * 校验给定的内容是否包含符合给定regex正则规则的内容
     * <pre>
     * 该方法默认区分大小写匹配，常用方法，若需要匹配Emoji建议显示指定忽略大小写匹配
     * </pre>
     *
     * @param regexRule
     * @param context
     *
     * @return
     */
    public static boolean isRegexFilter(String value, String regex) {
        return isRegexFilter(value, regex, false);
    }

    /**
     * 校验给定的内容是否包含符合给定regex正则规则的内容
     *
     * @param regexRule
     * @param context
     * @param insensitiveCase 是否忽略大小写
     *
     * @return
     */
    public static boolean isRegexFilter(String value, String regex, boolean insensitiveCase) {
        Matcher matcher = null;
        if (insensitiveCase) {
            matcher = Pattern.compile(regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE).matcher(value);
        } else {
            matcher = Pattern.compile(regex).matcher(value);
        }
        return matcher.find();
    }

    /**
     * 校验字符串长度是否超过期望值
     * <pre>
     * 系统统一规定汉字和Emoji标签全部按照两个字符来计算长度
     * </pre>
     *
     * @param value
     * @param expectLen
     *
     * @return
     */
    public static boolean checkCharacterLengthMoreThan(String value, int expectLen) {
        int strLen = value.length();
        int chineseCnt = matchRegexCnt(value, RegexUtil.CHINESE_REGEX);
        int emojiCnt = matchRegexCnt(value, RegexUtil.EMOJI_REGEX);
        if (strLen + chineseCnt + emojiCnt > expectLen) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算给定字符串中，符合给定正则的匹配数量
     *
     * @param value
     * @param regex
     *
     * @return
     */
    private static int matchRegexCnt(String value, String regex) {
        int count = 0;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        String text = "\uD83C\uDFA6中国\uD860\uDD5Da\uD860\uDE07\uD860\uDEE2\uD863\uDCCA\uD863\uDCCD\uD863\uDCD2\uD867\uDD98";
        String passwd = "abs就是65s2";
        System.out.println(text);
        System.out.println(text.length());
        System.out.println(checkPasswd(passwd));
        System.out.println(isRegexFilter(text, "[A-Z]"));
        System.out.println(Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        System.out.println(checkCharacterLengthMoreThan("你s", 3));
    }

}
