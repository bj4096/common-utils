package com.sljl.core.utils;

public class Test {

    public final static String regex = "'|%|--|;|and|or|not|use|insert|delete|update|select|count|group|union" + "|create|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|source";

    /**
     * 把SQL关键字替换为空字符串
     *
     * @param param
     *
     * @return
     */
    public static String filter(String param) {
        if (param == null) {
            return param;
        }
        return param.replaceAll("(?i)" + regex, ""); // (?i)不区分大小写替换
    }

    public static void main(String[] args) {
        // System.out.println(StringEscapeUtils.escapeSql("1' or '1' = '1; drop table test"));
        // //1'' or ''1'' = ''1; drop table test
        String str = "sElect * from test ;-- where id = 1 And name != 'sql' ";
        String outStr = "";
        for (int i = 0; i < 1000; i++) {
            outStr = Test.filter(str);
            System.out.println(outStr);
        }
    }

}
