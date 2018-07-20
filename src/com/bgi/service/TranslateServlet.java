package com.bgi.service;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bgi.util.SpeechUtil;
import com.bgi.translate.GoogleApi;


public class TranslateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(TranslateServlet.class);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "content-type, x-requested-with");
		response.setHeader("Access-Control-Max-Age:", "3628800");
		response.setCharacterEncoding("UTF-8");

		String ret = null;
		String text = request.getParameter("query");
		String type = request.getParameter("type");
		int mode = 0;
		if (!SpeechUtil.isEmpty(type)) {
			mode = Integer.parseInt(type);
		}

		if (!SpeechUtil.isEmpty(text)) {
			GoogleApi googleApi = new GoogleApi();
			if (mode > 0) {
				ret = googleApi.translate(text, "zh", "en");
			} else {
				ret = googleApi.translate(text, "en", "zh");
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
}

