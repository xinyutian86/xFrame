package com.xframe.common;

import com.jfinal.render.ErrorRender;
import com.jfinal.render.JsonRender;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;

/**
 * 设置500错误返回Json
 */

public class MyRenderFactory extends RenderFactory {
	@Override
	public Render getErrorRender(int errorCode) {
		ResponseResult res = new ResponseResult();
		res.setStatus("4");
		if (errorCode == 500) {
			res.setMessage("服务器异常");
			return new JsonRender(res);
		}else if (errorCode == 404) {
			res.setMessage("404");
			return new ErrorRender(errorCode,"/404.html");
		}
		return super.getErrorRender(errorCode);
	}
}


