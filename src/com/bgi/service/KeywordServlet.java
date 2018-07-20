package com.bgi.service;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bgi.util.SpeechUtil;
import com.bgi.text.Extract;


public class KeywordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(KeywordServlet.class);
	
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
		if (!SpeechUtil.isEmpty(text)) {
			ret = Extract.extract(text);
		}
		
		try {
			response.getWriter().write(ret);
		} catch (IOException e) {
			logger.error("fail to process keyword extracting");
		}
	}
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}
}


