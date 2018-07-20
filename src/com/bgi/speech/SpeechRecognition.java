package com.bgi.speech;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;

import com.bgi.util.PostProcessor;
import com.bgi.util.SystemConfig;
import com.bgi.util.SpeechUtil;


public class SpeechRecognition {
	public static final String APPID = SystemConfig.APPID;
	private static boolean debug = SystemConfig.DEBUG;
	private int maxWaitTime = SystemConfig.MAXWAIT;  // 最大等待时间默认为10秒
	private int perWaitTime = SystemConfig.PERWAIT;  // 单次最大等待时间为200毫秒
	private boolean mIsEndOfSpeech = false;
	private StringBuffer mResult = null;
	
	public SpeechRecognition() {
		SpeechUtility.createUtility("appid=" + APPID);
		mResult = new StringBuffer();
		mIsEndOfSpeech = false;
	}
	
	
	public String recognize(String fileName) {
		if (SpeechRecognizer.getRecognizer() == null) {
			SpeechRecognizer.createRecognizer();
		}
		
		mIsEndOfSpeech = false;
		return recognizePcmfileByte(fileName);
	}
	
	/**
	 * 分块传输音频流, 以便能实时
	 * @param fileName
	 */
	public String recognizePcmfileByte(String fileName) {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		recognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
		recognizer.setParameter(SpeechConstant.NET_TIMEOUT, "30000");
//		recognizer.setParameter(SpeechConstant.ASR_PTT, "0");
		recognizer.setParameter(SpeechConstant.VAD_BOS, "3000");
		recognizer.setParameter(SpeechConstant.VAD_EOS, "2500");
		recognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
		recognizer.startListening(recListener);
		
		FileInputStream fis = null;
		final byte[] buffer = new byte[64*1024];
		try {
			fis = new FileInputStream(new File(fileName));
			if (0 == fis.available()) { // 没有有效信息
				mResult.append("no audio avaible!");
				recognizer.cancel();
			} else { // 存在有效信息
				int lenRead = buffer.length;
				while (buffer.length == lenRead && !mIsEndOfSpeech) {
					lenRead = fis.read(buffer);
					recognizer.writeAudio(buffer, 0, lenRead); // 分块写入音频流
				}
				recognizer.stopListening();
			}
			
			return waitSpeechText(recognizer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fis) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * 听写监听器, 用于实时处理语音数据, 当前为了获得较好效果, 将语音数据分块实时传输
	 */
	private RecognizerListener recListener = new RecognizerListener() {
		public void onBeginOfSpeech() {
			SpeechUtil.Log("onBeginOfSpeech enter", debug);
			SpeechUtil.Log( "*************开始录音*************", debug);
		}

		public void onEndOfSpeech() {
			SpeechUtil.Log("onEndOfSpeech enter", debug);
			mIsEndOfSpeech = true;
		}

		public void onVolumeChanged(int volume) {
			SpeechUtil.Log("onVolumeChanged enter", debug);
			if (volume > 0) {
				SpeechUtil.Log("*************音量值: " + volume + "*************", debug);
			}
		}

		/**
		 * 返回分块音频流的结果
		 */
		public void onResult(RecognizerResult result, boolean islast) {
			SpeechUtil.Log("onResult enter", debug);
			if (result != null) { // 当识别结果不为空
				mResult.append(result.getResultString());
			}

			if (islast) { // 判断是否是最后一段语音流
				SpeechUtil.Log("识别结果为: " + mResult.toString(), debug);
				mIsEndOfSpeech = true;
			}
		}

		public void onError(SpeechError error) {
			mIsEndOfSpeech = true;
			SpeechUtil.Log("错误代码: " + error.getErrorCode(), debug);
			SpeechUtil.Log("错误类型: " + error.getErrorType(), debug);
			SpeechUtil.Log("错误描述: " + error.getErrorDescription(true), debug);
		}

		public void onEvent(int eventType, int arg1, int agr2, String msg) {
			SpeechUtil.Log("onEvent enter", debug);
		}
	};
	
	
	/**
	 * 轮询语音识别监听器, 以获取最终的结果
	 * @return
	 */
	public String waitSpeechText(SpeechRecognizer recognizer) {
		while (recognizer.isListening()) {
			if (maxWaitTime < 0) { // 服务访问超时
				return null;
			}
			
			try {
				Thread.sleep(perWaitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  
            maxWaitTime -= perWaitTime;
		}
		
		return mResult.toString();  // 正常服务返回
	}
	
	
	public static void main(String[] args) {
		SpeechRecognition recognition = new SpeechRecognition();
		String fileName = "/home/liuyong/Documents/Audio/3aeaa3f7-bddc-4ee7-85cd-a4e7f1189382.pcm";
		String text = recognition.recognize(fileName);
		System.out.println("最终结果为: " + text);
		System.out.println("最终结果为: " + PostProcessor.parseText(text));
	}

}
