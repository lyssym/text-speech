package com.bgi.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.bgi.util.SystemConfig;

public class HttpUtil {
	private static CloseableHttpClient client = null;
    private static RequestConfig config = RequestConfig.custom()
            .setSocketTimeout(5000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .build();
	
    /**
     * 获取http请求
     * @param text
     * @return
     */
	public static String httpGetRequest(String text) {
		try {
			client = HttpClients.createDefault();
			String url = getKeywordURL(text.toLowerCase());
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(config);
			
			HttpResponse response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				String entity = EntityUtils.toString(resEntity);
				JSONObject obj = JSON.parseObject(entity);
				return getExtractWords(obj, "result");
			}
	        
	        httpGet.abort();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	
    /**
     * 获取http请求
     * @param text
     * @return
     */
	public static String httpPostRequest(String text) {
		try {
			client = HttpClients.createDefault();
			Map<String, String> map = new HashMap<String, String>();
			map.put("query", text.toLowerCase());
			HttpPost httpPost = getHttpPost(SystemConfig.KEYURL, map, "UTF-8");
			httpPost.setConfig(config);
			
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				String entity = EntityUtils.toString(resEntity);
				JSONObject obj = JSON.parseObject(entity);
				return getExtractWords(obj, "result");
			}
	        
	        httpPost.abort();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	
	/**
	 * 获取请求URL
	 * @param text
	 * @return
	 */
	public static String getKeywordURL(String text) {
		String url = null;
		try {
			String query = URLEncoder.encode(text, "utf-8");
			url = SystemConfig.KEYURL + "?" + "query=" + query; 
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return url;
	}
	
	
	private static HttpPost getHttpPost(String url, Map<String, String> params, String encode)
			throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		if (params != null) {
			List<NameValuePair> form = new ArrayList<NameValuePair>();
			for (String name : params.keySet()) {
				form.add(new BasicNameValuePair(name, params.get(name)));
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, encode);
			httpPost.setEntity(entity);
		}

		return httpPost;
	}
	
	
	/**
	 * 提取关键词信息
	 * @param obj
	 * @param key
	 * @return
	 */
	public static String getExtractWords(JSONObject obj, String key) {
		return obj.getString(key);
	}
	
	
	public static void main(String[] args) {
		String text = "Please help me find an article about breast cancer.";
//		System.out.println(HttpUtil.httpGetRequest(text));
		System.out.println(HttpUtil.httpPostRequest(text));
	}

}
