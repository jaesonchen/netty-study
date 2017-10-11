package com.asiainfo.mynetty.future;

import java.nio.channels.SelectableChannel;

import com.asiainfo.mynetty.pipeline.ChannelPipeline;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月3日  上午11:34:59
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelFuture {

	SelectableChannel channel();
	
	ChannelPipeline pipeline();
	
	ChannelFuture addListener(FutureListener listener);
	
	void sync() throws Exception;
	
	void await() throws Exception;
	
	void notifier();
	
	void done();
	
	ChannelFuture closeFuture();
}
