package com.bgi.util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;


public class PostProcessor {
	/**
	 * 根据特殊字符将文本进行分割
	 * @param text
	 * @return
	 */
	public static List<String> splitJSON(String text) {
		if (SpeechUtil.isEmpty(text)) { // 数据内容为空, 无需处理
			return null;
		}
		
		List<String> ret = new ArrayList<String>();
		String[] array = text.split("\\}\\{");
		int len = array.length;
		if (len < 2) {
			ret.add(array[0]);
		} else {
			for (int i = 0; i < len; i++) {
				if (i == 0) {
					ret.add(array[i] + "}");
				} else if (i == len - 1) {
					ret.add("{" + array[i]);
				} else {
					ret.add("{" + array[i] + "}");
				}
			}
		}
		
		return ret;
	}
	
	/***
	 * 对JSON文本进行分割, 并进行处理
	 * @param json
	 * @return
	 */
	public static String parseText(String json) {
		List<String> tokens = splitJSON(json);
		if (tokens != null) {
			return parseJSON(tokens);
		}
		
		return null;
	}
	
	/**
	 * 对多个JSON文本分别进行处理
	 * @param tokens
	 * @return
	 */
	public static String parseJSON(List<String> tokens) {
		StringBuffer sb = new StringBuffer();
		for (String token : tokens) {
			String tmp = parseJSON(token);
			if (!SpeechUtil.isEmpty(tmp)) {
				sb.append(tmp);
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 对单个JSON文本进行处理
	 * @param text
	 * @return
	 */
	public static String parseJSON(String text) {
		if (SpeechUtil.isEmpty(text)) { // 数据内容为空, 无需处理
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		try {
			JSONObject start = new JSONObject(text);
			JSONArray second = start.getJSONArray("ws");
			int size = second.length();
			for (int i = 0; i < size; i++) {
				JSONObject sub = second.getJSONObject(i);
				String subLevel = parseObject(sub);
				if (!SpeechUtil.isEmpty(subLevel)) {
					sb.append(subLevel);
				}
			}

			return sb.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 对子JSON文本进行处理
	 * @param sub
	 * @return
	 */
	public static String parseObject(JSONObject sub) {
		try {
			JSONArray src = sub.getJSONArray("cw");
			StringBuilder sb = new StringBuilder();
			
			int size = src.length();
			for (int i = 0; i < size; i++) {
				JSONObject dst = src.getJSONObject(i);
				sb.append(dst.getString("w"));
			}
			
			return sb.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
