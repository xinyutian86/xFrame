package com.xframe.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 分页插件
 * @Description 逻辑分页插件，静态调用SetStartPage传入需要分页的List集合即可
 * @version 1.1
 * @author XinYutian
 *
 */
public class PageHelper {
	public static Map<String,Object> SetStartPage(List<?> list,int pageNow,int Size){
		boolean isFristPage=false;
		boolean isLastPage=false;
		boolean haveNexPage=false;
		boolean havePerPage=false;
		int pageSize=0;
		int totalRow=list.size();
		int fromIndex=(pageNow-1)*Size;
		int toIndex=pageNow*Size;
		if(fromIndex==0) {
			isFristPage=true;
		}else if (!isFristPage) {
			havePerPage=true;
		}
		if(toIndex>=totalRow) {
			toIndex=totalRow;
			isLastPage=true;
		}else if (!isLastPage) {
			haveNexPage=true;
		}
		if(totalRow%Size==0) {
			pageSize=totalRow/Size;
		}else {
			pageSize=totalRow/Size+1;
		}
		Map<String,Object> map=new HashMap<>();
		map.put("pageIndex", pageNow);
		map.put("totalPage", pageSize);
		map.put("totalCount", totalRow);
		map.put("pageSize", Size);
		map.put("fristPage", isFristPage);
		map.put("lastPage", isLastPage);
		map.put("haveNexPage", haveNexPage);
		map.put("havePerPage", havePerPage);
		map.put("list", list.subList(fromIndex, toIndex));
		return map;
	}
}