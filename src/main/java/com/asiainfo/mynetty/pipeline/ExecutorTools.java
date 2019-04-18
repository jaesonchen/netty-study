package com.asiainfo.mynetty.pipeline;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月7日  下午11:30:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ExecutorTools {

	private final ExecutorService service = ThreadPoolUtils.getInstance().cachedThreadPool();
	private final ScheduledExecutorService schedule = ThreadPoolUtils.getInstance().scheduledThreadPool(10);
	
	private ExecutorTools() {}
	
	private static class ExecutorToolsHolder {
		static ExecutorTools instance = new ExecutorTools();
	}
	
	public static ExecutorTools getInstance() {
		return ExecutorToolsHolder.instance;
	}
	
	public ExecutorService getExecutor() {
		return service;
	}
	
	public ScheduledExecutorService getSchedule() {
	    return schedule;
	}
}
