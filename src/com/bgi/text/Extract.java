package com.bgi.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.bgi.chunk.Chunk;
import com.bgi.filter.CleanWords;
import com.bgi.filter.RepeatClean;
import com.bgi.io.IOUtil;
import com.bgi.rake.RapidRake;

import com.bgi.util.SpeechUtil;
import com.bgi.util.HttpUtil;

public class Extract {
	
	public static String extract(String text) {
		List<String> keys = RapidRake.rake(text);   // 关键词提取
		List<String> chunks = Chunk.getDataChunking(text); // 数据分块提取
		List<String> ret = RepeatClean.cleanRepeat(keys, chunks); // 重复数据清洗
		ret = CleanWords.cleanWords(ret); // 特定数据清洗
		
		if (!SpeechUtil.isEmepty(ret)) {
			return String.join(" ", ret);
		}
		
		return null;
	}
	
	
	public static String integradeExtract(String text) {
		List<String> keys = RapidRake.rake(text);   // 关键词提取
		String others = HttpUtil.httpPostRequest(text); // 数据分块提取
		List<String> ret = RepeatClean.cleanRepeat(keys, others); // 重复数据清洗
		ret = CleanWords.cleanWords(ret); // 特定数据清洗
		
		if (!SpeechUtil.isEmepty(ret)) {
			return String.join(" ", ret);
		}
		
		return null;
	}
	
	
	public static void main(String[] args) {
		String src = "newqueryenglish.txt";
		String dst = "result.txt";
		try {
			BufferedWriter bw = IOUtil.newBufferedWriter(dst);
			BufferedReader br = IOUtil.loadResource(src, 2);
			String s = null;
			while ((s = br.readLine()) != null) {
				bw.write(s + "\t\t");
				bw.write(integradeExtract(s) + "\n");
			}
			
			bw.close();
			br.close();
		} catch(IOException e) {
			
		}
	}

}
