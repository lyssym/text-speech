package com.bgi.util;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtil {
	
	/**
	 * 名词短语的起始位置, 默认从-1开始寻找
	 * @param chunks
	 * @param start
	 * @return
	 */
	public static int next(String[] chunks, int start) {
		int size = chunks.length;
		if (start >= size) {
			return -1;
		}
		
		for (int i = start+1; i < size; i++) {
			String e = chunks[i];
			if (e.equals("B-NP")) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 获取名词短语的结束位置
	 * @param chunks
	 * @param size
	 * @param start
	 * @return
	 */
	public static int end(String[] chunks, int size, int start) {
		int index = start+1;
		for (int i = start+1; i < size; index++) {
			if (i != index) { // 次序失配,则跳出循环
				break;
			}
			
			if (!chunks[index].equals("I-NP")) {
				break;
			} else { // 若属于短语中间元素, 则继续递增
				i++; // 按照次序递增
			}
		}
		
		if (index > start+1) { // 次序递增至结束位置
			return index;
		}
		
		return -1;
	}
	
	/**
	 * 寻找近邻名词短语, 根据名词短语起始位置进行叠加
	 * @param chunks
	 * @param next
	 * @return
	 */
	public static boolean isInner(String[] chunks, int next) {
		if (chunks[next].equals("I-NP")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 根据名词短语标记判断元素是否为名词词性, 避免一些介宾短语误判为名词短语
	 * @param tags
	 * @param next
	 * @return
	 */
	public static boolean isNoun(String[] tags, int next) {
		if (tags[next].startsWith("N") || tags[next].equals("FW")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 根据分块属性及词性综合判断元素是否有效
	 * @param chunks
	 * @param tags
	 * @param next
	 * @return
	 */
	public static boolean isValid(String[] chunks, String[] tags, int next) {
		if (isInner(chunks, next) && isNoun(tags, next)) {
			return true;
		}

		return false;
	}
	
	/**
	 * 从起始位置至结束位置判断名词短语是否有效
	 * @param chunks
	 * @param tags
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean isValid(String[] chunks, String[] tags, int start, int end) {
		for (int i = start; i < end; i++) {
			if (!isValid(chunks, tags, i)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 获取名词短语
	 * @param tokens
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getNounPhrases(String[] tokens, int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++) {
			if (i == end -1) {
				sb.append(tokens[i]);
			} else {
				sb.append(tokens[i] + " ");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 获取最终分块结果
	 * @param chunks
	 * @param tags
	 * @param tokens
	 * @return
	 */
	public static List<String> getDataChunk(String[] chunks, String[] tags, String[] tokens) {
		int size = chunks.length;
		List<String> ret = new ArrayList<String>();
		int start = next(chunks, -1); // 短语有效信息起始位置
		while (start >= 0 && start < size) { // 短语信息有效
			int end = end(chunks, size, start);
			if (end > 0) { // 名词词组
				if (isValid(chunks, tags, start, end)) {
					ret.add(getNounPhrases(tokens, start, end));
				}
				start = end;
			} else { // 单个名词
				start++;
			}
		}
		return ret;
	}

}
