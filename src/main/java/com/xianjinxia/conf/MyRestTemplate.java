package com.xianjinxia.conf;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.logcenter.http.CatHttpClientFactory;
import com.xianjinxia.service.remote.UrlContent;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 更换默认httpfactory
 * 
 * @author hym 2017年9月14日
 */
@Component
public class MyRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyGsonHttpMessageConverter myGsonHttpMessageConverter;

    @Autowired
    @Qualifier("restTemplateWithAbsoluteUrl")
    private RestTemplate restTemplateWithAbsoluteUrl;

    @Bean
    public RestTemplate restTemplateDefault(){
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplateWithAbsoluteUrl(){
        return new RestTemplate();
    }

    private final static String HTTP = "http://";

    @PostConstruct
    protected void init() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // SSLContext sslContext = new
        // SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
        // public boolean isTrusted(X509Certificate[] arg0, String arg1) throws
        // CertificateException {
        // return true;
        // }
        // }).build();
        // httpClientBuilder.setSSLContext(sslContext);
        // HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        // SSLConnectionSocketFactory sslConnectionSocketFactory = new
        // SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        // RegistryBuilder.<ConnectionSocketFactory>create().register("https",
        // sslConnectionSocketFactory)
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory()).build();// 注册http
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);// 开始设置连接池
        poolingHttpClientConnectionManager.setMaxTotal(1000); // 最大连接数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(600); // 同路由并发数
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));// 重试次数
        HttpClient httpClient = CatHttpClientFactory.createHttpClient(httpClientBuilder);
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectTimeout(10000);// 连接超时
        clientHttpRequestFactory.setReadTimeout(20000);// 数据读取超时时间
        clientHttpRequestFactory.setConnectionRequestTimeout(20000);// 连接不够用的等待时间
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        restTemplateWithAbsoluteUrl.setRequestFactory(clientHttpRequestFactory);
        restTemplate.getMessageConverters().add(0, myGsonHttpMessageConverter);
        restTemplateWithAbsoluteUrl.getMessageConverters().add(0, myGsonHttpMessageConverter);
    }

    /**
     * http post 请求 ParameterizedTypeReference<List<MyBean>> myBean = new
     * ParameterizedTypeReference<List<MyBean>>() {}; ResponseEntity<List<MyBean>> response =
     * template.exchange("http://example.com",HttpMethod.POST, requestObject, myBean);
     *
     * @param url
     * @param requestObject
     * @param
     * @return
     */
    public <T> T httpPost(String url, Object requestObject,
                          ParameterizedTypeReference<T> classType) {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(requestObject, httpHeader);
        return restTemplate.exchange(HTTP + url, HttpMethod.POST, httpEntity, classType).getBody();
    }

    /**
     * 发送post请求
     * @param url 请求链接
     * @param param 参数
     * @param responseType 返回类型
     * @param <T> 返回类型
     * @return
     */
    public  <T> T httpPostForm(String url, Object param
            , Class<T> responseType) {
        HttpHeaders httpHeaders = buildBasicFORMHeaders();
        return postForEntity(HTTP + url, param, httpHeaders, responseType);
    }

    /**
     * 获取一个application/x-www-form-urlencoded头
     *
     * @return
     */
    public HttpHeaders buildBasicFORMHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    /**
     * 发送post请求
     * @param url 请求链接
     * @param param 参数
     * @param httpHeaders 请求头
     * @param responseType 返回类型
     * @param <T> 返回类型
     * @return
     */
    public <T> T postForEntity(String url, Object param
            , HttpHeaders httpHeaders , Class<T> responseType) {
        T result = null;
        try {
            HttpEntity<?> httpEntity = new HttpEntity<>(param, httpHeaders);
            // 构建参数
            httpEntity = convert(httpEntity);
            // 发送请求
            ResponseEntity<T> responseEntity = restTemplateWithAbsoluteUrl.postForEntity(url, httpEntity, responseType);
            // 返回内容
            result = responseEntity.getBody();
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    /**
     * 对bean对象转表单模型做处理
     *
     * @param requestEntity
     * @return
     */
    private static HttpEntity<?> convert(HttpEntity<?> requestEntity) {
        Object body = requestEntity.getBody();
        HttpHeaders headers = requestEntity.getHeaders();

        if (body == null) {
            return requestEntity;
        }

        if (body instanceof Map) {
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            Map<String, ?> _body = (Map<String, ?>) body;
            for (String key : _body.keySet()) {
                multiValueMap.add(key, MapUtils.getString(_body, key));
            }

            requestEntity = new HttpEntity<>(multiValueMap, headers);
        }
        if (body instanceof String) {
            return requestEntity;
        }

        if (body instanceof Collection) {
            return requestEntity;
        }

        if (body instanceof Map) {
            return requestEntity;
        }


        if (headers != null && MediaType.APPLICATION_JSON.equals(headers.getContentType())) {
            return new HttpEntity<>(JSON.toJSONString(body), headers);
        }

        if (headers == null || !MediaType.APPLICATION_FORM_URLENCODED.equals(headers.getContentType())) {
            return requestEntity;
        }

        MultiValueMap<String, Object> formEntity = new LinkedMultiValueMap<>();

        Field[] fields = body.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            String value = null;

            try {
                value = BeanUtils.getProperty(body, name);
            } catch (Exception e) {
            }

            formEntity.add(name, value);
        }

        return new HttpEntity<>(formEntity, headers);
    }



    public <T> T httpPostWithAbsoluteUrl(String url, Object requestObject,
            ParameterizedTypeReference<T> classType) {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(requestObject, httpHeader);
        return restTemplateWithAbsoluteUrl
                .exchange(HTTP + url, HttpMethod.POST, httpEntity, classType).getBody();
    }

    /**
     * http get 请求
     * 
     * @param url
     * @param
     * @return
     * @throws URISyntaxException
     * @throws RestClientException
     */
    public <T> T httpGet(String url, Object requestObject,
            ParameterizedTypeReference<T> classType) {
        try {
            return restTemplate.exchange(buildUrlForHttpGet(HTTP + url, requestObject),
                    HttpMethod.GET, null, classType).getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T httpGetWithAbsoluteUrl(String url, Object requestObject,
            ParameterizedTypeReference<T> classType) {
        try {
            return restTemplateWithAbsoluteUrl
                    .exchange(buildUrlForHttpGet(HTTP + url, requestObject), HttpMethod.GET, null,
                            classType)
                    .getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * http get 无需请求参数
     * 
     * @param url
     * @param classType
     * @return
     */
    public <T> T httpGet(String url, ParameterizedTypeReference<T> classType) {
        return httpGet(url, null, classType);
    }

    public <T> T httpGetWithAbsoluteUrl(String url, ParameterizedTypeReference<T> classType) {
        return httpGetWithAbsoluteUrl(url, null, classType);
    }

    private String buildUrlForHttpGet(String requestUrl, Object requestObject)
            throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(requestUrl);
        Set<Map.Entry<Object, Object>> paramMap = new BeanMap(requestObject).entrySet();
        for (Map.Entry<Object, Object> entry : paramMap) {
            if (!"class".equals(entry.getKey()) && entry.getValue() != null)
                uriBuilder.addParameter((String) entry.getKey(), entry.getValue().toString());
        }
        return uriBuilder.toString();
    }

    /**
     * http post 请求 add by dengwenjie 2017-11-03
     * @param url
     * @param headers
     * @return
     */
    public <T> T post4Passport(String url, HttpHeaders headers, ParameterizedTypeReference<T> classType) {
        return httpGetPost(url, HttpMethod.POST, headers, classType);
    }


    /**
     * http get 或 post 请求 add by dengwenjie 2017-11-03
     * @param url
     * @param method
     * @param headers
     * @return
     */
    private <T> T httpGetPost(String url, HttpMethod method, HttpHeaders headers, ParameterizedTypeReference<T> classType) {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        return restTemplate.exchange(HTTP + url, method, httpEntity, classType).getBody();
    }

    /**
     * wangwei
     * @return
     */
    public <T> T absoluteUrlCall(UrlContent urlContent, Object requestObject,
                                 ParameterizedTypeReference<T> classType) {
        if("post".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpPostWithAbsoluteUrl(urlContent.getUrl(), requestObject,classType);
            //return httpPostWithAbsoluteUrl("localhost:8080/gateway", requestObject,classType);
        }else if("get".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpGetWithAbsoluteUrl(urlContent.getUrl(), requestObject,classType);
        }
        return null;
    }


    /**
     * wangwei
     * @return
     */
    public <T> T call(UrlContent urlContent, Object requestObject,
                      ParameterizedTypeReference<T> classType) {
        if("post".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpPost(urlContent.getUrl(), requestObject,classType);
        }else if("get".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpGet(urlContent.getUrl(), requestObject,classType);
        }
        return null;
    }


    public <T> T httpPutWithAbsoluteUrl(String url, Object requestObject,
                                        ParameterizedTypeReference<T> classType, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(requestObject, headers);
        return restTemplateWithAbsoluteUrl
                .exchange(HTTP + url, HttpMethod.PUT, httpEntity, classType).getBody();
    }

    public ResponseEntity<String> httpWithAbsoluteUrl(String url, Object requestObject, HttpMethod method, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(requestObject, headers);
        return restTemplateWithAbsoluteUrl
                .exchange(HTTP + url, method, httpEntity, String.class);
    }


    /**
     * chunliny
     * @return
     */
    public <T> T absoluteUrlCall(UrlContent urlContent, Object requestObject,
                                 ParameterizedTypeReference<T> classType, HttpHeaders headers) {
        if("post".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpPostWithAbsoluteUrl(urlContent.getUrl(), requestObject,classType);
            //return httpPostWithAbsoluteUrl("localhost:8080/gateway", requestObject,classType);
        }else if("get".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpGetWithAbsoluteUrl(urlContent.getUrl(), requestObject,classType);
        }else if("put".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpPutWithAbsoluteUrl(urlContent.getUrl(), requestObject,classType, headers);
        }
        return null;
    }

    /**
     * chunliny
     * @return
     */
    public ResponseEntity<String> absoluteUrlCall(UrlContent urlContent, Object requestObject, HttpHeaders headers) {
        if("post".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpWithAbsoluteUrl(urlContent.getUrl(), requestObject, HttpMethod.POST, headers);
        }else if("get".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpWithAbsoluteUrl(urlContent.getUrl(), requestObject, HttpMethod.GET, headers);
        }else if("put".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpWithAbsoluteUrl(urlContent.getUrl(), requestObject, HttpMethod.PUT, headers);
        }
        return null;
    }


    /**
     * chunliny
     * @return
     */
    public <T> T call(UrlContent urlContent, Object requestObject,
                      ParameterizedTypeReference<T> classType, HttpHeaders headers) {
        if("post".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpPost(urlContent.getUrl(), requestObject,classType);
        }else if("get".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpGet(urlContent.getUrl(), requestObject,classType);
        }else if("put".equalsIgnoreCase(urlContent.getUrlMethod())){
            return httpPutWithAbsoluteUrl(urlContent.getUrl(), requestObject,classType, headers);
        }
        return null;
    }





}
