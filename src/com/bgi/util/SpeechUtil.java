package com.bgi.util;

import java.util.Date;
import java.util.List;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.text.SimpleDateFormat;

public class SpeechUtil {
	
	public static boolean isEmpty(String s) {
		if (s == null || s.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isEmepty(List<String> src) {
		if (src == null || src.size() == 0) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isEmepty(String[] src) {
		if (src == null || src.length == 0) {
			return true;
		}
		
		return false;
	}
	
	public static String[] tokenizer(SnowballStemmer stemmer, String sentence) {
		sentence = sentence.replaceAll("([-,.?():;\"!/])", " $1 ");
		String[] tokenArray = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
		int size = tokenArray.length;
		String[] ret = new String[size];
		for (int i = 0; i < size; i++) {
			ret[i] = stemmer.stem(tokenArray[i]).toString();
		}
		
		return ret;
	}
	
	
	public static String[] tokenizer(String sentence) {
		sentence = sentence.replaceAll("([-,.?():;\"!/])", " $1 ");
		String[] tokenArray = WhitespaceTokenizer.INSTANCE.tokenize(sentence.toLowerCase());
		return tokenArray;
	}

	public static void Log(String log, boolean debug) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String date = dateFormat.format(new Date());
		
		if (debug && !isEmpty(log)) {
		    System.out.println("<" + date + ">" + log);
		}
	}
	
	/**
	 * 判断字符是否为中文
	 * @param c
	 * @return
	 */
	public static boolean isChineseByScript(char c) {  
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);  
        if (sc == Character.UnicodeScript.HAN) {  
            return true;  
        }  
        return false;  
    }
	
	/**
	 * 判断文本是否包含中文字符, 若存在中文字符, 则判定为中文文本
	 * @param text
	 * @return
	 */
	public static boolean isChinese(String text) {
		int len = text.length();
		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (isChineseByScript(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(isChinese("中国人"));
	}

}
