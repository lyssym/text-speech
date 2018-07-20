package com.bgi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.bgi.io.IOUtil;
import com.bgi.util.SystemConfig;


public class PreProcessor {
	/**
	 * 音频格式转换
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String transform(String fileName) throws Exception {
		File file = new File(fileName);
        if (!file.exists()) {  
            SpeechUtil.Log("文件不存在： " + fileName, SystemConfig.DEBUG);  
            throw new RuntimeException("文件不存在："+fileName);  
        }

        if (!checkAudioFormat(fileName)) return null;
        String name = fileName.substring(0, fileName.lastIndexOf(".")) + ".pcm";
        findAndDelete(name);
        String target = enhanceAudio(fileName);
        if (target == null) target = fileName;
        
        String cmd = "ffmpeg -i " + target + " -f s16le -ac 1 -ar 16000 -acodec pcm_s16le " + name; // 处理为单声道音频流
        Process process = Runtime.getRuntime().exec(cmd);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));   
        String line = null;
        while ((line = br.readLine()) != null) {  
            System.out.println(line);  
        }
        br.close();
        process.waitFor();
        process.destroy();
        
        return name;
	}
	
	public static String enhanceAudio(String fileName) {
		try {
			String name = fileName.substring(0, fileName.lastIndexOf(".")) + "-eha" + ".wav";
			findAndDelete(name);
			String cmd = "ffmpeg -i "+ fileName + " -filter:a " + "volume=1.8 " + name;
	        Process process = Runtime.getRuntime().exec(cmd);
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));   
	        String line = null;
	        while ((line = br.readLine()) != null) {  
	            System.out.println(line);  
	        }
	        br.close();
	        process.waitFor();
	        process.destroy();
	        
	        return name;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static void findAndDelete(String fileName) {
		File file = new File(fileName);
        if (file.exists()) {  
            file.delete();
        }
	}
	
	/**
	 * 音频格式校验
	 * @param fileName
	 * @return
	 */
	public static boolean checkAudioFormat(String fileName) {
		int index = fileName.lastIndexOf(".");
		String suffix = fileName.substring(index + 1);
		if (suffix.equalsIgnoreCase("mp3") || suffix.equalsIgnoreCase("wav")
		 || suffix.equalsIgnoreCase("wma") || suffix.equalsIgnoreCase("pcm")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 加载用户词典信息
	 * @param fileName
	 * @return
	 */
	public static ArrayList<String> loadUserWords(String fileName) {
		try {
			BufferedReader br = IOUtil.loadResource(fileName, 2);
			String s = null;
			ArrayList<String> ret = new ArrayList<String>();
			while ((s = br.readLine()) != null) {
				if (!SpeechUtil.isEmpty(s)) {
					if (!ret.contains(s.trim())) {
						ret.add(s.trim());
					}
				}
			}
			br.close();
			
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static void main(String[] args) {
		String fileName = "/home/liuyong/Documents/Audio/3aeaa3f7-bddc-4ee7-85cd-a4e7f1189382.wav";
		try {
			String target = transform(fileName);
			System.out.println("result: " + target);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
