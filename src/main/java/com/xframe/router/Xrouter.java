package com.xframe.router;

import com.jfinal.config.Routes;
import com.xframe.controller.LogController;

public class Xrouter extends Routes{

	@Override
	public void config() {
		// TODO 自动生成的方法存根
		this.add("/api", LogController.class); // 第三个参数为该Controller的视图存放路径
	}

}
