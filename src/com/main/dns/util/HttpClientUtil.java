package com.main.dns.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
/**
 * @Description HttpClient4.5连接池及请求
 * @Author maodelong
 * @Time 2016-7-30 17:54:46
 * @Version 1.0.1
 */
public class HttpClientUtil {
	private static final Boolean PROXY = Utility.PROXY;
	private static final String PROXY_HOSTNAME = Utility.PROXY_HOSTNAME;
	private static final Integer PROXY_PORT = Utility.PROXY_PORT;
	private static RequestConfig httpConfig;
	private static RequestConfig httpConfigFile;
	private static CloseableHttpClient httpClient;
	/** 连接超时时间, 读取超时时间 */
	public final static int CONNECT_TIMEOUT = 10000;
	/** 每个路由(主机)最大并行连接数(实际使用的是这个而不是MAX_TOTAL) */
	public final static int MAX_ROUTE = 200;
	/** 最大总并行连接数 */
	public final static int MAX_TOTAL = 200;
  
	static {
		Builder build = RequestConfig.custom();
		build.setConnectionRequestTimeout(2000).setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(CONNECT_TIMEOUT);
		Builder buildFile = RequestConfig.custom();
		buildFile.setConnectionRequestTimeout(2000).setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(86400000);
		if(PROXY){
			HttpHost proxy = new HttpHost(PROXY_HOSTNAME, PROXY_PORT);
			build.setProxy(proxy);
			buildFile.setProxy(proxy);
		}
		httpConfig = build.build();
		httpConfigFile = buildFile.build();
		
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
		PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager(registry);  
	    pccm.setDefaultMaxPerRoute(MAX_ROUTE);
	    pccm.setMaxTotal(MAX_TOTAL);
	    httpClient = HttpClients.custom().setConnectionManager(pccm).build();
    }
	
	/** GET请求
	 * @param url HTTP地址
	 * @param charset 编码格式 默认UTF-8（GBK，UTF-8等）
	 * @return String body的内容
	 * @throws Exception */
	public static String httpGet(String url, String charset) throws Exception {
		if(null==charset||"".equals(charset)) charset = "UTF-8";
		String responseMsg = "";
		HttpGet method = new HttpGet(url);
		method.setConfig(httpConfig);
		try {
			method.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
		    HttpResponse response = httpClient.execute(method);  
		    if(response.getStatusLine().getStatusCode()==200){
		    	responseMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
		    }else{
		    	throw new Exception("StatusLine="+response.getStatusLine().toString());
		    }
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException("HTTP Connection timeout of "+ CONNECT_TIMEOUT +" ms");
		} finally { 
			method.releaseConnection();
		} 
		return responseMsg;
	}
	
	/** POST请求(参数)
	 * @param url HTTP地址
	 * @param parasMap 参数  HashMap<String,String> map = new HashMap<String,String>();  map.put("aa","内容");
	 * @param charset 编码格式 默认UTF-8（GBK，UTF-8等）
	 * @return String body的内容
	 * @throws Exception */
	public static String httpPost(String url, HashMap<String, String> parasMap, String charset) throws Exception {
		return httpPost(url, parasMap, null, charset);
	}
	
	/** POST请求(参数)
	 * @param url HTTP地址
	 * @param parasMap 参数  HashMap<String,String> map = new HashMap<String,String>();  map.put("aa","内容");
	 * @param cookie 例如, "uid=111,ticket=222"
	 * @param charset 编码格式 默认UTF-8（GBK，UTF-8等）
	 * @return String body的内容
	 * @throws Exception */
	public static String httpPost(String url, HashMap<String, String> parasMap, String cookie, String charset) throws Exception {
		if(null==charset||"".equals(charset)) charset = "UTF-8";
		String responseMsg = "";
		HttpPost method = new HttpPost(url);
		method.setConfig(httpConfig);
		try {
		    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		    if(null!=parasMap){
			    Iterator<?> iter = parasMap.entrySet().iterator();
				while(iter.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Object val = entry.getValue();
					formparams.add(new BasicNameValuePair(key.toString(), val.toString()));
				}
		    }
		    UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(formparams, charset);
		    method.setEntity(urlEntity);
		    if(null!=cookie) method.addHeader(new BasicHeader("Cookie",cookie));
		    
		    HttpResponse response = httpClient.execute(method);
		    if(response.getStatusLine().getStatusCode()==200){
		    	responseMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
		    }else{
		    	throw new Exception("StatusLine="+response.getStatusLine().toString());
		    }
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException("HTTP Connection timeout of "+ CONNECT_TIMEOUT +" ms");
		} finally { 
			method.releaseConnection();
		} 
		return responseMsg;
	}
	
	/** POST请求(body参数)
	 * @param url HTTP地址
	 * @param parasBody BODY参数
	 * @param charset 编码格式 默认UTF-8（GBK，UTF-8等）
	 * @return String body的内容
	 * @throws Exception */
	public static String httpPostBody(String url, String parasBody, String charset) throws Exception {
		if(null==charset||"".equals(charset)) charset = "UTF-8";
		if(null==parasBody) parasBody = "";
		String responseMsg = "";
		HttpPost method = new HttpPost(url);
		method.setConfig(httpConfig);
		try {
			HttpEntity entity = new StringEntity(parasBody, charset);
			method.setEntity(entity);
			
		    HttpResponse response = httpClient.execute(method);
		    if(response.getStatusLine().getStatusCode()==200){
		    	responseMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
		    }else{
		    	throw new Exception("StatusLine="+response.getStatusLine().toString());
		    }
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException("HTTP Connection timeout of "+ CONNECT_TIMEOUT +" ms");
		} finally { 
			method.releaseConnection();
		} 
		return responseMsg;
	}
	
