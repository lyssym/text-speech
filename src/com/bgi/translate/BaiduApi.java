package com.bgi.translate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.bgi.translate.util.TransApi;
import com.bgi.util.SpeechUtil;

public class BaiduApi {
    private static final String APP_ID = "20171018000089127";
    private static final String SECURITY_KEY = "oxHQ8IodFp45h6ZDrnBH";
    
    public static String translate(String text, String srcLang, String dstLang) {
    	TransApi api = new TransApi(APP_ID, SECURITY_KEY);
    	String ret = api.getTransResult(text, srcLang, dstLang);
    	JSONObject obj = JSON.parseObject(ret);
    	JSONArray array = obj.getJSONArray("trans_result");
    	int size = array.size();
    	for (int i = 0; i < size; i++) {
    		JSONObject tmp = array.getJSONObject(i);
    		String target = tmp.getString("dst");
    		if (!SpeechUtil.isEmpty(target)) {
    			return target;
    		}
    	}
    	
    	return null;
    }
    
    public static void main(String[] args) {
        String query = "高度600米";
        System.out.println(translate(query, "zh", "en"));
    }

}
