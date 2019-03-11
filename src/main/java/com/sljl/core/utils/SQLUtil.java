package com.sljl.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;


/**
 * 专门处理SQL语句的工具类
 *
 * @author L.Y.F
 */
public class SQLUtil {

    private final static String SQL_INJECTION = "'|%|--|;|and|or|not|use|insert|delete|update|select|count|union|create|drop|truncate|alter|grant|execute|exec|call|declare|source";

    /**
     * 生成数据库自定义主键
     *
     * @return
     */
    public static String generatePrimaryKey() {
        return StrUtil.generateUUID().toUpperCase().replaceAll("-", "");
    }

    /**
     * 转义英文单引号
     *
     * @param value
     *
     * @return
     */
    public static String singleQuotesFilter(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replaceAll("'", "\\\\'");
        }
        return value;
    }

    /**
     * 把java.sql.Date转换成java.util.Date
     *
     * @param date
     *
     * @return
     */
    public static Date convertUtilDate(java.sql.Date date) {
        if (null == date) {
            return null;
        }
        return new Date(date.getTime());
    }

    /**
     * 过滤Sql语句的拼接参数部分，防止拼接部分被SQL注入
     *
     * @param sqlParam
     *
     * @return
     */
    public static String sqlInjectionFilter(String sqlParam) {
        if (StringUtils.isBlank(sqlParam)) {
            return sqlParam;
        }
        // (?i)不区分大小写替换
        return sqlParam.replaceAll("(?i)" + SQL_INJECTION, "");
    }

    public static void main(String[] args) {
        System.out.println(generatePrimaryKey());
    }

}
