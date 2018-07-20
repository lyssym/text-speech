package com.bgi.filter;

import java.util.ArrayList;
import java.util.List;

import com.bgi.util.SpeechUtil;

public class CleanWords {
	
	/**
	 * 构建过滤词典
	 * @return
	 */
	public static String[] getFilterWords() {
		return new String[] {
				"papers", "paper",
				"all documents", "some documents",
				"a document", "any document",
				"document", "documents",
				"related documents",
				"article", "articles",
				"related article", "related articles",
				"related papers", "related paper", 
				"some articles", "an article",
				"many articles", "all articles",
				"materials", "some materials", 
				"material", "related material",
				"literature", "any literature",
				"the literature", "all literature",
				"some literature", "literature related",
				"information", "informational",
				"related materials", "informational materials",
				"studies",
				"the", "that", "about", "all", "some", "many", "any", "a", "an", "one",
				"search", "list", "filter", "select", "study", "research"
				};
	}
	
	/**
	 * 过滤指定词典信息
	 * @param src
	 * @return
	 */
	public static List<String> cleanWords(List<String> src) {
		if (!SpeechUtil.isEmepty(src)) {
			List<String> ret = new ArrayList<String>(src);
			for (String e : getFilterWords()) {
				if (src.contains(e)) {
					ret.remove(e);
				}
			}
			
			return ret;
		}
		
		return null;
	}

}
