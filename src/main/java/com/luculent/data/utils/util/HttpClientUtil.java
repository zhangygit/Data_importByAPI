package com.luculent.data.utils.util;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class HttpClientUtil {

	/**
	 * 防止类被实例化
	 */
	private HttpClientUtil() {
		// TODO Auto-generated constructor stub
		throw new AssertionError();
	}

	private final static  Logger logger = LogManager.getLogger(HttpClientUtil.class);
	
	private final static int CONNECTION_TIMEOUT = 100;
	
	/**ocr地址*/
	private final static String OCR_PATH =System.getProperty("user.dir") +"\\src\\main\\resources\\";;
	/**图片输出地址*/
	private final static String TEMP_PATH =System.getProperty("user.dir") +"\\src\\main\\webapp\\temp\\";
	/**图片扩展名 */
	private final static String IMG_TYPE =".gif";
	/**请求类型 返回数据 */
	private final static String ASK_TYPE_DATA="DATA";
	/**请求类型 返回图片 */
	private final static String ASK_TYPE_IMG="IMG";
	/**请求类型 返回验证码 */
	private final static String ASK_TYPE_CODE="CODE";
	
	/**
	 * 以get方式获取返回值
	 * @param url
	 * @return
	 */
	public static String getContent(String url){
		if(logger.isDebugEnabled()) {
			logger.debug("开始获取网页：" + url);
		}
		return getContent(url, null,null, CONNECTION_TIMEOUT,ASK_TYPE_DATA);
	}
	
	/**
	 * 以get方式获取图片 返回图片uuid文件名
	 * @param url
	 * @return
	 */
	public static String getImageDownLoad(String url){
		if(logger.isDebugEnabled()) {
			logger.debug("开始获取图片：" + url);
		}
		return getContent(url, null,null, CONNECTION_TIMEOUT,ASK_TYPE_IMG);
	}
	
	/**
	 * 以get方式获取验证码返回值
	 * @param url
	 * @return
	 */
	public static String getCodeResult(String url){
		if(logger.isDebugEnabled()) {
			logger.debug("开始获取图片：" + url);
		}
		return getContent(url, null,null, CONNECTION_TIMEOUT,ASK_TYPE_CODE);
	}
	
	
	/**
	 * 以get方式获取返回值
	 * @param url
	 * @param params 字符串
	 * @return
	 */
	public static String getContent(String url,String... params){
		if(logger.isDebugEnabled()) {
			logger.debug("开始获取网页：" + url);
		}
		return getContent(url,ConventionUtils.toMap(params));
	}
	
	
	/**
	 * 以get方式获取返回值
	 * @param url
	 * @param params 参数Map
	 * @return
	 */
	public static String getContent(String url,Map<String,String> params){
		if(logger.isDebugEnabled()) {
			logger.debug("开始获取网页：" + url);
		}
		return getContent(url, null,params, CONNECTION_TIMEOUT,ASK_TYPE_DATA);
	} 
	/**
	 * 
	* @Title. getContent
	* @Description. 以get方式获取网页内容
	* @param url
	* @param params
	* @return String
	* @exception.
	 */
	public static String getContent(String url,Map<String,String> headers,Map<String,String> params){
		if(logger.isDebugEnabled()) {
			logger.debug("开始获取网页：" + url);
		}
		return getContent(url, headers,params, CONNECTION_TIMEOUT,ASK_TYPE_DATA);
	}
	
	/**
	 * 
	* @Title. postContent
	* @Description. 以post方式map参数获取网页内容
	* @param url
	* @param params
	* @return String
	* @exception.
	 */
	public static String postMapContent(String url,Map<String,String> headers,Map<String, String> params) {
		if(logger.isDebugEnabled()) {
			logger.debug("开始抓取网页：" + url);
		}
		return postContent(url,headers, params,null, CONNECTION_TIMEOUT);
	}
	
	/**
	 * 
	* @Title. postMapContent
	* @Description. 以post方式json参数获取网页内容
	* @param url
	* @param headers
	* @param jsonStr
	* @return String
	* @exception.
	 */
	public static String postJsonContent(String url,Map<String,String> headers,String jsonStr) {
		if(logger.isDebugEnabled()) {
			logger.debug("开始抓取网页：" + url);
		}
		return postContent(url,headers, null,jsonStr, CONNECTION_TIMEOUT);
	}
	

	/**
	 * 
	* @Title. postJsonContent
	* @Description. 以post方式json参数获取网页内容
	* @param url
	* @param headers
	* @param jsonStr
	* @param timeout
	* @return String
	* @exception.
	 */
	public static String postJsonContent(String url,Map<String,String> headers,String jsonStr,int timeout) {
		if(logger.isDebugEnabled()) {
			logger.debug("开始抓取网页：" + url);
		}
		return postContent(url,headers, null,jsonStr, timeout);
	}
	
	
	
	/**
	 * 
	* @Title. postContent
	* @Description. 设置post 的url 等参数
	* @param url
	* @param params
	* @param connectionTimeoutSeconds
	* @return String
	* @exception.
	 */
	private static String postContent(String url,Map<String,String> headers,Map<String, String> params,String jsonStr, int connectionTimeoutSeconds) {
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		RequestConfig requestConfig = RequestConfig
				.custom()
				.setSocketTimeout(connectionTimeoutSeconds * 1000)
				.setConnectTimeout(connectionTimeoutSeconds * 1000)
				.setConnectionRequestTimeout(connectionTimeoutSeconds * 1000)
				.build();//设置请求和传输超时时间

		
		HttpContext httpContext = null;
		
		HttpPost post = new HttpPost( url );
		post.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 7.0; Windows NT 5.1) ");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Connection", "close");
		if(headers != null && !headers.isEmpty()){
			for (Entry<String, String> header : headers.entrySet()) {
				post.setHeader(header.getKey() , header.getValue());
			}
		}
		post.setConfig(requestConfig);
		setPostMapParameters(post, params);
		setPostJsonParameters(post, jsonStr);
		return getPostContent(client, post, httpContext);
	}
	
	/**
	 * 
	* @Title. setPostParameters
	* @Description. 设置post map参数 
	* @param post
	* @param params void
	* @exception.
	 */
	private static void setPostMapParameters(HttpPost post, Map<String, String> params) {
		if(params != null && !params.isEmpty()) {
			List<NameValuePair> clientParams = new ArrayList<NameValuePair>();
			for (Entry<String, String> param : params.entrySet()) {
				clientParams.add(new BasicNameValuePair( param.getKey() , param.getValue() ));
			}
			try {
				//用于向请求对象中写入请求实体
				post.setEntity(new UrlEncodedFormEntity(clientParams, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.debug("编码失败", e);
			}
		}
	}
	
	/**
	 * 
	* @Title. setPostParameters
	* @Description. 设置post json参数 
	* @param post
	* @param params void
	* @exception.
	 */
	private static void setPostJsonParameters(HttpPost post, String jsonStr) {
		if(StringUtils.isNotEmpty(jsonStr)){
			 StringEntity entity = new StringEntity(jsonStr,"utf-8");
			 post.setEntity(entity);
		}
	}
	
	/**
	 * 
	* @Title. getPostContent
	* @Description. 以Post方式抓取网页
	* @param client
	* @param post
	* @param httpContext
	* @return String
	* @exception.
	 */
	private static String getPostContent(CloseableHttpClient  client, HttpPost post, HttpContext httpContext) {
		 CloseableHttpResponse  response = null;
		try {
			if(httpContext != null) {
				response = client.execute(post, httpContext);
			}else{
				response = client.execute(post);
			}
			if(response.getStatusLine().getStatusCode() != 200) {
				post.abort();
				logger.error("抓取网页【"+ post.getURI() +"】内容错误：" + response.getStatusLine().getStatusCode());
				return null;
			}
			//这句话是得到响应的内容，内容里面有很多信息
			String result = EntityUtils.toString(response.getEntity());
			EntityUtils.consume(response.getEntity());
			logger.info("获取网页"+ post.getURI() +"成功");
			return result;
		} catch (Exception e) {
			post.abort();
			logger.error("获取网页"+ post.getURI() +"出错", e);
			return "获取网页"+ post.getURI() +"出错";
		} finally{
			try {
				response.close();
				client.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	* @Title. getContent
	* @Description. 设置getUrl和参数等信息
	* @param url
	* @param params
	* @param connectionTimeoutSeconds
	* @return String
	* @exception.
	 */
	public static String getContent(String url,Map<String,String> headers,Map<String, String> params, int connectionTimeoutSeconds,String getType) {
			
		CloseableHttpClient client = HttpClients.createDefault();
		
		RequestConfig requestConfig = RequestConfig
				.custom()
				.setSocketTimeout(connectionTimeoutSeconds * 1000)
				.setConnectTimeout(connectionTimeoutSeconds * 1000)
				.setConnectionRequestTimeout(connectionTimeoutSeconds * 1000)
				.build();//设置请求和传输超时时间

		
		
		HttpGet get = new HttpGet( url );
		get.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 7.0; Windows NT 5.1) ");
		get.setHeader("Content-Type", "application/x-www-form-urlencoded");
		get.setHeader("Connection", "close");
		if(headers != null && !headers.isEmpty()){
			for (Entry<String, String> header : headers.entrySet()) {
				get.setHeader(header.getKey() , header.getValue());
			}
		}
		
		get.setConfig(requestConfig);
		setGetParameters(get, params);
		switch (getType) {
		case ASK_TYPE_DATA:
			return getContent(client, get); 
		case ASK_TYPE_IMG:
			return getImageDownLoad(client, get); 
		case ASK_TYPE_CODE:
			return getCodeCheckedStr(client, get); 
		default:
			return getContent(client, get); 
		}
		
		
	}
	
	
	/**
	 * 
	* @Title. setGetParameters
	* @Description. 设置get参数
	* @param get
	* @param params void
	* @exception.
	 */
	private static void setGetParameters(HttpGet get, Map<String, String> params) {
		if(params != null && !params.isEmpty()) {
			List<NameValuePair> clientParams = new ArrayList<NameValuePair>();
			for (Entry<String, String> param : params.entrySet()) {
				clientParams.add(new BasicNameValuePair( param.getKey() , param.getValue() ));
			}
			try {
				//用于向请求对象中写入请求实体
				URI uri = new URIBuilder(get.getURI()).addParameters(clientParams).setCharset(Charset.forName("UTF-8")).build();
				get.setURI(uri);
			} catch (URISyntaxException e) {
				logger.debug("编码失败", e);
			}
			
		}
	}
	
	/**
	 * 
	* @Title. getContent
	* @Description. 以get方式获取网页内容
	* @param client
	* @param get
	* @return String
	* @exception.
	 */
	private static String getContent(CloseableHttpClient  client, HttpGet get){
		CloseableHttpResponse  response = null;
		try {  
            // 执行get请求.    
            response = client.execute(get);  
            if(response.getStatusLine().getStatusCode()!= 200){
            	get.abort();
				logger.error("获取网页【"+ get.getURI() +"】内容错误：" + response.getStatusLine().getStatusCode());
				return null;
            }
            
            String result =EntityUtils.toString(response.getEntity());
            //释放资源
            EntityUtils.consume(response.getEntity());
            logger.info("获取网页"+ get.getURI() +"成功");
            return result;
        } catch (Exception e) {
			get.abort();
			logger.error("获取网页"+ get.getURI() +"出错", e);
			return e.getMessage();
		} finally {  
            // 关闭连接,释放资源    
            try {  
            	response.close();  
            	client.close();
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
	}
	
	private static String getImageDownLoad(CloseableHttpClient  client, HttpGet get){
		CloseableHttpResponse  response = null;
		String uuid = UUIDBuilder.getUUID();
		try {  
            // 执行get请求.    
            response = client.execute(get);  
            if(response.getStatusLine().getStatusCode()!= 200){
            	get.abort();
				logger.error("获取图片【"+ get.getURI() +"】内容错误：" + response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
            	
				InputStream input = entity.getContent();
				OutputStream output = new FileOutputStream(new File(TEMP_PATH+uuid+IMG_TYPE));
				IOUtils.copy(input, output);
				output.flush();
			}
            //释放资源
            EntityUtils.consume(entity);
            logger.info("获取网页"+ get.getURI() +"成功");
            return uuid;
        } catch (Exception e) {
			get.abort();
			logger.error("获取网页"+ get.getURI() +"出错", e);
			return e.getMessage();
		} finally {  
            // 关闭连接,释放资源    
            try {  
            	response.close();  
            	client.close();
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
	}
	
	private static String getCodeCheckedStr(CloseableHttpClient  client, HttpGet get){
		CloseableHttpResponse  response = null;
		BufferedImage bi = null;
		String res ="";
		try {  
            // 执行get请求.    
            response = client.execute(get);  
            if(response.getStatusLine().getStatusCode()!= 200){
            	get.abort();
				logger.error("获取图片【"+ get.getURI() +"】内容错误：" + response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
            	
				InputStream input = entity.getContent();
				bi = ImageIO.read(input);
				EntityUtils.consume(entity);
	            Tesseract tessreact = new Tesseract();  
	    		tessreact.setDatapath(OCR_PATH); 
	    		tessreact.setLanguage("eng");
	    		res = tessreact.doOCR(bi).replace("\n", ""); 
				
			}
            logger.info("获取验证码图片【"+ get.getURI() +"】相应的字符串为："+res);
    		return res.trim(); 
        } catch (Exception e) {
			get.abort();
			logger.error("获取图片【"+ get.getURI() +"】出错", e);
			return null;
		}  finally {  
            // 关闭连接,释放资源    
            try {  
            	response.close();  
            	client.close();
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
	}
	
	public static void main(String[] args) {
		getCodeResult("http://106.39.199.78/cpad/getInputImage?clientUniqueID=3c2a3a936a24257ccb72b81ff578231f5602ac8758706154af3d4f1ff29e6e7e");
	}

}
