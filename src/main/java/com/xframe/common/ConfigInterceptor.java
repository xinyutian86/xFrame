package com.xframe.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 
* @ClassName  ConfigInterceptor
* @Description 总配置拦截器;该拦截器处理项目跨域问题
* @author Huf,Xinyt
* @date 2017-7-17
 */
public class ConfigInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		inv.getController().getResponse().addHeader("Access-Control-Allow-Method", "GET,POST,PUT,OPTIONS,DELETE");
		inv.getController().getResponse().addHeader("Access-Control-Allow-Origin", "*");
		inv.getController().getResponse().addHeader("Access-Control-Allow-Headers","x-requested-with,content-type,authorization");
		inv.getController().getResponse().addHeader("Content-Type","multipart/form-data");
		System.err.println(inv.getController().getRequest().getMethod());
		if (inv.getController().getRequest().getMethod().equals("OPTIONS")) {
			System.out.println("访问IP:"+inv.getController().getRequest().getLocalAddr());
			System.out.println("访问UA:"+inv.getController().getRequest().getHeader("User-Agent"));
			ResponseResult res=new ResponseResult();
			res.setStatus("0");
			res.setMessage("OPTIONS预请求");
			res.setData("");
			inv.getController().renderJson(res);
			return;
		}
		inv.invoke();
	}

}