	/** POST请求(文件上传)
	 * @param url HTTP地址
	 * @param parasMap request参数 HashMap<String,String> map = new HashMap<String,String>();  map.put("aa","内容");
	 * @param textMap 文本参数  <input type="text" name="key" value="value" />
	 * @param fileMap 文件参数  <input type="file" name="key" />  value:文件路径
	 * @param charset 编码格式 默认UTF-8（GBK，UTF-8等）
	 * @return String body的内容
	 * @throws Exception */
	public synchronized static String httpPostFile(String url, HashMap<String, String> parasMap, HashMap<String, String> textMap,
			HashMap<String, String> fileMap, String charset) throws Exception {
		if(null==charset||"".equals(charset)) charset = "UTF-8";
		String responseMsg = "";
		HttpPost method = new HttpPost(url);
		method.setConfig(httpConfigFile);
		try{	
		    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if (null != parasMap) {
			    for (Map.Entry<String, String> entry : parasMap.entrySet()) {
					formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
		    }
		    UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(formparams, charset);
		    method.setEntity(urlEntity);
            
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            if (null != textMap) {
	            for (Map.Entry<String, String> entry : textMap.entrySet()) {
	            	builder.addTextBody(entry.getKey(), entry.getValue());
				}
            }
            if (null != fileMap) {
				for (Map.Entry<String, String> entry : fileMap.entrySet()) {
					File f = new File(entry.getValue());
				    FileBody fileBody = new FileBody(f);
				    builder.addPart(entry.getKey(), fileBody);
				}
            }
            //浏览器兼容模式运行防止乱码
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(CharsetUtils.get(charset));
            HttpEntity entity = builder.build();
            method.setEntity(entity);
		    
		    HttpResponse response = httpClient.execute(method);
		    if(response.getStatusLine().getStatusCode()==200){
		    	responseMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
		    }else{
		    	throw new Exception("StatusLine="+response.getStatusLine().toString());
		    }
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally { 
			method.releaseConnection();
		} 
		return responseMsg;
	}
	
	/** POST请求(大文件下载,指定存放路径)
	 * @param url HTTP地址
	 * @param parasMap 参数  HashMap<String,String> map = new HashMap<String,String>();  map.put("aa","内容");
	 * @param charset 编码格式 默认UTF-8（GBK，UTF-8等）
	 * @param filePath 文件存放路径
	 * @throws Exception */
	public static void httpDownload(String url, HashMap<String, String> parasMap, String charset, String filePath) throws Exception{
		if(null==charset||"".equals(charset)) charset = "UTF-8";
		HttpPost method = new HttpPost(url);
		method.setConfig(httpConfigFile);
		try{	
		    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		    if (null != parasMap) {
			    for (Map.Entry<String, String> entry : parasMap.entrySet()) {
					formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
		    }
		    UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(formparams, charset);
		    method.setEntity(urlEntity);
			
		    HttpResponse response = httpClient.execute(method);
		    if(response.getStatusLine().getStatusCode()==200){
		    	InputStream in = response.getEntity().getContent();
		    	FileUtil.getFile(in, filePath);
		    	
		    }else{
		    	throw new Exception("StatusLine="+response.getStatusLine().toString());
		    }
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally { 
			method.releaseConnection();
		} 
	}
	
	/** POST请求(小文件下载,文件必须小于虚拟机的内存)
	 * @param url HTTP地址
	 * @param parasMap 参数  HashMap<String,String> map = new HashMap<String,String>();  map.put("aa","内容");
	 * @param charset 编码格式 默认UTF-8（GBK，UTF-8等）
	 * @return String byte[]
	 * @throws Exception */
	public static byte[] httpDownload(String url, HashMap<String, String> parasMap, String charset) throws Exception{
		if(null==charset||"".equals(charset)) charset = "UTF-8";
		byte[] responseByte = null;
		HttpPost method = new HttpPost(url);
		method.setConfig(httpConfigFile);
		try{	
		    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		    if (null != parasMap) {
			    for (Map.Entry<String, String> entry : parasMap.entrySet()) {
					formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
		    }
		    UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(formparams, charset);
		    method.setEntity(urlEntity);
			
		    HttpResponse response = httpClient.execute(method);
		    if(response.getStatusLine().getStatusCode()==200){
		    	responseByte = EntityUtils.toByteArray(response.getEntity());
		    }else{
		    	throw new Exception("StatusLine="+response.getStatusLine().toString());
		    }
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally { 
			method.releaseConnection();
		} 
		return responseByte;
	}
	

	public static void main(String[] args) throws Exception {
		String result = "";
//		result = httpGet("http://www.baidu.com", null); //百度只能用get请求
//		System.out.println(result);
//		result = httpPost("http://101.200.80.193:8282/res/", null, null);
//		System.out.println(result);
		
		//文件上传
		String url = "http://101.200.80.193:8282/res/file/uploadcloud?key=haoxuehaozhi1234";
		HashMap<String,String> fileMap = new HashMap<String,String>();
		fileMap.put("file","c:/新建图片.jpg");
		result = httpPostFile(url, null, null, fileMap, null);
		System.out.println(result);
	}
	
}