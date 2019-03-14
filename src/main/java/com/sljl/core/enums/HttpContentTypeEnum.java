package com.sljl.core.enums;

/**
 * @author L.Y.F
 * @create 2019-03-14 15:28
 */
public enum HttpContentTypeEnum {

    /** Content-type : application/x-www-form-urlencoded */
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    /** Content-type : application/json */
    APPLICATION_JSON("application/json"),
    ;

    private HttpContentTypeEnum(String contentType) {
        this.contentType = contentType;
    }

    private String contentType;

    public String getContentType() {
        return contentType;
    }
}
