package com.sljl.core.utils;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Http请求工厂，可同时支持http和https
 *
 * @author L.Y.F
 */
public class HttpClientFactory {

    /**
     * 普通httpClient，支持http和https
     */
    private static final CloseableHttpClient closeable_http_client;

    /**
     * 信任所有服务的httpClient，支持http和https，并且无条件信任
     */
    private static final CloseableHttpClient trust_all_http_client;

    static {
        try {
            // 创建普通httpClient
            SSLContext sslContext = SSLContexts.custom().build();
            closeable_http_client = HttpClients.custom().setSSLContext(sslContext).build();

            // 创建无条件信任的httpClient
            SSLContext trustAllSslContext = SSLContexts.custom().build();
            // 使用免验证manager初始化
            trustAllSslContext.init(null, new X509TrustManager[]{new TrustAllX509TrustManager()}, null);
            trust_all_http_client = HttpClients.custom().setSSLContext(trustAllSslContext).build();
        } catch (Exception e) {
            throw new RuntimeException("初始化httpclient失败", e);
        }
    }

    /**
     * 获取普通的closeableHttpClient
     *
     * @return
     */
    public static CloseableHttpClient getDefaultCloseableHttpClient() {
        return closeable_http_client;
    }

    /**
     * 获取无条件信任任何https服务器的closeableHttpClient
     * 在请求https报 PKIX path building failed 表示服务端不受信任时，使用此对象
     *
     * @return
     */
    public static CloseableHttpClient getTrustAllCloseableHttpClient() {
        return trust_all_http_client;
    }


    /**
     * 此X509TrustManager实现类信任任何https服务器，在需要的时候使用，如报 PKIX path building failed
     *
     * @author L.Y.F
     */
    private static class TrustAllX509TrustManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

    ;

}
