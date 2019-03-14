package com.sljl.core.utils;

import com.google.common.collect.Lists;
import com.sljl.core.enums.HttpContentTypeEnum;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 封装的HTTP请求工具类
 *
 * @author L.Y.F
 */
public class HttpClientUtil {

    private static CloseableHttpClient httpClient = HttpClientFactory.getTrustAllCloseableHttpClient();
    private static int CONNECT_TIMEOUT = (1000 * TimeUtil.SECOND) * 30;
    private static int CONNECT_REQUEST_TIMEOUT = (1000 * TimeUtil.SECOND) * 30;
    private static int SOCKET_TIMEOUT = (1000 * TimeUtil.SECOND) * 30;

    private static String DEFAULT_CHARSET = "UTF-8";

    /**
     * 发起HTTP GET请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     *
     * @return
     */
    public static String getResultByHttpGet(String url) {
        return getResultByHttpGet(url, null, null, DEFAULT_CHARSET);
    }

    /**
     * 发起HTTP GET请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param charset ： 编码格式
     *
     * @return
     */
    public static String getResultByHttpGet(String url, String charset) {
        return getResultByHttpGet(url, null, null, charset);
    }

    /**
     * 发起HTTP GET请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param _headerMap ：请求头信息
     *
     * @return
     */
    public static String getResultByHttpGet(String url, Map<String, String> _headerMap) {
        return getResultByHttpGet(url, _headerMap, null, DEFAULT_CHARSET);
    }

    /**
     * 发起HTTP GET请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param _headerMap ：请求头信息
     * @param charset ：编码格式
     *
     * @return
     */
    public static String getResultByHttpGet(String url, Map<String, String> _headerMap, String charset) {
        return getResultByHttpGet(url, _headerMap, null, charset);
    }

    /**
     * 发起HTTP GET请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param _headerMap ：请求头信息
     * @param _httpProxy ：请求使用的代理信息
     * @param charset ：编码格式，默认UTF-8
     *
     * @return
     */
    public static String getResultByHttpGet(String url, Map<String, String> _headerMap, HttpHost _httpProxy, String charset) {
        String responseText = "";
        HttpResponse httpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = null;
            if (null == _httpProxy) {
                requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            } else {
                requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setProxy(_httpProxy).build();
            }
            httpGet.setConfig(requestConfig);
            if (null != _headerMap) {
                Set<String> headerKeys = _headerMap.keySet();
                for (String headKey : headerKeys) {
                    httpGet.addHeader(headKey, _headerMap.get(headKey));
                }
            }
            httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                responseText = EntityUtils.toString(httpResponse.getEntity(), charset);
            } else {
                throw new Exception("HTTP STATUS CODE：" + statusCode + "\n" + EntityUtils.toString(httpResponse.getEntity(), charset));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpResponse) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
        return responseText;
    }

    /**
     * 发起HTTP POST请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param _paramMap ：Post请求参数
     * @param _contentType ：请求头Content-Type
     *
     * @return
     */
    public static String getResultByHttpPost(String url, Map<String, String> _paramMap, HttpContentTypeEnum _contentType) {
        return getResultByHttpPost(url, _paramMap, null, _contentType, DEFAULT_CHARSET);
    }

    /**
     * 发起HTTP POST请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param _paramMap ：Post请求参数
     * @param _contentType ：请求头Content-Type
     * @param charset ：编码格式
     *
     * @return
     */
    public static String getResultByHttpPost(String url, Map<String, String> _paramMap, HttpContentTypeEnum _contentType, String charset) {
        return getResultByHttpPost(url, _paramMap, null, _contentType, charset);
    }

    /**
     * 发起HTTP POST请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param _paramMap ：Post请求参数
     * @param _headerMap ：请求头信息
     * @param _contentType ：请求头Content-Type
     * @param _contentType ：请求头Content-Type
     *
     * @return
     */
    public static String getResultByHttpPost(String url, Map<String, String> _paramMap, Map<String, String> _headerMap, HttpContentTypeEnum _contentType) {
        return getResultByHttpPost(url, _paramMap, _headerMap, null, _contentType, DEFAULT_CHARSET);
    }

    /**
     * 发起HTTP POST请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param _paramMap ：Post请求参数
     * @param _headerMap ：请求头信息
     * @param _contentType ：请求头Content-Type
     * @param charset ：编码格式
     *
     * @return
     */
    public static String getResultByHttpPost(String url, Map<String, String> _paramMap, Map<String, String> _headerMap, HttpContentTypeEnum _contentType, String charset) {
        return getResultByHttpPost(url, _paramMap, _headerMap, null, _contentType, charset);
    }

    /**
     * 发起HTTP POST请求获取对应URL的返回结果
     *
     * @param url ： 请求URL
     * @param _paramMap ：Post请求参数
     * @param _headerMap ：请求头信息
     * @param _httpProxy ：请求使用的代理信息
     * @param _contentType ：请求头Content-Type
     * @param charset ：编码格式，默认UTF-8
     *
     * @return
     */
    public static String getResultByHttpPost(String url, Map<String, String> _paramMap, Map<String, String> _headerMap, HttpHost _httpProxy, HttpContentTypeEnum _contentType, String charset) {
        String responseText = "";
        HttpResponse httpResponse = null;
        try {
            if (null == _contentType) {
                _contentType = HttpContentTypeEnum.APPLICATION_X_WWW_FORM_URLENCODED;
            }
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = null;
            if (null == _httpProxy) {
                requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            } else {
                requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setProxy(_httpProxy).build();
            }
            httpPost.setConfig(requestConfig);
            if (null != _headerMap) {
                Set<String> headerKeys = _headerMap.keySet();
                for (String headKey : headerKeys) {
                    httpPost.addHeader(headKey, _headerMap.get(headKey));
                }
            }
            httpPost.addHeader("Content-Type", _contentType.getContentType());
            if (HttpContentTypeEnum.APPLICATION_JSON == _contentType) {
                httpPost.setEntity(new StringEntity(JsonUtil.toJson(_paramMap), charset));
            } else {
                List<NameValuePair> params = Lists.newArrayList();
                if (null != _paramMap) {
                    for (String key : _paramMap.keySet()) {
                        params.add(new BasicNameValuePair(key, _paramMap.get(key)));
                    }
                }
                httpPost.setEntity(new UrlEncodedFormEntity(params, charset));
            }
            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                responseText = EntityUtils.toString(httpResponse.getEntity(), charset);
            } else {
                throw new Exception("HTTP STATUS CODE：" + statusCode + "\n" + EntityUtils.toString(httpResponse.getEntity(), charset));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpResponse) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
        return responseText;
    }
}
