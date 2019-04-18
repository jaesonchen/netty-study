package com.asiainfo.mynetty.handler;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.pipeline.ChannelInitializer;

/**
 * @Description: ChannelPipeline 的Handler 初始化程序
 * 
 * @author       zq
 * @date         2017年10月5日  上午11:19:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HandlerInitializerContext {

    private Map<EventLoopGroup, ChannelInitializer> map = new HashMap<>();
	private HandlerInitializerContext() {}
	private static class ContextHolder {
		static HandlerInitializerContext instance = new HandlerInitializerContext();
	}
	
	public static HandlerInitializerContext getContext() {
		return ContextHolder.instance;
	}

	public ChannelInitializer getInitializer(EventLoopGroup group) {
		return this.map.get(group);
	}
	
	public void setInitializer(EventLoopGroup group, ChannelInitializer initializer) {
        this.map.put(group, initializer);
    }
}
