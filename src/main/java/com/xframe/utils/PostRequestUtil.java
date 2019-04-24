package com.xframe.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.HttpKit;

public class PostRequestUtil {
	@SuppressWarnings("unchecked")
	public static Map<String, Object> PostParams(HttpServletRequest request) {
		String jsonString = HttpKit.readData(request);
		Map<String, Object> params=JSON.parseObject(jsonString, Map.class);
		return params;
	}
	
	@SuppressWarnings("unchecked")
	public static String get_json_Pram_to_String(HttpServletRequest request,String param) {
		String jsonString = HttpKit.readData(request);
		Map<String, Object> params=JSON.parseObject(jsonString, Map.class);
		return params.get(param).toString();
	}
}
