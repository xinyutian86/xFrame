package com.xframe.tasks;

import java.text.ParseException;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.xframe.utils.DateHelper;

public class Tasks implements Runnable{
	
	private final static Log LOG = Log.getLog(Tasks.class);

	@Override
	public void run() {
		Prop use = PropKit.use("tasker.properties");
		String task_time = use.get("task_time");
		String[] task_times=task_time.split(",");
		String task_class = use.get("task_class");
		String[] task_classes=task_class.split(",");
		try {
			for(int i=0;i<task_times.length;i++) {
				if(template(task_times[i])) {
					TaskInterface ts= (TaskInterface)Class.forName(task_classes[i]).newInstance();
					ts.doit();
					LOG.info("执行定时任务:"+task_class+"于"+DateHelper.Now());
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static boolean template(String corn) throws ParseException {	
		boolean flag=true;
		String[] args=corn.split("[*]");
		for (String str : args) {
			if (!str.equals("")) {
				int start=corn.indexOf(str);
				int end=corn.indexOf(str)+str.length();
				if (!corn.substring(start, end).endsWith(DateHelper.Now_short().substring(start, end))) {
					flag=false;
				}
			}
		}
		return flag;
	}
}
