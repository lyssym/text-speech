package com.bgi.service;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bgi.util.PostProcessor;
import com.bgi.util.PreProcessor;
import com.bgi.util.SpeechUtil;
import com.bgi.util.SystemConfig;
import com.bgi.speech.SpeechRecognition;
import com.bgi.text.Extract;
import com.bgi.translate.GoogleApi;

public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(UpdateServlet.class);
	public static GoogleApi googleApi = new GoogleApi();
	
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
		if (!SpeechUtil.isEmpty(name)) {
			String path = SystemConfig.AUDIOROOT + name;
			String query = null;
			try {
				String target = PreProcessor.transform(path);
				SpeechRecognition recognition = new SpeechRecognition();
				String text = recognition.recognize(target);
				query = PostProcessor.parseText(text);
				
				if (!SpeechUtil.isEmpty(query)) {
					String source = query;
					if (SpeechUtil.isChinese(query)) { // 若是中文则进行翻译处理
						source = googleApi.translate(query, "zh", "en");
					}

					if (!SpeechUtil.isEmpty(source)) {
						ret = Extract.extract(source);
					}
				}
			} catch (Exception e) {
				logger.error("fail to process speech stream");
			}

			if (SpeechUtil.isEmpty(ret)) { // 音频流输入检索时未获取到语义信息
				ret = "DNA cancer";
			}
		}

		try {
			response.getWriter().write(ret);
		} catch (IOException e) {
			logger.error("fail to process speech search");
		}
	}
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}
}
