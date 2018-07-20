package com.bgi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置信息
 * @author liuyong
 * 
 */
public class SystemConfig {
	public static String APPID = null;
	public static boolean DEBUG = false;
	public static int MAXWAIT = 0;
	public static int PERWAIT = 0;
	public static String AUDIOROOT = null;
	
	public static String GOOGLE = null;
	public static String TRANSLATE = null;
	public static String JSSCRIPT = null;
	
	public static String CHUNKER = null;
	public static String POS = null;
	public static String SENT = null;
	
	public static String KEYURL = null;

	static {
		try {
			Properties p = new Properties();
			InputStream is = null;
			is = SystemConfig.class.getClassLoader().getResourceAsStream("config/system.config");
			p.load(is);
			
			APPID = p.getProperty("appid").trim();
			DEBUG = Boolean.parseBoolean(p.getProperty("debug").trim());
			MAXWAIT = Integer.parseInt(p.getProperty("maxwait").trim());
			PERWAIT = Integer.parseInt(p.getProperty("perwait").trim());
			AUDIOROOT = p.getProperty("audioroot").trim();
			
			GOOGLE = p.getProperty("googleroot").trim();
			TRANSLATE = p.getProperty("translate").trim();
			JSSCRIPT = p.getProperty("jsscript").trim();
			
			CHUNKER = p.getProperty("chunker").trim();
			POS = p.getProperty("pos").trim();
			SENT = p.getProperty("sent").trim();
			
			KEYURL = p.getProperty("keyurl").trim();
			
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
