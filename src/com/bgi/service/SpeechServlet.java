package com.bgi.service;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bgi.speech.SpeechRecognition;
import com.bgi.util.PostProcessor;
import com.bgi.util.PreProcessor;
import com.bgi.util.SystemConfig;
import com.bgi.util.SpeechUtil;

public class SpeechServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SpeechServlet.class);
	
	static {
		String path = System.getProperty("java.library.path");
		if (!path.contains("msc64")) {
			System.loadLibrary("msc64");
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "content-type, x-requested-with");
		response.setHeader("Access-Control-Max-Age:", "3628800");
		response.setCharacterEncoding("UTF-8");

		String ret = null;
		String name = request.getParameter("name");
//		System.out.println(System.getProperty("java.library.path"));
		if (!SpeechUtil.isEmpty(name)) {
			String path = SystemConfig.AUDIOROOT + name;
			try {
				String target = PreProcessor.transform(path);
				SpeechRecognition recognition = new SpeechRecognition();
				String text = recognition.recognize(target);
				ret = PostProcessor.parseText(text);
			} catch (Exception e) {
				logger.error("fail to process speech stream");
			}
		}
		
		try {
			response.getWriter().write(ret);
		} catch (IOException e) {
			logger.error("fail to process audio stream");
		}
	}
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}
	
	
	public boolean isEmpty(String s) {
		if (s == null || s.isEmpty()) {
			return true;
		}
		
		return false;
	}
}
