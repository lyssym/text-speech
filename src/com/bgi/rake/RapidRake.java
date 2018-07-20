package com.bgi.rake;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Arrays;

import com.bgi.rake.model.*;
import com.bgi.rake.dict.*;
import com.bgi.rake.RakeAlgorithm;

import com.bgi.util.SystemConfig;

public class RapidRake {
	public static RakeAlgorithm rakeAlg = null;
	
	static {
		try {
			initModel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void initModel() throws IOException {
		// Create a parameter object. See the RakeParams docs for details.
		String[] stopWords = new SmartWords().getSmartWords(); 
		String[] stopPOS = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"}; 
		int minWordChar = 1;
		boolean shouldStem = true;
		String phraseDelims = "[-,.?():;\"!/]";	
		RakeParams params = new RakeParams(stopWords, stopPOS, minWordChar, 
				shouldStem, phraseDelims);
			
		// Create a RakeAlgorithm object
		InputStream posStream = RapidRake.class.getClassLoader().getResourceAsStream(SystemConfig.POS);  // The path to your POS tagging model
		InputStream sentStream = RapidRake.class.getClassLoader().getResourceAsStream(SystemConfig.SENT); // The path to your sentence detection model
		rakeAlg = new RakeAlgorithm(params, posStream, sentStream);
	}
	
	
	public static List<String> rake(String text) {
		Result result = rakeAlg.rake(text);
		Result ret = result.distinct();
		return Arrays.asList(ret.getFullKeywords());
	}


	public static void main(String[] args) throws java.io.IOException {
		String txt = "Can you search for articles about breast cancer?";
		List<String> ret = rake(txt);
		System.out.println(ret);
		
	}
}