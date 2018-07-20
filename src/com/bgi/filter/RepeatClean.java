package com.bgi.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bgi.util.SpeechUtil;

public class RepeatClean {
	
	/**
	 * 将结果列表中冗余信息进行过滤
	 * @param src
	 * @param dst
	 * @return
	 */
	public static List<String> cleanRepeat(List<String> src, List<String> dst) {
		if (SpeechUtil.isEmepty(src) && SpeechUtil.isEmepty(dst)) {
			return null;
		} else if (SpeechUtil.isEmepty(src) && !SpeechUtil.isEmepty(dst)) {
			return dst;
		} else if (!SpeechUtil.isEmepty(src) && SpeechUtil.isEmepty(dst)) {
			return src;
		} else {
			List<String> ret = new ArrayList<String>(src);
			for (String e : dst) {
				if (!src.contains(e)) {
					ret.add(e);
				}
			}
			
			return ret;
		}
	}
	
	
	/**
	 * 将结果列表中冗余信息进行过滤
	 * @param src
	 * @param dst
	 * @return
	 */
	public static List<String> cleanRepeat(List<String> src, String dst) {
		String[] array = dst.split("\\s+");
		if (SpeechUtil.isEmepty(src) && SpeechUtil.isEmepty(array)) {
			return null;
		} else if (SpeechUtil.isEmepty(src) && !SpeechUtil.isEmepty(array)) {
			return Arrays.asList(array);
		} else if (!SpeechUtil.isEmepty(src) && SpeechUtil.isEmepty(array)) {
			return src;
		} else {
			List<String> ret = new ArrayList<String>(src);
			for (String e : array) {
				if (src.contains(e)) { // 存在则从中删除
					ret.remove(e);
				}
			}
			
			ret.addAll(Arrays.asList(array));
			return ret;
		}
	}
	
}
