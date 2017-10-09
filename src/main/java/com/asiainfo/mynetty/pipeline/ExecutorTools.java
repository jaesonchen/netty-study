package com.asiainfo.mynetty.pipeline;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月7日  下午11:30:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ExecutorTools {

	private final ExecutorService service = Executors.newCachedThreadPool();
	
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
}
