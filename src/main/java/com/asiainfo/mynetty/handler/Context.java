package com.asiainfo.mynetty.handler;

import com.asiainfo.mynetty.pipeline.ChannelInitializer;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月5日  上午11:19:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Context {

	private ChannelInitializer initializer;
	private Context() {}
	private static class ContextHolder {
		static Context instance = new Context();
	}
	
	public static Context getContext() {
		return ContextHolder.instance;
	}

	public ChannelInitializer getInitializer() {
		return this.initializer;
	}
	
	public void setInitializer(ChannelInitializer initializer) {
		this.initializer = initializer;
	}
}
