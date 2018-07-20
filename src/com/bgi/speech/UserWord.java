package com.bgi.speech;

import com.iflytek.cloud.speech.LexiconListener;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.iflytek.cloud.speech.UserWords;

import java.util.ArrayList;

import com.bgi.util.SystemConfig;
import com.bgi.util.PreProcessor;
import com.bgi.util.SpeechUtil;


public class UserWord {
	public static final String APPID = SystemConfig.APPID;
	public static final boolean debug = SystemConfig.DEBUG;
	private UserWords userWord = null;
	
	public UserWord() {
		SpeechUtility.createUtility("appid=" + APPID);
		userWord = new UserWords();
	}
	
	public void uploadUserWords() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		if (recognizer == null) {
			recognizer = SpeechRecognizer.createRecognizer();
		}

		recognizer.setParameter(SpeechConstant.DATA_TYPE, "userword");
		recognizer.updateLexicon("userword", userWord.toString(), lexiconListener);
	}
	
	/**
	 * 词表上传监听器
	 */
	LexiconListener lexiconListener = new LexiconListener() {
		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error == null) {
				SpeechUtil.Log("*************上传成功*************", debug);
			} else {
				SpeechUtil.Log("*************" + error.getErrorCode() + "*************", debug);
			}
		}
	};
	
	
	public void insertWords(String key, ArrayList<String> value) {
		userWord.putWords(key, value);
	}

	
	public void insertWord(String key, String value) {
		userWord.putWord(key, value);
	}
	
	public static void main(String[] args) {
		UserWord uw = new UserWord();
		String fileName = "rare.txt";
		ArrayList<String> dicts = PreProcessor.loadUserWords(fileName);
		uw.insertWords("rare", dicts);
		
		fileName = "disease.txt";
		dicts = PreProcessor.loadUserWords(fileName);
		uw.insertWords("disease", dicts);
		
		fileName = "cancer.txt";
		dicts = PreProcessor.loadUserWords(fileName);
		uw.insertWords("cancer", dicts);
		
		fileName = "biology.txt";
		dicts = PreProcessor.loadUserWords(fileName);
		uw.insertWords("biology", dicts);
		
		fileName = "drug.txt";
		dicts = PreProcessor.loadUserWords(fileName);
		uw.insertWords("drug", dicts);
		
		uw.uploadUserWords();
	}
}
