package com.xframe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;

public class DateHelper {
	
	public static final String Year="yyyy";
	public static final String Month="MM";
	public static final String Day="dd";
	public static final String Hour="HH";
	public static final String Minute="mm";
	public static final String Second="ss";
	public static final String Full_time="yyyy-MM-dd HH:mm:ss";
	
	
	public static void main(String[] args) {
		
	}
	
	
	public static String ConvertFomatter(String string_date,String original,String after) throws ParseException {
		if (string_date==null||string_date.equals("")) {
			return "";
		}
		Date date = new Date();
		String strDate = string_date;
		SimpleDateFormat sDateFormat = new SimpleDateFormat(original);
		SimpleDateFormat DateFormat = new SimpleDateFormat(after);
		date = sDateFormat.parse(strDate);
		return DateFormat.format(date);
	}
	
	public static String ConvertDateFomatter(Date date,String after) throws ParseException {
		if (date==null) {
			return "";
		}
		SimpleDateFormat DateFormat = new SimpleDateFormat(after);
		return DateFormat.format(date);
	}
	
	/**
	 * @throws ParseException 
	 * @Description 获取上月
	 * @Version 1.0
	 * @Author xinyutian
	 */
	public static String TheLastMonth() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return DateHelper.ConvertDateFomatter(cal.getTime(),"yyyy-MM");
	}
	
	public static String ThisMonth() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 0);
		return DateHelper.ConvertDateFomatter(cal.getTime(),"yyyy-MM");
	}
	
	public static String TheNextMonth() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, +1);
		return DateHelper.ConvertDateFomatter(cal.getTime(),"yyyy-MM");
	}
	
	public static String Today() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 0);
		return DateHelper.ConvertDateFomatter(cal.getTime(),"yyyy-MM-dd");
	}
	
	public static String Now() throws ParseException {
		return DateHelper.ConvertDateFomatter(new Date(),Full_time);
	}
	
	public static String Now_short() throws ParseException {
		return DateHelper.ConvertDateFomatter(new Date(),"yyyyMMddHHmmss");
	}
	
	public static List<String> cal(int split) throws ParseException {
		List<String> times = new ArrayList<>();
		SimpleDateFormat sfm = new SimpleDateFormat("HHmm");
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fm.parse(fm.format(new Date())));
		times.add("000000");
		while (2360-Integer.parseInt(sfm.format(calendar.getTime()))>split) {
			calendar.add(Calendar.MINUTE, split);
			times.add(sfm.format(calendar.getTime())+"00");
		}
		times.add("235959");
		return times;
	}
	
	public List<String> year_month() throws ParseException{
		List<String> list=new ArrayList<>();
		String year=ConvertDateFomatter(new Date(), "yyyy");
		for(int i=1;i<=12;i++) {
			String month="";
			if (i<10) {
				month="0"+Integer.toString(i);
			}else {
				month=Integer.toString(i);
			}
			list.add(year+month);
		}
		return list;
	}
	
	/**
	 * @Description 检测某天是否是节假日
	 * @Version 1.0
	 * @Author xinyutian
	 */
	public boolean isRest(String day, List<Map<String, String>> list) {
		boolean flag = false;
		for (Map<String, String> map : list) {
			String rest = map.get("day");
			if (day.equals(rest)) {
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * @Description 从网络获取某年的法定节假日
	 * @Version 1.0
	 * @Author xinyutian
	 */
	@Before(CacheInterceptor.class)
	@CacheName("restofyear")
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getRestDays(String year) {
		List<Map<String, String>> infos = new ArrayList<>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("d", year);
		String content = null;
		try {
			content = HttpRequestUtils.get("http://tool.bitefu.net/jiari/", params);
			if (null != content) {
				Map<Object, Object> kv = JSON.parseObject(content, Map.class);
				Map<Object, Object> k2 = JSON.parseObject(kv.get(year).toString(), Map.class);
				for (Map.Entry<Object, Object> entry : k2.entrySet()) {
					Map<String, String> info = new HashMap<>();
					info.put("day", year + entry.getKey().toString());
					info.put("state", k2.get(entry.getKey().toString()).toString());
					infos.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
}
