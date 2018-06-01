package com.xianjinxia.conf;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.xianjinxia.enums.HttpEnum;
import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.response.HttpResponse;
import com.xianjinxia.service.remote.UrlContent;

/**
 * HttpTemplate
 * @author chunliny
 *
 */
@SuppressWarnings("deprecation")
@Component
public class HttpTemplate {

	private static final Logger logger = LoggerFactory.getLogger(HttpTemplate.class);
	
	private static final String HTTPS = "https";

	private static final String HTTP = "http";

	private static int MAX_TIMEOUT = 20000;

	private static int MAX_TOTAL = 500;

	private static int DEFAULT_MAX_PERROUTE = 300 ;
	
	private PoolingHttpClientConnectionManager poolConnManager = null;

	public void init() {
		
		try {
			SSLContext sslcontext = SSLContexts.custom()
					.loadTrustMaterial(null, new TrustSelfSignedStrategy())
					.build();
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext, hostnameVerifier);
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						@Override
						public boolean isTrusted(
								java.security.cert.X509Certificate[] chain,
								String authType)
								throws java.security.cert.CertificateException {
							return true;
						}
					}).build();
	        httpClientBuilder.setSSLContext(sslContext);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register(HTTP,PlainConnectionSocketFactory.getSocketFactory())
					.register(HTTPS, sslsf).build();
			poolConnManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			poolConnManager.setMaxTotal(MAX_TOTAL);
			poolConnManager.setDefaultMaxPerRoute(DEFAULT_MAX_PERROUTE);
			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(MAX_TIMEOUT).build();
			poolConnManager.setDefaultSocketConfig(socketConfig);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化SSLConnectionSocketFactory失败, errorMsg:" + e);
		}
	}

	public CloseableHttpClient getHttpClient() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(MAX_TIMEOUT).setConnectTimeout(MAX_TIMEOUT).setSocketTimeout(MAX_TIMEOUT).build();  
		if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
			logger.info("now client pool "+ poolConnManager.getTotalStats().toString());
		}
		return HttpClients.custom().setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).build();
	}
	
	public HttpResponse doService(UrlContent urlContent, String requestBody,HttpHeaders headers ){
		 if("post".equalsIgnoreCase(urlContent.getUrlMethod())){
			 return doPost(urlContent.getUrl(), requestBody, headers);
		 }else if("get".equalsIgnoreCase(urlContent.getUrlMethod())){
			 return doGet(urlContent.getUrl(), requestBody, headers);
		 }
		return null;
	}

	public HttpResponse doPost(String url, String requestBody,HttpHeaders headers) {
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String rspContent = null;
		CloseableHttpResponse response = null;
		HttpResponse httpResponse = new HttpResponse();
		InputStream in = null;
		try {
			StringEntity requestEntity = setHeaders(requestBody, headers,httpPost);
			requestEntity.setContentEncoding("UTF-8");
			requestEntity.setContentType("application/json;charset=UTF-8");//发送json数据需要设置contentType
			httpPost.setEntity(requestEntity);
			
			response = httpClient.execute(httpPost);
			in = response.getEntity().getContent();
			rspContent = IOUtils.toString(in);
			httpResponse.setCode(HttpEnum.SUCCESS.getCode());
			int statusCode = response.getStatusLine().getStatusCode();
			httpResponse.setMsg("响应码:" + statusCode + ", rspContent:" + rspContent);
			httpResponse.setStatusCode(statusCode);
			addHeaders(response, httpResponse);
		}catch (Exception e) {
			logger.error(
					"Call url=" + url + "service error, errorMsg:"
							+ e.getMessage(), e);
			throw new ServiceException(BaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY.getCode(), e.getMessage());
		} finally {

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpResponse;
	}

	private void addHeaders(CloseableHttpResponse response, HttpResponse httpResponse) {
		Header[] rspHeaders = response.getAllHeaders();
		for (Header header : rspHeaders) {
			httpResponse.getRspHeaders().put(header.getName(), header.getValue());
		}
	}

	private StringEntity setHeaders(String requestBody, HttpHeaders headers,
			HttpPost httpPost) throws UnsupportedEncodingException {
		StringEntity requestEntity = new StringEntity(requestBody,"UTF-8");
		if (MapUtils.isNotEmpty(headers)) {
			for (Entry<String, List<String>> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue().get(0));
			}
		}
		return requestEntity;
	}

	public HttpResponse doGet(String url, String requestBody, HttpHeaders headers) {
		CloseableHttpClient httpClient = getHttpClient();
		HttpGet httpget = new HttpGet(url);
		String rspContent = null;
		CloseableHttpResponse response = null;
		HttpResponse httpResponse = null;
		InputStream in = null;
		try {
			response = httpClient.execute(httpget);
			in = response.getEntity().getContent();
			rspContent = IOUtils.toString(in);
			httpResponse = new HttpResponse();
			httpResponse.setCode(HttpEnum.SUCCESS.getCode());
			httpResponse.setMsg(rspContent);
			httpResponse.setStatusCode(response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			logger.error(
					"Call url=" + url + "service error, errorMsg:"
							+ e.getMessage(), e);
			throw new ServiceException(BaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY.getCode(), e.getMessage());
		} finally {

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpResponse;
	}
}
