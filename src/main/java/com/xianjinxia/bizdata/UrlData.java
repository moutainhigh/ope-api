package com.xianjinxia.bizdata;

/**
 * Created wangwei on 2017/11/25.
 */
public class UrlData {

    public static class UrlDataDto{
        public String url;
        public String url_method;
        public String app_id;
        public String method;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl_method() {
            return url_method;
        }

        public void setUrl_method(String url_method) {
            this.url_method = url_method;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public UrlDataDto(String url, String url_method, String app_id, String method) {
            this.url = url;
            this.url_method = url_method;
            this.app_id = app_id;
            this.method = method;
        }

        @Override
        public String toString() {
            return "UrlDataDto{" +
                    "url='" + url + '\'' +
                    ", url_method='" + url_method + '\'' +
                    ", app_id='" + app_id + '\'' +
                    ", method='" + method + '\'' +
                    '}';
        }
    }

    public static class UrlDataWrap{
        public String url;
        public String url_method;
        public String method;
        public UrlDataWrap(String url, String url_method, String method) {
            this.url = url;
            this.url_method = url_method;
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl_method() {
            return url_method;
        }

        public void setUrl_method(String url_method) {
            this.url_method = url_method;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }

}
