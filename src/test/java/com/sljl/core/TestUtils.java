package com.sljl.core;

import com.google.common.collect.Maps;
import com.sljl.core.utils.HttpClientUtil;

import java.util.Map;

/**
 * @author L.Y.F
 * @create 2019-03-08 14:51
 */
public class TestUtils {

    public static void main(String[] args) {
        String url = "http://passport.hulian120.com/web?operation=sendValidationNoByPc";
        String phone = "17365678321";

        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Host", "passport.hulian120.com");
        headerMap.put("Origin", "http://passport.hulian120.com");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headerMap.put("Referer", "http://passport.hulian120.com/view/forgotPassword.jsp?service=http://edu.hulian120.com:80/");
        headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        headerMap.put("Cookie", "UM_distinctid=1697651145a2ba-02eb6403e8f66c-36657905-13c680-1697651145b138;");
        headerMap.put("Accept", "*/*");

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("phone", phone);

        String httpResponseResult = HttpClientUtil.getResultByHttpPost(url, paramMap, headerMap, null);
        System.out.println(httpResponseResult);
    }
}
